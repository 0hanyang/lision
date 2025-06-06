import os
basedir = os.path.abspath(os.path.dirname(__file__))

class Config:
    SECRET_KEY = os.environ.get('SECRET_KEY') or 'your_secret_key_for_development' # Added a default for dev
    SQLALCHEMY_DATABASE_URI = os.environ.get('DATABASE_URL') or \
        'sqlite:///' + os.path.join(basedir, 'app.db')
    SQLALCHEMY_TRACK_MODIFICATIONS = False

class TestingConfig(Config):
    TESTING = True
    # Use in-memory SQLite for tests for speed, though file-based can be easier to inspect
    SQLALCHEMY_DATABASE_URI = 'sqlite:///:memory:'
    # SQLALCHEMY_DATABASE_URI = 'sqlite:///' + os.path.join(basedir, 'test_app.db') # Alternative
    WTF_CSRF_ENABLED = False # Disable CSRF forms for testing if using Flask-WTF (not used yet, but good practice)
    # Ensure public_id generation is deterministic for tests if relying on specific UUIDs, or mock it.
    # For now, we rely on API to generate or we provide it.
