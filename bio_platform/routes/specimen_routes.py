from flask import Blueprint, request, jsonify
from bio_platform.models import Specimen, Donor, StorageLocation
from bio_platform.schemas.specimen_schema import SpecimenSchema
from bio_platform.app import db
import uuid

specimen_bp = Blueprint('specimen_bp', __name__)
specimen_schema = SpecimenSchema()
specimens_schema = SpecimenSchema(many=True)

@specimen_bp.route('', methods=['POST'])
def add_specimen():
    try:
        data = request.get_json()
        if not data:
            return jsonify({"message": "No input data provided"}), 400

        # Generate public_id if not provided
        if 'public_id' not in data or not data['public_id']:
            data['public_id'] = str(uuid.uuid4())
        # Ensure public_id is unique if provided
        elif Specimen.query.filter_by(public_id=data['public_id']).first():
            return jsonify({"message": f"Specimen with public_id {data['public_id']} already exists."}), 409

        # Validate foreign keys: donor_id and storage_location_id (if provided)
        # The schema expects donor_id (PK), not public_id for input
        if 'donor_id' in data and not Donor.query.get(data['donor_id']):
            return jsonify({"message": f"Donor with id {data['donor_id']} not found."}), 404

        if 'storage_location_id' in data and data['storage_location_id'] is not None and \
           not StorageLocation.query.get(data['storage_location_id']):
            return jsonify({"message": f"StorageLocation with id {data['storage_location_id']} not found."}), 404

        loaded_data = specimen_schema.load(data)

    except Exception as e:
        from marshmallow import ValidationError
        if isinstance(e, ValidationError):
            return jsonify({"message": "Validation error", "errors": e.messages}), 400
        return jsonify({"message": "Error processing request", "error": str(e)}), 500

    try:
        new_specimen = Specimen(**loaded_data)
        db.session.add(new_specimen)
        db.session.commit()
        # Dump with context to potentially load related models for display if needed by schema
        return jsonify(specimen_schema.dump(new_specimen)), 201
    except Exception as e:
        db.session.rollback()
        # A more specific error might be useful if it's an IntegrityError (e.g. FK constraint)
        from sqlalchemy.exc import IntegrityError
        if isinstance(e, IntegrityError):
             return jsonify({"message": "Database integrity error. Check foreign keys or unique constraints.", "error": str(e)}), 409
        return jsonify({"message": "Error adding specimen to database", "error": str(e)}), 500

@specimen_bp.route('', methods=['GET'])
def get_specimens():
    # Basic query, can be expanded with filters later
    all_specimens = Specimen.query.all()
    return jsonify(specimens_schema.dump(all_specimens)), 200

@specimen_bp.route('/<string:public_id>', methods=['GET'])
def get_specimen(public_id):
    specimen = Specimen.query.filter_by(public_id=public_id).first()
    if not specimen:
        return jsonify({"message": "Specimen not found"}), 404
    return jsonify(specimen_schema.dump(specimen)), 200

@specimen_bp.route('/<string:public_id>', methods=['PUT'])
def update_specimen(public_id):
    specimen = Specimen.query.filter_by(public_id=public_id).first()
    if not specimen:
        return jsonify({"message": "Specimen not found"}), 404

    try:
        data = request.get_json()
        if not data:
            return jsonify({"message": "No input data provided"}), 400

        # Validate foreign keys if they are part of the update
        if 'donor_id' in data and data['donor_id'] is not None and not Donor.query.get(data['donor_id']):
            return jsonify({"message": f"Donor with id {data['donor_id']} not found."}), 404

        if 'storage_location_id' in data and data['storage_location_id'] is not None and \
           not StorageLocation.query.get(data['storage_location_id']):
            return jsonify({"message": f"StorageLocation with id {data['storage_location_id']} not found."}), 404

        # public_id should not be updatable this way. If it needs to change, it's more complex.
        if 'public_id' in data and data['public_id'] != specimen.public_id:
             # Check uniqueness if attempting to change public_id
            if Specimen.query.filter_by(public_id=data['public_id']).first():
                 return jsonify({"message": f"Another specimen with public_id {data['public_id']} already exists."}), 409

        loaded_data = specimen_schema.load(data, partial=True)

    except Exception as e:
        from marshmallow import ValidationError
        if isinstance(e, ValidationError):
            return jsonify({"message": "Validation error", "errors": e.messages}), 400
        return jsonify({"message": "Error processing request for update", "error": str(e)}), 500

    try:
        for key, value in loaded_data.items():
            setattr(specimen, key, value)

        db.session.commit()
        return jsonify(specimen_schema.dump(specimen)), 200
    except Exception as e:
        db.session.rollback()
        from sqlalchemy.exc import IntegrityError
        if isinstance(e, IntegrityError):
             return jsonify({"message": "Database integrity error during update.", "error": str(e)}), 409
        return jsonify({"message": "Error updating specimen in database", "error": str(e)}), 500

@specimen_bp.route('/<string:public_id>', methods=['DELETE'])
def delete_specimen(public_id):
    specimen = Specimen.query.filter_by(public_id=public_id).first()
    if not specimen:
        return jsonify({"message": "Specimen not found"}), 404

    try:
        db.session.delete(specimen)
        db.session.commit()
        return jsonify({"message": "Specimen deleted"}), 200
    except Exception as e:
        db.session.rollback()
        return jsonify({"message": "Error deleting specimen from database", "error": str(e)}), 500
