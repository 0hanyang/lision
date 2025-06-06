import unittest
import json
from datetime import date # Added for specimen date
from bio_platform.app import create_app, db
from bio_platform.models import Donor, Specimen # Import Specimen
from bio_platform.config import TestingConfig

class APITestCase(unittest.TestCase):
    def setUp(self):
        self.app = create_app(TestingConfig)
        self.app_context = self.app.app_context()
        self.app_context.push()
        self.client = self.app.test_client()
        db.create_all()

        self.donor1_public_id = "donor1_routes" # Use a distinct public_id
        self.donor1 = Donor(public_id=self.donor1_public_id, age_range="20-30", sex="Male", consent_status=True)
        db.session.add(self.donor1)
        db.session.commit()

    def tearDown(self):
        db.session.remove()
        db.drop_all()
        self.app_context.pop()

    def test_add_donor(self):
        payload = {
            "public_id": "donor2_routes",
            "age_range": "40-50",
            "sex": "Female",
            "consent_status": False # This is a required field
        }
        response = self.client.post('/api/donors',
                                    data=json.dumps(payload),
                                    content_type='application/json')
        self.assertEqual(response.status_code, 201, msg=response.get_data(as_text=True))
        json_response = response.get_json()
        self.assertEqual(json_response['public_id'], 'donor2_routes')
        self.assertTrue(Donor.query.filter_by(public_id="donor2_routes").count() == 1)

    def test_add_donor_autogenerate_public_id(self):
        payload = {
            "age_range": "60-70",
            "sex": "Other",
            "consent_status": True
        }
        response = self.client.post('/api/donors',
                                    data=json.dumps(payload),
                                    content_type='application/json')
        self.assertEqual(response.status_code, 201, msg=response.get_data(as_text=True))
        json_response = response.get_json()
        self.assertIsNotNone(json_response['public_id'])
        self.assertTrue(Donor.query.filter_by(public_id=json_response['public_id']).count() == 1)


    def test_add_donor_missing_required_field(self):
        # consent_status is required by schema
        response = self.client.post('/api/donors',
                                    data=json.dumps({"public_id": "donor3_routes", "age_range": "40-50"}),
                                    content_type='application/json')
        self.assertEqual(response.status_code, 400, msg=response.get_data(as_text=True))
        json_response = response.get_json()
        self.assertIn('errors', json_response)
        self.assertIn('consent_status', json_response['errors'])

    def test_add_donor_duplicate_public_id(self):
        payload = {
            "public_id": self.donor1_public_id, # Duplicate public_id
            "age_range": "40-50",
            "sex": "Female",
            "consent_status": False
        }
        response = self.client.post('/api/donors',
                                    data=json.dumps(payload),
                                    content_type='application/json')
        self.assertEqual(response.status_code, 409, "Should return 409 for duplicate public_id")


    def test_get_all_donors(self):
        # Add another donor to have more than one
        db.session.add(Donor(public_id="donor_temp_list", consent_status=True))
        db.session.commit()

        response = self.client.get('/api/donors')
        self.assertEqual(response.status_code, 200)
        json_response = response.get_json()
        self.assertIsInstance(json_response, list)
        self.assertEqual(len(json_response), 2)
        public_ids = [d['public_id'] for d in json_response]
        self.assertIn(self.donor1_public_id, public_ids)
        self.assertIn("donor_temp_list", public_ids)

    def test_get_specific_donor(self):
        response = self.client.get(f'/api/donors/{self.donor1_public_id}')
        self.assertEqual(response.status_code, 200)
        json_response = response.get_json()
        self.assertEqual(json_response['public_id'], self.donor1_public_id)

    def test_get_nonexistent_donor(self):
        response = self.client.get('/api/donors/donor_not_exist_routes')
        self.assertEqual(response.status_code, 404)

    def test_update_donor(self):
        update_data = {"age_range": "25-35", "sex": "Other"}
        response = self.client.put(f'/api/donors/{self.donor1_public_id}',
                                   data=json.dumps(update_data),
                                   content_type='application/json')
        self.assertEqual(response.status_code, 200, msg=response.get_data(as_text=True))
        json_response = response.get_json()
        self.assertEqual(json_response['age_range'], "25-35")

        updated_donor = db.session.get(Donor, self.donor1.id) # Use db.session.get for PK lookup
        self.assertEqual(updated_donor.age_range, "25-35")

    def test_update_donor_invalid_field_value(self):
        update_data = {"consent_status": "not-a-boolean"}
        response = self.client.put(f'/api/donors/{self.donor1_public_id}',
                                   data=json.dumps(update_data),
                                   content_type='application/json')
        self.assertEqual(response.status_code, 400)
        json_response = response.get_json()
        self.assertIn('errors', json_response)
        self.assertIn('consent_status', json_response['errors'])


    def test_delete_donor_success(self):
        donor_to_delete = Donor(public_id="donor_del_routes", consent_status=True)
        db.session.add(donor_to_delete)
        db.session.commit()

        response = self.client.delete(f'/api/donors/{donor_to_delete.public_id}')
        self.assertEqual(response.status_code, 200, msg=response.get_data(as_text=True))
        self.assertIsNone(Donor.query.filter_by(public_id=donor_to_delete.public_id).first())

    def test_delete_donor_with_specimens(self):
        # Create a specimen associated with donor1
        spec = Specimen(public_id="spec_del_test", specimen_type="Blood",
                        collection_date=date(2023,1,1), donor_id=self.donor1.id)
        db.session.add(spec)
        db.session.commit()

        response = self.client.delete(f'/api/donors/{self.donor1_public_id}')
        self.assertEqual(response.status_code, 400, "Should not delete donor with specimens")
        json_response = response.get_json()
        self.assertIn("Cannot delete donor with associated specimens", json_response['message'])
        self.assertIsNotNone(Donor.query.filter_by(public_id=self.donor1_public_id).first())


    def test_delete_nonexistent_donor(self):
        response = self.client.delete('/api/donors/donor_not_exist_del_routes')
        self.assertEqual(response.status_code, 404)

# if __name__ == '__main__': # Not needed for test discovery
#    unittest.main()
