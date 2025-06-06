from flask import Blueprint, request, jsonify
from bio_platform.models import StorageLocation, Specimen # Added Specimen for check
from bio_platform.schemas.storage_schema import StorageLocationSchema
from bio_platform.app import db

storage_bp = Blueprint('storage_bp', __name__)
storage_location_schema = StorageLocationSchema()
storage_locations_schema = StorageLocationSchema(many=True)

@storage_bp.route('', methods=['POST'])
def add_storage_location():
    try:
        data = request.get_json()
        if not data:
            return jsonify({"message": "No input data provided"}), 400

        # Check if name is unique
        if 'name' in data and StorageLocation.query.filter_by(name=data['name']).first():
            return jsonify({"message": f"Storage location with name '{data['name']}' already exists."}), 409

        loaded_data = storage_location_schema.load(data)

    except Exception as e:
        from marshmallow import ValidationError
        if isinstance(e, ValidationError):
            return jsonify({"message": "Validation error", "errors": e.messages}), 400
        return jsonify({"message": "Error processing request", "error": str(e)}), 500

    try:
        new_location = StorageLocation(**loaded_data)
        db.session.add(new_location)
        db.session.commit()
        return jsonify(storage_location_schema.dump(new_location)), 201
    except Exception as e:
        db.session.rollback()
        return jsonify({"message": "Error adding storage location to database", "error": str(e)}), 500

@storage_bp.route('', methods=['GET'])
def get_storage_locations():
    all_locations = StorageLocation.query.all()
    return jsonify(storage_locations_schema.dump(all_locations)), 200

@storage_bp.route('/<int:location_id>', methods=['GET'])
def get_storage_location(location_id):
    location = StorageLocation.query.get(location_id)
    if not location:
        return jsonify({"message": "Storage location not found"}), 404
    return jsonify(storage_location_schema.dump(location)), 200

@storage_bp.route('/<int:location_id>', methods=['PUT'])
def update_storage_location(location_id):
    location = StorageLocation.query.get(location_id)
    if not location:
        return jsonify({"message": "Storage location not found"}), 404

    try:
        data = request.get_json()
        if not data:
            return jsonify({"message": "No input data provided"}), 400

        # Check for name uniqueness if name is being changed
        if 'name' in data and data['name'] != location.name and StorageLocation.query.filter_by(name=data['name']).first():
            return jsonify({"message": f"Storage location with name '{data['name']}' already exists."}), 409

        loaded_data = storage_location_schema.load(data, partial=True)

    except Exception as e:
        from marshmallow import ValidationError
        if isinstance(e, ValidationError):
            return jsonify({"message": "Validation error", "errors": e.messages}), 400
        return jsonify({"message": "Error processing request for update", "error": str(e)}), 500

    try:
        for key, value in loaded_data.items():
            setattr(location, key, value)

        db.session.commit()
        return jsonify(storage_location_schema.dump(location)), 200
    except Exception as e:
        db.session.rollback()
        return jsonify({"message": "Error updating storage location in database", "error": str(e)}), 500

@storage_bp.route('/<int:location_id>', methods=['DELETE'])
def delete_storage_location(location_id):
    location = StorageLocation.query.get(location_id) # Use .get() for primary key lookup
    if not location:
        return jsonify({"message": "Storage location not found"}), 404

    # Check for associated specimens
    if Specimen.query.filter_by(storage_location_id=location.id).first():
        return jsonify({"message": "Cannot delete storage location with associated specimens. Please move or delete specimens first."}), 400

    try:
        db.session.delete(location)
        db.session.commit()
        return jsonify({"message": "Storage location deleted"}), 200
    except Exception as e:
        db.session.rollback()
        return jsonify({"message": "Error deleting storage location from database", "error": str(e)}), 500
