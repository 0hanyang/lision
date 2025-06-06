from .donor import Donor
from .specimen import Specimen
from .storage import StorageLocation

# This makes it easier to import models, e.g., from bio_platform.models import Donor
__all__ = ['Donor', 'Specimen', 'StorageLocation']
