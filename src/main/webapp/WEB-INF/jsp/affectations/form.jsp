<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title"><i class="fas fa-plus-circle"></i> Nouvelle Affectation</h3>
        <a href="${pageContext.request.contextPath}/affectations" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Retour
        </a>
    </div>
    <div class="card-body">
        <!-- Messages d'erreur et succès -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-triangle"></i>
                <pre style="white-space: pre-wrap; margin: 0; font-family: inherit;">${errorMessage}</pre>
            </div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i> ${successMessage}
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/affectations/save" class="affectation-form">
            <div class="form-section">
                <h4><i class="fas fa-plane"></i> Sélection du Vol</h4>
                <div class="form-group">
                    <label for="volInstanceId">Vol Instance <span class="required">*</span></label>
                    <select id="volInstanceId" name="volInstanceId" class="form-control" required>
                        <option value="">-- Sélectionner un vol --</option>
                        <c:forEach var="vi" items="${volInstances}">
                            <option value="${vi.id}">
                                Vol #${vi.id} - ${vi.vol.aeroportOrigine.nomAeroport} → ${vi.vol.aeroportDestination.nomAeroport}
                                | Départ: ${vi.dateDepart.toLocalDate()} ${vi.dateDepart.toLocalTime()}
                                | Arrivée: ${vi.dateArrivee.toLocalDate()} ${vi.dateArrivee.toLocalTime()}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="form-section">
                <h4><i class="fas fa-user"></i> Sélection du Membre d'Équipage</h4>
                
                <!-- Filtre par type de membre -->
                <div class="form-group">
                    <label for="filterType">Filtrer par type</label>
                    <select id="filterType" class="form-control" onchange="filterMembres()">
                        <option value="">-- Tous les types --</option>
                        <c:forEach var="t" items="${typesMembre}">
                            <option value="${t.id}">${t.type}</option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="membreId">Membre <span class="required">*</span></label>
                    <select id="membreId" name="membreId" class="form-control" required>
                        <option value="">-- Sélectionner un membre --</option>
                        <c:forEach var="m" items="${membres}">
                            <option value="${m.id}" data-type="${m.typeMembre.id}" data-type-name="${m.typeMembre.type}">
                                ${m.nom} ${m.prenom} - ${m.typeMembre.type}
                                <c:if test="${m.heuresVol != null}"> (${m.heuresVol}h de vol)</c:if>
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="form-section">
                <h4><i class="fas fa-tag"></i> Rôle dans l'Équipage</h4>
                <div class="form-group">
                    <label for="role">Rôle</label>
                    <input type="text" id="role" name="role" class="form-control" 
                           placeholder="Ex: Commandant de bord, Copilote, Chef cabine...">
                    <small class="form-text text-muted">
                        Si non spécifié, le type de membre sera utilisé comme rôle.
                    </small>
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary btn-lg">
                    <i class="fas fa-check"></i> Affecter
                </button>
                <a href="${pageContext.request.contextPath}/affectations" class="btn btn-secondary btn-lg">
                    <i class="fas fa-times"></i> Annuler
                </a>
            </div>
        </form>
    </div>
</div>

<style>
    .affectation-form {
        max-width: 800px;
    }
    .form-section {
        background: var(--bg-light, #f8f9fa);
        padding: 20px;
        border-radius: 8px;
        margin-bottom: 20px;
    }
    .form-section h4 {
        margin-bottom: 15px;
        color: var(--primary-color, #1e3a5f);
        border-bottom: 2px solid var(--primary-color, #1e3a5f);
        padding-bottom: 10px;
    }
    .form-section h4 i {
        margin-right: 10px;
    }
    .form-group {
        margin-bottom: 15px;
    }
    .form-group label {
        display: block;
        margin-bottom: 5px;
        font-weight: 500;
        color: var(--text-primary, #333);
    }
    .form-group .required {
        color: #dc3545;
    }
    .form-control {
        width: 100%;
        padding: 10px 12px;
        border: 1px solid var(--border-color, #dee2e6);
        border-radius: 6px;
        font-size: 14px;
    }
    .form-control:focus {
        border-color: var(--primary-color, #1e3a5f);
        outline: none;
        box-shadow: 0 0 0 3px rgba(30, 58, 95, 0.1);
    }
    .form-text {
        font-size: 12px;
        color: var(--text-secondary, #6c757d);
        margin-top: 5px;
    }
    .form-actions {
        display: flex;
        gap: 15px;
        margin-top: 25px;
    }
    .btn-lg {
        padding: 12px 30px;
        font-size: 16px;
    }
    .alert {
        padding: 15px 20px;
        border-radius: 8px;
        margin-bottom: 20px;
    }
    .alert-danger {
        background-color: #f8d7da;
        border: 1px solid #f5c6cb;
        color: #721c24;
    }
    .alert-success {
        background-color: #d4edda;
        border: 1px solid #c3e6cb;
        color: #155724;
    }
    .alert i {
        margin-right: 10px;
    }
    .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
</style>

<script>
    function filterMembres() {
        var filterType = document.getElementById('filterType').value;
        var membreSelect = document.getElementById('membreId');
        var options = membreSelect.querySelectorAll('option');
        
        options.forEach(function(option) {
            if (option.value === '') {
                // Toujours afficher l'option par défaut
                option.style.display = '';
            } else {
                var optionType = option.getAttribute('data-type');
                if (filterType === '' || optionType === filterType) {
                    option.style.display = '';
                } else {
                    option.style.display = 'none';
                }
            }
        });
        
        // Réinitialiser la sélection
        membreSelect.value = '';
    }
    
    // Auto-remplir le rôle basé sur le type de membre sélectionné
    document.getElementById('membreId').addEventListener('change', function() {
        var selectedOption = this.options[this.selectedIndex];
        var typeName = selectedOption.getAttribute('data-type-name');
        var roleInput = document.getElementById('role');
        
        if (typeName && roleInput.value === '') {
            roleInput.placeholder = 'Suggestion: ' + typeName;
        }
    });
</script>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
