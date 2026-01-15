<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title"><i class="fas fa-clipboard-list"></i> Affectation des Équipages aux Vols</h3>
        <a href="${pageContext.request.contextPath}/affectations/affecter" class="btn btn-primary">
            <i class="fas fa-plus"></i> Affecter
        </a>
    </div>
    <div class="card-body">
        <!-- Filtres multi-critères -->
        <div class="filter-section mb-4">
            <form method="get" action="${pageContext.request.contextPath}/affectations" class="filter-form">
                <div class="filter-row">
                    <div class="filter-group">
                        <label for="volInstanceId">Vol Instance</label>
                        <select id="volInstanceId" name="volInstanceId" class="form-control">
                            <option value="">-- Tous les vols --</option>
                            <c:forEach var="vi" items="${volInstances}">
                                <option value="${vi.id}" <c:if test="${param.volInstanceId == vi.id}">selected</c:if>>
                                    Vol #${vi.id} - ${vi.vol.aeroportOrigine.nomAeroport} → ${vi.vol.aeroportDestination.nomAeroport}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="filter-group">
                        <label for="membreId">Membre</label>
                        <select id="membreId" name="membreId" class="form-control">
                            <option value="">-- Tous les membres --</option>
                            <c:forEach var="m" items="${membres}">
                                <option value="${m.id}" <c:if test="${param.membreId == m.id}">selected</c:if>>
                                    ${m.nom} ${m.prenom}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="filter-group">
                        <label for="typeMembreId">Type de Membre</label>
                        <select id="typeMembreId" name="typeMembreId" class="form-control">
                            <option value="">-- Tous les types --</option>
                            <c:forEach var="t" items="${typesMembre}">
                                <option value="${t.id}" <c:if test="${param.typeMembreId == t.id}">selected</c:if>>${t.type}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="filter-row">
                    <div class="filter-group">
                        <label for="role">Rôle</label>
                        <input type="text" id="role" name="role" value="${param.role}" placeholder="Ex: Commandant, Copilote..." class="form-control" />
                    </div>
                    <div class="filter-group">
                        <label for="dateDepart">Date de Départ</label>
                        <input type="date" id="dateDepart" name="dateDepart" value="${param.dateDepart}" class="form-control" />
                    </div>
                    <div class="filter-group filter-actions-group">
                        <label>&nbsp;</label>
                        <div class="filter-actions">
                            <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i> Rechercher</button>
                            <a href="${pageContext.request.contextPath}/affectations" class="btn btn-secondary"><i class="fas fa-undo"></i> Réinitialiser</a>
                        </div>
                    </div>
                </div>
            </form>
        </div>

        <!-- Tableau des affectations -->
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Vol Instance</th>
                        <th>Trajet</th>
                        <th>Date Départ</th>
                        <th>Membre</th>
                        <th>Type</th>
                        <th>Rôle</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="aff" items="${affectations}">
                        <tr>
                            <td>
                                <span class="vol-id">#${aff.volInstance.id}</span>
                            </td>
                            <td>
                                <c:if test="${aff.volInstance.vol != null}">
                                    <span class="trajet">
                                        ${aff.volInstance.vol.aeroportOrigine.nomAeroport} 
                                        <i class="fas fa-arrow-right"></i> 
                                        ${aff.volInstance.vol.aeroportDestination.nomAeroport}
                                    </span>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${aff.volInstance.dateDepart != null}">
                                    <span class="date-depart">
                                        <i class="fas fa-calendar"></i> 
                                        ${aff.volInstance.dateDepart.toLocalDate()} 
                                        <i class="fas fa-clock"></i> 
                                        ${aff.volInstance.dateDepart.toLocalTime()}
                                    </span>
                                </c:if>
                            </td>
                            <td>
                                <strong>${aff.membre.nom} ${aff.membre.prenom}</strong>
                            </td>
                            <td>
                                <span class="badge badge-info">${aff.membre.typeMembre.type}</span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${aff.role != null && !aff.role.isEmpty()}">
                                        <span class="badge badge-role">${aff.role}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">Non défini</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/affectations/delete/${aff.volInstance.id}/${aff.membre.id}" style="display:inline">
                                    <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Supprimer cette affectation ?')">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
                <c:if test="${empty affectations}">
                    <tfoot>
                        <tr>
                            <td colspan="7" class="text-muted text-center">
                                <i class="fas fa-info-circle"></i> Aucune affectation trouvée pour ces critères.
                            </td>
                        </tr>
                    </tfoot>
                </c:if>
            </table>
        </div>

        <!-- Statistiques -->
        <div class="stats-section mt-3">
            <span class="stats-item">
                <i class="fas fa-clipboard-check"></i> Total: <strong>${affectations.size()}</strong> affectation(s)
            </span>
        </div>
    </div>
</div>

<style>
    .filter-section {
        background: var(--bg-light, #f8f9fa);
        padding: 20px;
        border-radius: 8px;
        margin-bottom: 20px;
    }
    .filter-form .filter-row {
        display: flex;
        gap: 15px;
        margin-bottom: 15px;
        flex-wrap: wrap;
    }
    .filter-form .filter-group {
        flex: 1;
        min-width: 200px;
    }
    .filter-form .filter-group label {
        display: block;
        margin-bottom: 5px;
        font-weight: 500;
        color: var(--text-primary, #333);
    }
    .filter-actions-group {
        display: flex;
        flex-direction: column;
    }
    .filter-actions {
        display: flex;
        gap: 10px;
    }
    .badge-info {
        background-color: var(--primary-color, #1e3a5f);
        color: white;
        padding: 4px 10px;
        border-radius: 4px;
        font-size: 0.85em;
    }
    .badge-role {
        background-color: #28a745;
        color: white;
        padding: 4px 10px;
        border-radius: 4px;
        font-size: 0.85em;
    }
    .vol-id {
        font-weight: 600;
        color: var(--primary-color, #1e3a5f);
    }
    .trajet {
        color: #555;
    }
    .trajet i {
        color: var(--primary-color, #1e3a5f);
        margin: 0 5px;
    }
    .date-depart i {
        color: var(--primary-color, #1e3a5f);
        margin-right: 3px;
    }
    .stats-section {
        padding: 10px 0;
        border-top: 1px solid var(--border-color, #dee2e6);
    }
    .stats-item {
        color: var(--text-secondary, #6c757d);
    }
    .stats-item strong {
        color: var(--primary-color, #1e3a5f);
    }
    .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
</style>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
