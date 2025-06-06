# Public Health Biospecimen Repository Management Platform

A Flask-based web application for managing public health biospecimen data. This platform allows for tracking donors, specimens, and their storage locations.

## Features (Phase 1)

*   Core data models for Donors, Specimens, and Storage Locations.
*   RESTful APIs for CRUD operations on these models.
*   Basic web interface for interacting with the APIs.
*   Unit tests for models and API endpoints.
*   Database migrations using Flask-Migrate.

## Project Structure

```
.
├── bio_platform/         # Main application package
│   ├── __init__.py
│   ├── app.py            # Flask app factory and core setup
│   ├── config.py         # Configuration settings (dev, test, prod)
│   ├── models/           # SQLAlchemy data models
│   │   ├── __init__.py
│   │   ├── donor.py
│   │   ├── specimen.py
│   │   └── storage.py
│   ├── routes/           # API and view blueprints/routes
│   │   ├── __init__.py
│   │   ├── donor_routes.py
│   │   ├── specimen_routes.py
│   │   ├── storage_routes.py
│   │   └── view_routes.py  # Routes for HTML pages
│   ├── schemas/          # Marshmallow schemas for validation/serialization
│   │   ├── __init__.py
│   │   ├── donor_schema.py
│   │   ├── specimen_schema.py
│   │   └── storage_schema.py
│   ├── static/           # Static files (CSS, JS, images)
│   │   ├── main.js
│   │   └── style.css
│   ├── templates/        # HTML templates
│   │   ├── base.html
│   │   ├── donors.html
│   │   ├── index.html
│   │   ├── specimens.html
│   │   └── storage.html
│   └── utils/            # Utility functions (currently empty)
├── migrations/           # Flask-Migrate database migration scripts
├── tests/                # Unit and integration tests
│   ├── __init__.py
│   ├── test_models.py
│   └── test_routes.py
├── .gitignore
├── requirements.txt      # Python package dependencies
└── README.md             # This file
```

## Setup and Installation

1.  **Clone the repository:**
    ```bash
    git clone <repository_url>
    cd <repository_name>
    ```

2.  **Create and activate a virtual environment:**
    ```bash
    python -m venv venv
    source venv/bin/activate  # On Windows: venv\Scripts\activate
    ```

3.  **Install dependencies:**
    ```bash
    pip install -r requirements.txt
    ```

