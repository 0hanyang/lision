from bio_platform.app import db
from datetime import datetime

class StorageLocation(db.Model):
    __tablename__ = 'storage_locations'

    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(100), unique=True, nullable=False)
    type = db.Column(db.String(100))
    temperature = db.Column(db.String(50))
    capacity_rows = db.Column(db.Integer)
    capacity_cols = db.Column(db.Integer)
    notes = db.Column(db.Text)
    created_at = db.Column(db.DateTime, default=datetime.utcnow)
    updated_at = db.Column(db.DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

    specimens = db.relationship('Specimen', backref='storage_location_rel', lazy='dynamic')

    def __repr__(self):
        return f'<StorageLocation {self.name}>'
