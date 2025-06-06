from flask import Blueprint, render_template

# Ensure template_folder points to the global 'templates' directory
# and static_folder to global 'static'
view_bp = Blueprint('view_bp', __name__,
                    template_folder='../templates',
                    static_folder='../static',
                    static_url_path='/static/view_bp') # Differentiate static paths if needed

@view_bp.route('/')
def index():
    return render_template('index.html')

@view_bp.route('/donors')
def manage_donors():
    return render_template('donors.html')

@view_bp.route('/specimens')
def manage_specimens():
    return render_template('specimens.html')

@view_bp.route('/storage')
def manage_storage():
    return render_template('storage.html')
