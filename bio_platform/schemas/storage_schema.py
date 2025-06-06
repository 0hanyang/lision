from marshmallow import Schema, fields, validate

class StorageLocationSchema(Schema):
    id = fields.Int(dump_only=True)
    name = fields.Str(required=True, validate=validate.Length(min=1))
    type = fields.Str(allow_none=True)
    temperature = fields.Str(allow_none=True)
    capacity_rows = fields.Int(allow_none=True)
    capacity_cols = fields.Int(allow_none=True)
    notes = fields.Str(allow_none=True)
    created_at = fields.DateTime(dump_only=True, format='%Y-%m-%dT%H:%M:%S')
    updated_at = fields.DateTime(dump_only=True, format='%Y-%m-%dT%H:%M:%S')

    class Meta:
        unknown = 'RAISE'
