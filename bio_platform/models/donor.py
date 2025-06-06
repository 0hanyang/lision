from bio_platform.app import db
from datetime import datetime

class Donor(db.Model):
    __tablename__ = 'donors'

    id = db.Column(db.Integer, primary_key=True)
    public_id = db.Column(db.String(100), unique=True, nullable=False, index=True)
    age_range = db.Column(db.String(50))
    sex = db.Column(db.String(50))
    consent_status = db.Column(db.Boolean, nullable=False, default=False)
    created_at = db.Column(db.DateTime, default=datetime.utcnow)
    updated_at = db.Column(db.DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

    specimens = db.relationship('Specimen', backref='donor', lazy=True)

    def __repr__(self):
        return f'<Donor {self.public_id}>'
