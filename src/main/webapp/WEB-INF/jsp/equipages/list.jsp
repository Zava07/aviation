<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title"><i class="fas fa-users-cog"></i> Liste des Équipages</h3>
    </div>
    <div class="card-body">
        <!-- Filtres multi-critères -->
        <div class="filter-section mb-4">
            <form method="get" action="${pageContext.request.contextPath}/equipages" class="filter-form">
                <div class="filter-row">
                    <div class="filter-group">
                        <label for="nom">Nom</label>
                        <input type="text" id="nom" name="nom" value="${param.nom}" placeholder="Rechercher par nom" class="form-control" />
                    </div>
                    <div class="filter-group">
                        <label for="prenom">Prénom</label>
                        <input type="text" id="prenom" name="prenom" value="${param.prenom}" placeholder="Rechercher par prénom" class="form-control" />
                    </div>
                    <div class="filter-group">
                        <label for="typeId">Type de Membre</label>
                        <select id="typeId" name="typeId" class="form-control">
                            <option value="">-- Tous les types --</option>
                            <c:forEach var="t" items="${types}">
                                <option value="${t.id}" <c:if test="${param.typeId == t.id}">selected</c:if>>${t.type}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="filter-row">
                    <div class="filter-group">
                        <label for="qualifications">Qualifications</label>
                        <input type="text" id="qualifications" name="qualifications" value="${param.qualifications}" placeholder="Rechercher par qualifications" class="form-control" />
                    </div>
                    <div class="filter-group">
                        <label for="heuresVolMin">Heures de Vol Min</label>
                        <input type="number" id="heuresVolMin" name="heuresVolMin" value="${param.heuresVolMin}" placeholder="Min" class="form-control" min="0" />
                    </div>
                    <div class="filter-group">
                        <label for="heuresVolMax">Heures de Vol Max</label>
                        <input type="number" id="heuresVolMax" name="heuresVolMax" value="${param.heuresVolMax}" placeholder="Max" class="form-control" min="0" />
                    </div>
                </div>
                <div class="filter-actions">
                    <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i> Rechercher</button>
                    <a href="${pageContext.request.contextPath}/equipages" class="btn btn-secondary"><i class="fas fa-undo"></i> Réinitialiser</a>
                </div>
            </form>
        </div>

        <!-- Tableau des équipages -->
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nom</th>
                        <th>Prénom</th>
                        <th>Type</th>
                        <th>Qualifications</th>
                        <th>Heures de Vol</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="m" items="${membres}">
                        <tr>
                            <td>${m.id}</td>
                            <td>${m.nom}</td>
                            <td>${m.prenom}</td>
                            <td>
                                <span class="badge badge-info">${m.typeMembre.type}</span>
                            </td>
                            <td>${m.qualifications}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${m.heuresVol != null}">
                                        <span class="heures-vol">${m.heuresVol} h</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">N/A</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
                <c:if test="${empty membres}">
                    <tfoot>
                        <tr>
                            <td colspan="6" class="text-muted text-center">
                                <i class="fas fa-info-circle"></i> Aucun membre d'équipage trouvé pour ces critères.
                            </td>
                        </tr>
                    </tfoot>
                </c:if>
            </table>
        </div>

        <!-- Statistiques -->
        <div class="stats-section mt-3">
            <span class="stats-item">
                <i class="fas fa-users"></i> Total: <strong>${membres.size()}</strong> membre(s)
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
    .filter-form .filter-actions {
        display: flex;
        gap: 10px;
        margin-top: 10px;
    }
    .badge-info {
        background-color: var(--primary-color, #1e3a5f);
        color: white;
        padding: 4px 10px;
        border-radius: 4px;
        font-size: 0.85em;
    }
    .heures-vol {
        font-weight: 600;
        color: var(--primary-color, #1e3a5f);
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
</style>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
