from marshmallow import Schema, fields, validate, ValidationError
from datetime import date # Corrected from datetime.date

# Custom validator for date
def must_not_be_in_future(data_value): # Validator takes the value directly
    if data_value and data_value > date.today(): # Check if data_value is not None
        raise ValidationError("Collection date cannot be in the future.")

class SpecimenSchema(Schema):
    id = fields.Int(dump_only=True)
    public_id = fields.Str(required=True, validate=validate.Length(min=1))
    specimen_type = fields.Str(required=True)
    # Using Date field for collection_date
    collection_date = fields.Date(required=True, validate=must_not_be_in_future, format='%Y-%m-%d')
    quantity = fields.Float(allow_none=True)
    quantity_unit = fields.Str(allow_none=True)

    storage_container_id = fields.Str(allow_none=True)
    storage_position_row = fields.Str(allow_none=True)
    storage_position_col = fields.Str(allow_none=True)

    donor_id = fields.Int(required=True, load_only=True) # Use this for input linking
    storage_location_id = fields.Int(allow_none=True, load_only=True) # Use this for input linking

    notes = fields.Str(allow_none=True)
    # dump_default for output when field is missing; load_default for input if field is missing
    status = fields.Str(dump_default="Available", load_default="Available")

    created_at = fields.DateTime(dump_only=True, format='%Y-%m-%dT%H:%M:%S')
    updated_at = fields.DateTime(dump_only=True, format='%Y-%m-%dT%H:%M:%S')

    # For displaying related data (read-only during serialization)
    donor_public_id = fields.String(dump_only=True, attribute="donor.public_id")
    storage_location_name = fields.String(dump_only=True, attribute="storage_location_rel.name")

    class Meta:
        unknown = 'RAISE'
