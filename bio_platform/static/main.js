document.addEventListener('DOMContentLoaded', function() {
    function displayResponse(elementId, message, isSuccess) {
        const responseEl = document.getElementById(elementId);
        if (!responseEl) return;
        responseEl.innerHTML = message; // Use innerHTML to allow basic formatting if needed
        responseEl.className = isSuccess ? 'success' : 'error';
    }

    // Donor Management
    const addDonorForm = document.getElementById('addDonorForm');
    if (addDonorForm) {
        addDonorForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const formData = new FormData(addDonorForm);
            const data = Object.fromEntries(formData.entries());
            data.consent_status = formData.get('consent_status') === 'true';

            fetch('/api/donors', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(data)
            })
            .then(response => response.json().then(resData => ({ok: response.ok, status: response.status, body: resData })))
            .then(result => {
                if (result.ok) {
                    displayResponse('donorResponse', `Donor added/updated. Public ID: <strong>${result.body.public_id}</strong> (Internal DB ID: ${result.body.id})`, true);
                    addDonorForm.reset();
                    loadDonors();
                } else {
                    const errorMsg = result.body.message || (result.body.errors ? JSON.stringify(result.body.errors) : JSON.stringify(result.body));
                    displayResponse('donorResponse', `Error (${result.status}): ${errorMsg}`, false);
                }
            })
            .catch(error => displayResponse('donorResponse', `Request failed: ${error}`, false));
        });
    }

    const loadDonorsBtn = document.getElementById('loadDonorsBtn');
    if (loadDonorsBtn) {
        loadDonorsBtn.addEventListener('click', loadDonors);
    }

    function loadDonors() {
        fetch('/api/donors')
        .then(response => response.json())
        .then(data => {
            const tableBody = document.querySelector('#donorsTable tbody');
            if (!tableBody) return;
            tableBody.innerHTML = '';
            data.forEach(donor => {
                const row = tableBody.insertRow();
                row.insertCell().textContent = donor.public_id;
                row.insertCell().textContent = donor.age_range || 'N/A';
                row.insertCell().textContent = donor.sex || 'N/A';
                row.insertCell().textContent = donor.consent_status ? 'Yes' : 'No';
                row.insertCell().textContent = new Date(donor.created_at).toLocaleString();
                 // Display internal ID for specimen form usage
                const idCell = row.insertCell();
                idCell.textContent = donor.id;
                idCell.title = "Internal DB ID";
            });
        })
        .catch(error => console.error('Error loading donors:', error));
    }

    // Specimen Management
    const addSpecimenForm = document.getElementById('addSpecimenForm');
    if (addSpecimenForm) {
        addSpecimenForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const formData = new FormData(addSpecimenForm);
            const data = Object.fromEntries(formData.entries());
            if(data.quantity) data.quantity = parseFloat(data.quantity);
            if(data.donor_id) data.donor_id = parseInt(data.donor_id);
            if(data.storage_location_id) data.storage_location_id = parseInt(data.storage_location_id);
            else delete data.storage_location_id; // Remove if empty, as API expects int or null

            fetch('/api/specimens', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(data)
            })
            .then(response => response.json().then(resData => ({ok: response.ok, status: response.status, body: resData })))
            .then(result => {
                if (result.ok) {
                    displayResponse('specimenResponse', `Specimen added: ${result.body.public_id}`, true);
                    addSpecimenForm.reset();
                    loadSpecimens();
                } else {
                    const errorMsg = result.body.message || (result.body.errors ? JSON.stringify(result.body.errors) : JSON.stringify(result.body));
                    displayResponse('specimenResponse', `Error (${result.status}): ${errorMsg}`, false);
                }
            })
            .catch(error => displayResponse('specimenResponse', `Request failed: ${error}`, false));
        });
    }

    const loadSpecimensBtn = document.getElementById('loadSpecimensBtn');
    if (loadSpecimensBtn) {
        loadSpecimensBtn.addEventListener('click', loadSpecimens);
    }

    function loadSpecimens() {
        fetch('/api/specimens')
        .then(response => response.json())
        .then(data => {
            const tableBody = document.querySelector('#specimensTable tbody');
            if(!tableBody) return;
            tableBody.innerHTML = '';
            data.forEach(specimen => {
                const row = tableBody.insertRow();
                row.insertCell().textContent = specimen.public_id;
                row.insertCell().textContent = specimen.specimen_type;
                row.insertCell().textContent = specimen.collection_date;
                row.insertCell().textContent = specimen.quantity !== null ? specimen.quantity : 'N/A';
                row.insertCell().textContent = specimen.quantity_unit || 'N/A';
                row.insertCell().textContent = specimen.donor_public_id || 'N/A';
                row.insertCell().textContent = specimen.storage_location_name || 'N/A';
                row.insertCell().textContent = specimen.status || 'N/A';
            });
        })
        .catch(error => console.error('Error loading specimens:', error));
    }

    // Storage Management
    const addStorageForm = document.getElementById('addStorageForm');
    if (addStorageForm) {
        addStorageForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const formData = new FormData(addStorageForm);
            const data = Object.fromEntries(formData.entries());

            fetch('/api/storage-locations', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(data)
            })
            .then(response => response.json().then(resData => ({ok: response.ok, status: response.status, body: resData })))
            .then(result => {
                if (result.ok) {
                    displayResponse('storageResponse', `Storage Location added: ${result.body.name} (Internal DB ID: ${result.body.id})`, true);
                    addStorageForm.reset();
                    loadStorageLocations();
                } else {
                    const errorMsg = result.body.message || (result.body.errors ? JSON.stringify(result.body.errors) : JSON.stringify(result.body));
                    displayResponse('storageResponse', `Error (${result.status}): ${errorMsg}`, false);
                }
            })
            .catch(error => displayResponse('storageResponse', `Request failed: ${error}`, false));
        });
    }

    const loadStorageBtn = document.getElementById('loadStorageBtn');
    if (loadStorageBtn) {
        loadStorageBtn.addEventListener('click', loadStorageLocations);
    }

    function loadStorageLocations() {
        fetch('/api/storage-locations')
        .then(response => response.json())
        .then(data => {
            const tableBody = document.querySelector('#storageTable tbody');
            if(!tableBody) return;
            tableBody.innerHTML = '';
            data.forEach(loc => {
                const row = tableBody.insertRow();
                row.insertCell().textContent = loc.id; // Display internal ID for specimen form usage
                row.insertCell().textContent = loc.name;
                row.insertCell().textContent = loc.type || 'N/A';
                row.insertCell().textContent = loc.temperature || 'N/A';
            });
        })
        .catch(error => console.error('Error loading storage locations:', error));
    }

    if(document.getElementById('donorsTable')) loadDonors();
    if(document.getElementById('specimensTable')) loadSpecimens();
    if(document.getElementById('storageTable')) loadStorageLocations();
});
