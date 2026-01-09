/**
 * Aviation Management System - Main JavaScript
 * Gestion des fonctionnalités communes : navigation, modals, filtres, CRUD
 */

document.addEventListener('DOMContentLoaded', function() {
    initSidebar();
    initModals();
    initToasts();
    initFilters();
    initTableActions();
});

/* ===== SIDEBAR NAVIGATION ===== */
function initSidebar() {
    // Toggle mobile sidebar
    const menuToggle = document.querySelector('.menu-toggle');
    const sidebar = document.querySelector('.sidebar');
    
    if (menuToggle) {
        menuToggle.addEventListener('click', () => {
            sidebar.classList.toggle('open');
        });
    }

    // Close sidebar when clicking outside on mobile
    document.addEventListener('click', (e) => {
        if (window.innerWidth <= 1024 && 
            !e.target.closest('.sidebar') && 
            !e.target.closest('.menu-toggle')) {
            sidebar.classList.remove('open');
        }
    });

    // Submenu toggle with smooth animation
    const submenuParents = document.querySelectorAll('.nav-item.has-submenu');
    submenuParents.forEach(parent => {
        const link = parent.querySelector('.nav-link');
        const submenu = parent.querySelector('.submenu');

        // initialize max-height based on open state
        if (submenu) {
            if (parent.classList.contains('open')) {
                submenu.style.maxHeight = submenu.scrollHeight + 'px';
            } else {
                submenu.style.maxHeight = '0px';
            }
        }

        if (link) {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                parent.classList.toggle('open');
                // set maxHeight to animate
                if (submenu) {
                    if (parent.classList.contains('open')) {
                        submenu.style.maxHeight = submenu.scrollHeight + 'px';
                    } else {
                        submenu.style.maxHeight = '0px';
                    }
                }
            });
        }
    });

    // Active link highlight
    const currentPath = window.location.pathname;
    document.querySelectorAll('.nav-link').forEach(link => {
        if (link.getAttribute('href') === currentPath) {
            link.classList.add('active');
            // Open parent submenu if exists
            const parentSubmenu = link.closest('.has-submenu');
            if (parentSubmenu) {
                parentSubmenu.classList.add('open');
            }
        }
    });
}

/* ===== MODAL MANAGEMENT ===== */
function initModals() {
    // Open modal buttons
    document.querySelectorAll('[data-modal-open]').forEach(btn => {
        btn.addEventListener('click', () => {
            const modalId = btn.getAttribute('data-modal-open');
            openModal(modalId);
        });
    });

    // Close modal buttons
    document.querySelectorAll('[data-modal-close]').forEach(btn => {
        btn.addEventListener('click', () => {
            closeModal(btn.closest('.modal-overlay'));
        });
    });

    // Close on overlay click
    document.querySelectorAll('.modal-overlay').forEach(overlay => {
        overlay.addEventListener('click', (e) => {
            if (e.target === overlay) {
                closeModal(overlay);
            }
        });
    });

    // Close on ESC key
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            const activeModal = document.querySelector('.modal-overlay.active');
            if (activeModal) {
                closeModal(activeModal);
            }
        }
    });
}

function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('active');
        document.body.style.overflow = 'hidden';
    }
}

function closeModal(modal) {
    if (typeof modal === 'string') {
        modal = document.getElementById(modal);
    }
    if (modal) {
        modal.classList.remove('active');
        document.body.style.overflow = '';
        // Reset form if exists
        const form = modal.querySelector('form');
        if (form) form.reset();
    }
}

/* ===== TOAST NOTIFICATIONS ===== */
let toastContainer;

function initToasts() {
    toastContainer = document.querySelector('.toast-container');
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.className = 'toast-container';
        document.body.appendChild(toastContainer);
    }
}

function showToast(message, type = 'success', duration = 3000) {
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    
    const icons = {
        success: 'fa-check-circle',
        error: 'fa-times-circle',
        warning: 'fa-exclamation-triangle',
        info: 'fa-info-circle'
    };

    toast.innerHTML = `
        <i class="fas ${icons[type]}"></i>
        <span>${message}</span>
    `;

    toastContainer.appendChild(toast);

    setTimeout(() => {
        toast.style.animation = 'toastSlide 0.3s ease reverse';
        setTimeout(() => toast.remove(), 300);
    }, duration);
}

/* ===== MULTI-CRITERIA FILTER ===== */
function initFilters() {
    const filterForm = document.getElementById('filterForm');
    if (!filterForm) return;

    const filterInputs = filterForm.querySelectorAll('input, select');
    
    // Apply filters on change
    filterInputs.forEach(input => {
        input.addEventListener('change', applyFilters);
        if (input.type === 'text' || input.type === 'search') {
            input.addEventListener('keyup', debounce(applyFilters, 300));
        }
    });

    // Reset filters button
    const resetBtn = document.getElementById('resetFilters');
    if (resetBtn) {
        resetBtn.addEventListener('click', () => {
            filterForm.reset();
            applyFilters();
        });
    }
}