4.  **Set up Environment Variables (Optional but Recommended):**
    Create a `.env` file in the root directory (it's in `.gitignore`) for environment variables:
    ```
    FLASK_APP=bio_platform.app:create_app # Tells Flask how to load the app using the factory
    # FLASK_ENV=development (Deprecated, use FLASK_DEBUG=1)
    FLASK_DEBUG=1 # Enables debug mode
    # DATABASE_URL=postgresql://user:password@host:port/dbname (Example for PostgreSQL)
    # SECRET_KEY=your_very_secret_key_here (Override default in config.py)
    ```
    If you don't use a `.env` file, you might need to export these variables in your shell:
    ```bash
    export FLASK_APP=bio_platform.app:create_app
    export FLASK_DEBUG=1
    ```
    The application uses `python-dotenv` to load `.env` files automatically if available.

5.  **Initialize the database:**
    The application uses SQLite by default (`bio_platform/app.db`).
    ```bash
    flask db upgrade
    ```
    This command applies any pending database migrations. If the `migrations` folder or database does not exist, you might need to run:
    ```bash
    # flask db init  (Only if migrations folder doesn't exist - it's already created in this project)
    # flask db migrate -m "Initial setup" (Only if no migration scripts exist - they are already created)
    ```

6.  **Run the application:**
    ```bash
    flask run
    ```
    The application will typically be available at `http://127.0.0.1:5000/`.

## Data Models

### Donor
Represents an individual providing specimens.
*   `id` (Integer, PK): Internal database ID.
*   `public_id` (String, Unique, Not Null): Externally visible unique identifier for the donor.
*   `age_range` (String): Age range of the donor (e.g., "30-40").
*   `sex` (String): Sex of the donor.
*   `consent_status` (Boolean, Not Null): Indicates if consent has been given (True/False).
*   `created_at` (DateTime): Timestamp of creation.
*   `updated_at` (DateTime): Timestamp of last update.
*   `specimens` (Relationship): One-to-many relationship with `Specimen` model.

### StorageLocation
Represents a physical or logical location where specimens are stored.
*   `id` (Integer, PK): Internal database ID.
*   `name` (String, Unique, Not Null): Unique name for the storage location (e.g., "Freezer_A1_Shelf3_Box7").
*   `type` (String): Type of storage (e.g., "-80C Freezer", "LN2 Tank").
*   `temperature` (String): Storage temperature (e.g., "-80C").
*   `capacity_rows` (Integer): Number of rows for grid-based storage.
*   `capacity_cols` (Integer): Number of columns for grid-based storage.
*   `notes` (Text): Additional notes about the location.
*   `created_at` (DateTime): Timestamp of creation.
*   `updated_at` (DateTime): Timestamp of last update.
*   `specimens` (Relationship): One-to-many relationship with `Specimen` model (dynamic query).

### Specimen
Represents a biological sample.
*   `id` (Integer, PK): Internal database ID.
*   `public_id` (String, Unique, Not Null): Externally visible unique identifier for the specimen.
*   `specimen_type` (String, Not Null): Type of specimen (e.g., "Blood", "Plasma", "Tissue").
*   `collection_date` (Date, Not Null): Date when the specimen was collected.
*   `quantity` (Float): Amount of the specimen.
*   `quantity_unit` (String): Unit for the quantity (e.g., "mL", "tubes", "slides").
*   `storage_container_id` (String): Identifier for the container holding the specimen (e.g., "Box_123").
*   `storage_position_row` (String): Row position within the container.
*   `storage_position_col` (String): Column position within the container.
*   `donor_id` (Integer, FK, Not Null): Foreign key linking to the `Donor`'s internal `id`.
*   `storage_location_id` (Integer, FK): Foreign key linking to the `StorageLocation`'s internal `id`.
*   `notes` (Text): Additional notes about the specimen.
*   `status` (String): Current status of the specimen (e.g., "Available", "Depleted", "Reserved"). Defaults to "Available".
*   `created_at` (DateTime): Timestamp of creation.
*   `updated_at` (DateTime): Timestamp of last update.
*   `donor` (Relationship): Many-to-one relationship with `Donor` model.
*   `storage_location_rel` (Relationship): Many-to-one relationship with `StorageLocation` model.


## API Endpoints

Base URL: `http://127.0.0.1:5000/api`

All request and response bodies are in JSON format.

### Donor API (`/api/donors`)

*   **POST `/api/donors`**: Add a new donor.
    *   Request Body:
        ```json
        {
            "public_id": "DONOR001", // Optional, auto-generated if omitted
            "age_range": "30-39",
            "sex": "Female",
            "consent_status": true // Required
        }
        ```
    *   Response (201 Created): Donor object or (400 Bad Request / 409 Conflict / 500 Server Error)
        ```json
        {
            "id": 1,
            "public_id": "DONOR001",
            "age_range": "30-39",
            "sex": "Female",
            "consent_status": true,
            "created_at": "YYYY-MM-DDTHH:MM:SS",
            "updated_at": "YYYY-MM-DDTHH:MM:SS"
        }
        ```
*   **GET `/api/donors`**: Get a list of all donors.
    *   Response (200 OK): Array of donor objects.
*   **GET `/api/donors/<public_id>`**: Get a specific donor by their public ID.
    *   Response (200 OK): Donor object or (404 Not Found).
*   **PUT `/api/donors/<public_id>`**: Update an existing donor.
    *   Request Body: Fields to update (e.g., `{"age_range": "40-49"}`).
    *   Response (200 OK): Updated donor object or (400 / 404 / 500).
*   **DELETE `/api/donors/<public_id>`**: Delete a donor.
    *   Response (200 OK): `{"message": "Donor deleted"}` or (400 / 404 / 500).
    *   Note: Cannot delete a donor if they have associated specimens.

### Storage Location API (`/api/storage-locations`)

*   **POST `/api/storage-locations`**: Add a new storage location.
    *   Request Body: e.g., `{"name": "Freezer_B2", "type": "-80C Freezer", "temperature": "-80"}`
    *   Response (201 Created): StorageLocation object.
*   **GET `/api/storage-locations`**: Get all storage locations.
*   **GET `/api/storage-locations/<location_id>`**: Get a specific storage location by its internal ID.
*   **PUT `/api/storage-locations/<location_id>`**: Update a storage location.
*   **DELETE `/api/storage-locations/<location_id>`**: Delete a storage location (fails if specimens are present).

### Specimen API (`/api/specimens`)

*   **POST `/api/specimens`**: Add a new specimen.
    *   Request Body:
        ```json
        {
            "public_id": "SPEC001", // Optional
            "specimen_type": "Plasma",
            "collection_date": "YYYY-MM-DD", // Required
            "quantity": 5.5,
            "quantity_unit": "mL",
            "donor_id": 1, // Required (internal DB ID of an existing donor)
            "storage_location_id": 1 // Optional (internal DB ID of an existing storage location)
        }
        ```
    *   Response (201 Created): Specimen object.
*   **GET `/api/specimens`**: Get all specimens.
*   **GET `/api/specimens/<public_id>`**: Get a specific specimen by its public ID.
*   **PUT `/api/specimens/<public_id>`**: Update a specimen.
*   **DELETE `/api/specimens/<public_id>`**: Delete a specimen.

## Running Tests

To run the unit tests:

1.  Ensure you are in the project root directory with your virtual environment activated.
2.  Make sure `FLASK_APP` is set (e.g., `export FLASK_APP=bio_platform.app:create_app`).
3.  Run the tests using unittest discovery:
    ```bash
    python -m unittest discover -s tests -p "test_*.py" -v
    ```

## Future Enhancements (Beyond Phase 1)

*   User authentication and authorization.
*   Advanced search and filtering capabilities.
*   Specimen request and approval workflows.
*   Audit trails for tracking changes.
*   Reporting and analytics.
*   More robust UI/UX.
*   Integration with other laboratory systems.
```
