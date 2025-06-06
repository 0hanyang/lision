from flask import Blueprint, request, jsonify
from bio_platform.models import Donor, Specimen # Added Specimen to check for associations
from bio_platform.schemas.donor_schema import DonorSchema
from bio_platform.app import db
import uuid # For generating public_id

donor_bp = Blueprint('donor_bp', __name__)
donor_schema = DonorSchema()
donors_schema = DonorSchema(many=True)

@donor_bp.route('', methods=['POST'])
def add_donor():
    try:
        data = request.get_json()
        if not data:
            return jsonify({"message": "No input data provided"}), 400

        # Generate public_id if not provided
        if 'public_id' not in data or not data['public_id']:
            data['public_id'] = str(uuid.uuid4())
        # Ensure public_id is unique if provided
        elif Donor.query.filter_by(public_id=data['public_id']).first():
            return jsonify({"message": f"Donor with public_id {data['public_id']} already exists."}), 409

        # Marshmallow 3 .load() returns data, .validate() returns errors.
        # If .load() fails due to validation, it raises ValidationError.
        loaded_data = donor_schema.load(data)

    except Exception as e: # Catches marshmallow.exceptions.ValidationError too
        # It's better to catch ValidationError specifically if you want to return its messages
        from marshmallow import ValidationError
        if isinstance(e, ValidationError):
            return jsonify({"message": "Validation error", "errors": e.messages}), 400
        # db.session.rollback() # Not needed here as no db operation started
        return jsonify({"message": "Error processing request", "error": str(e)}), 500

    try:
        new_donor = Donor(
            public_id=loaded_data['public_id'],
            age_range=loaded_data.get('age_range'),
            sex=loaded_data.get('sex'),
            consent_status=loaded_data.get('consent_status', False)
        )
        db.session.add(new_donor)
        db.session.commit()
        return jsonify(donor_schema.dump(new_donor)), 201
    except Exception as e:
        db.session.rollback()
        return jsonify({"message": "Error adding donor to database", "error": str(e)}), 500

@donor_bp.route('', methods=['GET'])
def get_donors():
    all_donors = Donor.query.all()
    return jsonify(donors_schema.dump(all_donors)), 200

@donor_bp.route('/<string:public_id>', methods=['GET'])
def get_donor(public_id):
    donor = Donor.query.filter_by(public_id=public_id).first()
    if not donor:
        return jsonify({"message": "Donor not found"}), 404
    return jsonify(donor_schema.dump(donor)), 200

@donor_bp.route('/<string:public_id>', methods=['PUT'])
def update_donor(public_id):
    donor = Donor.query.filter_by(public_id=public_id).first()
    if not donor:
        return jsonify({"message": "Donor not found"}), 404

    try:
        data = request.get_json()
        if not data:
            return jsonify({"message": "No input data provided"}), 400

        # Use schema.load with partial=True for updates
        # This will validate present fields and ignore missing ones
        loaded_data = donor_schema.load(data, partial=True)

    except Exception as e: # Catches marshmallow.exceptions.ValidationError too
        from marshmallow import ValidationError
        if isinstance(e, ValidationError):
            return jsonify({"message": "Validation error", "errors": e.messages}), 400
        return jsonify({"message": "Error processing request for update", "error": str(e)}), 500

    try:
        # Update fields if they are in loaded_data
        for key, value in loaded_data.items():
            setattr(donor, key, value)

        db.session.commit()
        return jsonify(donor_schema.dump(donor)), 200
    except Exception as e:
        db.session.rollback()
        return jsonify({"message": "Error updating donor in database", "error": str(e)}), 500

@donor_bp.route('/<string:public_id>', methods=['DELETE'])
def delete_donor(public_id):
    donor = Donor.query.filter_by(public_id=public_id).first()
    if not donor:
        return jsonify({"message": "Donor not found"}), 404

    # Check for associated specimens
    if Specimen.query.filter_by(donor_id=donor.id).first():
        return jsonify({"message": "Cannot delete donor with associated specimens. Please delete or reassign specimens first."}), 400

    try:
        db.session.delete(donor)
        db.session.commit()
        return jsonify({"message": "Donor deleted"}), 200
    except Exception as e:
        db.session.rollback()
        return jsonify({"message": "Error deleting donor from database", "error": str(e)}), 500