function applyFilters() {
    const filterForm = document.getElementById('filterForm');
    if (!filterForm) return;

    const formData = new FormData(filterForm);
    const filters = {};
    
    formData.forEach((value, key) => {
        if (value.trim() !== '') {
            filters[key] = value.toLowerCase().trim();
        }
    });

    const table = document.querySelector('.data-table tbody');
    if (!table) return;

    const rows = table.querySelectorAll('tr');
    let visibleCount = 0;

    rows.forEach(row => {
        let visible = true;

        for (const [key, value] of Object.entries(filters)) {
            const cell = row.querySelector(`[data-filter="${key}"]`);
            if (cell) {
                const cellValue = cell.textContent.toLowerCase().trim();
                if (!cellValue.includes(value)) {
                    visible = false;
                    break;
                }
            }
        }

        row.style.display = visible ? '' : 'none';
        if (visible) visibleCount++;
    });

    // Update count display
    const countDisplay = document.getElementById('resultCount');
    if (countDisplay) {
        countDisplay.textContent = `${visibleCount} résultat(s)`;
    }
}

/* ===== TABLE ACTIONS ===== */
function initTableActions() {
    // Edit buttons
    document.querySelectorAll('[data-action="edit"]').forEach(btn => {
        btn.addEventListener('click', () => {
            const id = btn.getAttribute('data-id');
            handleEdit(id);
        });
    });

    // Delete buttons
    document.querySelectorAll('[data-action="delete"]').forEach(btn => {
        btn.addEventListener('click', () => {
            const id = btn.getAttribute('data-id');
            handleDelete(id);
        });
    });

    // View buttons
    document.querySelectorAll('[data-action="view"]').forEach(btn => {
        btn.addEventListener('click', () => {
            const id = btn.getAttribute('data-id');
            handleView(id);
        });
    });
}

function handleEdit(id) {
    // Navigate to server-side edit page which will open the modal with prefilled data
    window.location.href = `/${getCurrentEntity()}/edit/${id}`;
}

function handleDelete(id) {
    if (!confirm('Êtes-vous sûr de vouloir supprimer cet élément ?')) return;

    // Create and submit a POST form to server-side delete endpoint
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = `/${getCurrentEntity()}/delete/${id}`;

    // CSRF token support (if present on page as meta)
    const csrf = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
    if (csrf) {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = '_csrf';
        input.value = csrf;
        form.appendChild(input);
    }

    document.body.appendChild(form);
    form.submit();
}

function handleView(id) {
    window.location.href = `/${getCurrentEntity()}/${id}`;
}

/* ===== FORM HANDLING ===== */
function handleFormSubmit(formId, endpoint) {
    // If endpoint is not an API endpoint we won't intercept the submit and let the browser do a normal POST
    if (!endpoint || !endpoint.startsWith('/api')) return;

    const form = document.getElementById(formId);
    if (!form) return;

    form.addEventListener('submit', (e) => {
        e.preventDefault();

        if (!validateForm(form)) return;

        const formData = new FormData(form);
        const data = Object.fromEntries(formData);
        const id = data.id;
        const method = id ? 'PUT' : 'POST';
        const url = id ? `${endpoint}/${id}` : endpoint;

        fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
        .then(response => {
            if (response.ok) {
                showToast(id ? 'Modification réussie' : 'Création réussie', 'success');
                closeModal('formModal');
                setTimeout(() => location.reload(), 1000);
            } else {
                return response.json().then(err => {
                    throw new Error(err.message || 'Erreur serveur');
                });
            }
        })
        .catch(err => showToast(err.message, 'error'));
    });
}

function validateForm(form) {
    let valid = true;
    const requiredFields = form.querySelectorAll('[required]');

    // Clear previous errors
    form.querySelectorAll('.form-error').forEach(el => el.remove());
    form.querySelectorAll('.form-control.error').forEach(el => el.classList.remove('error'));

    requiredFields.forEach(field => {
        if (!field.value.trim()) {
            valid = false;
            field.classList.add('error');
            const error = document.createElement('span');
            error.className = 'form-error';
            error.textContent = 'Ce champ est requis';
            field.parentNode.appendChild(error);
        }
    });

    return valid;
}

function populateForm(data) {
    const form = document.getElementById('entityForm');
    if (!form) return;

    for (const [key, value] of Object.entries(data)) {
        const field = form.querySelector(`[name="${key}"]`);
        if (field) {
            field.value = value;
        }
    }
}

function resetForm() {
    const form = document.getElementById('entityForm');
    if (form) {
        form.reset();
        form.querySelector('[name="id"]')?.setAttribute('value', '');
    }
}

/* ===== UTILITY FUNCTIONS ===== */
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

function getCurrentEntity() {
    // Extract entity name from current URL
    const path = window.location.pathname;
    const parts = path.split('/').filter(p => p);
    return parts[0] || 'unknown';
}

function formatDate(dateString) {
    const options = { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' };
    return new Date(dateString).toLocaleDateString('fr-FR', options);
}

function formatNumber(number) {
    return new Intl.NumberFormat('fr-FR').format(number);
}

/* ===== EXPORT FOR GLOBAL USE ===== */
window.AviationApp = {
    openModal,
    closeModal,
    showToast,
    applyFilters,
    handleFormSubmit,
    validateForm,
    resetForm,
    formatDate,
    formatNumber
};
