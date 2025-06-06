from bio_platform.app import db
from datetime import datetime

class Specimen(db.Model):
    __tablename__ = 'specimens'

    id = db.Column(db.Integer, primary_key=True)
    public_id = db.Column(db.String(100), unique=True, nullable=False, index=True)
    specimen_type = db.Column(db.String(100), nullable=False)
    collection_date = db.Column(db.DateTime, nullable=False)
    quantity = db.Column(db.Float)
    quantity_unit = db.Column(db.String(50))

    storage_container_id = db.Column(db.String(100))
    storage_position_row = db.Column(db.String(10))
    storage_position_col = db.Column(db.String(10))

    donor_id = db.Column(db.Integer, db.ForeignKey('donors.id'), nullable=False)
    storage_location_id = db.Column(db.Integer, db.ForeignKey('storage_locations.id'), nullable=True)

    notes = db.Column(db.Text)
    created_at = db.Column(db.DateTime, default=datetime.utcnow)
    updated_at = db.Column(db.DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)
    status = db.Column(db.String(50), default="Available")

    def __repr__(self):
        return f'<Specimen {self.public_id}>'
