from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_migrate import Migrate
import os

db = SQLAlchemy()
migrate = Migrate()

def create_app(config_object='bio_platform.config.Config'):
    app = Flask(__name__, static_folder='static') # Define global static folder
    app.config.from_object(config_object)

    db.init_app(app)
    migrate.init_app(app, db)

    with app.app_context():
        from bio_platform.models import Donor, Specimen, StorageLocation # noqa

    from bio_platform.routes.donor_routes import donor_bp
    app.register_blueprint(donor_bp, url_prefix='/api/donors')

    from bio_platform.routes.storage_routes import storage_bp
    app.register_blueprint(storage_bp, url_prefix='/api/storage-locations')

    from bio_platform.routes.specimen_routes import specimen_bp
    app.register_blueprint(specimen_bp, url_prefix='/api/specimens')

    from bio_platform.routes.view_routes import view_bp
    # The static_folder for the app itself is 'static'.
    # The view_bp blueprint is told that its templates are in '../templates'
    # and its static files are in '../static'.
    # Flask searches for templates in app's template_folder then blueprint's.
    # For static files, app's static_url_path is /static by default.
    # Blueprint static files are typically /blueprint_name.static_url_path
    # If view_bp.static_folder='../static' and app.static_folder='static',
    # ensure url_for('static', filename='style.css') in base.html resolves correctly.
    # It will look for 'static/style.css' relative to the app root.
    app.register_blueprint(view_bp)

    return app
