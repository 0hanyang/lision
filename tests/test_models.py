import unittest
from datetime import datetime, date # Corrected: import date from datetime
from bio_platform.app import create_app, db
from bio_platform.models import Donor, Specimen, StorageLocation
from bio_platform.config import TestingConfig # Corrected import

class ModelTestCase(unittest.TestCase):
    def setUp(self):
        self.app = create_app(TestingConfig)
        self.app_context = self.app.app_context()
        self.app_context.push()
        db.create_all()

    def tearDown(self):
        db.session.remove()
        db.drop_all()
        self.app_context.pop()

    def test_donor_creation(self):
        d = Donor(public_id="donor001", age_range="30-40", sex="Female", consent_status=True)
        db.session.add(d)
        db.session.commit()
        self.assertEqual(Donor.query.count(), 1)
        queried_donor = Donor.query.first()
        self.assertEqual(queried_donor.public_id, "donor001")
        self.assertTrue(queried_donor.created_at <= datetime.utcnow())

    def test_storage_location_creation(self):
        s = StorageLocation(name="Freezer1", type="-80C Freezer", temperature="-80C")
        db.session.add(s)
        db.session.commit()
        self.assertEqual(StorageLocation.query.count(), 1)
        queried_sl = StorageLocation.query.first()
        self.assertEqual(queried_sl.name, "Freezer1")

    def test_specimen_creation_and_relationships(self):
        d = Donor(public_id="donor002", consent_status=True)
        sl = StorageLocation(name="BoxA")
        db.session.add_all([d, sl])
        db.session.commit() # d and sl now have IDs

        spec = Specimen(
            public_id="spec001",
            specimen_type="Blood",
            collection_date=date(2023, 1, 1), # Use date object
            donor_id=d.id,
            storage_location_id=sl.id,
            quantity=10.5,
            quantity_unit="ml"
        )
        db.session.add(spec)
        db.session.commit()

        self.assertEqual(Specimen.query.count(), 1)
        queried_spec = Specimen.query.first()
        self.assertEqual(queried_spec.donor_id, d.id)
        self.assertEqual(queried_spec.storage_location_id, sl.id)
        # Test relationships directly
        self.assertEqual(queried_spec.donor, d)
        self.assertEqual(queried_spec.storage_location_rel, sl)
        self.assertIn(queried_spec, d.specimens)
        # For dynamic relationship on StorageLocation, specimens is a query
        self.assertIn(queried_spec, sl.specimens) # or sl.specimens.all() if it was a list
        self.assertEqual(queried_spec.status, "Available")

# if __name__ == '__main__': # Not needed for test discovery
#     unittest.main()
