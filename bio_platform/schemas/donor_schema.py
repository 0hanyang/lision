from marshmallow import Schema, fields, validate

class DonorSchema(Schema):
    id = fields.Int(dump_only=True) # Internal DB ID
    public_id = fields.Str(required=True, validate=validate.Length(min=1)) # External unique ID
    age_range = fields.Str(allow_none=True)
    sex = fields.Str(allow_none=True)
    consent_status = fields.Bool(required=True)
    created_at = fields.DateTime(dump_only=True, format='%Y-%m-%dT%H:%M:%S')
    updated_at = fields.DateTime(dump_only=True, format='%Y-%m-%dT%H:%M:%S')

    class Meta:
        # strict = True # Deprecated in Marshmallow 3
        unknown = 'RAISE' # Behave like strict=True for Marshmallow 3+
