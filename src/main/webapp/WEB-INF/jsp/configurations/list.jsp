<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title"><i class="fas fa-cog"></i> Configuration Système</h3>
        <a href="${pageContext.request.contextPath}/configurations/create" class="btn btn-primary">
            <i class="fas fa-plus"></i> Nouvelle Configuration
        </a>
    </div>
    <div class="card-body">
        <!-- Configuration Active -->
        <c:if test="${currentConfig != null}">
            <div class="current-config-card mb-4">
                <h4><i class="fas fa-star"></i> Configuration Active</h4>
                <div class="config-details">
                    <div class="config-item">
                        <span class="config-label">Points de Fidélité</span>
                                <span class="config-value">${currentConfig.pointFidelite} points</span>
                    </div>
                            <div class="config-item">
                                <span class="config-label">Points par Vol</span>
                                <span class="config-value">${currentConfig.pointVol} pts</span>
                            </div>
                    <div class="config-item">
                        <span class="config-label">Coût Publicité</span>
                        <span class="config-value">${currentConfig.coutPubliciter} MGA</span>
                    </div>
                    <div class="config-item">
                        <span class="config-label">Dernière Modification</span>
                        <span class="config-value">
                            <c:if test="${currentConfig.dateModification != null}">
                                ${currentConfig.dateModification.toLocalDate()} à ${currentConfig.dateModification.toLocalTime()}
                            </c:if>
                        </span>
                    </div>
                </div>
            </div>
        </c:if>
        <c:if test="${currentConfig == null}">
            <div class="alert alert-warning">
                <i class="fas fa-exclamation-triangle"></i> Aucune configuration n'est définie. Veuillez créer une configuration.
            </div>
        </c:if>

        <!-- Historique des Configurations -->
        <h4 class="mt-4 mb-3"><i class="fas fa-history"></i> Historique des Configurations</h4>
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Points Fidélité</th>
                        <th>Points par Vol</th>
                        <th>Coût pub.</th>
                        <th>Date de Modification</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cfg" items="${configs}">
                        <tr>
                            <td>#${cfg.id}</td>
                            <td>
                                <span class="badge badge-primary">${cfg.pointFidelite} points</span>
                            </td>
                            <td>
                                <span class="badge badge-info">${cfg.pointVol} pts</span>
                            </td>
                            <td>
                                <span class="badge badge-warning">${cfg.coutPubliciter} MGA</span>
                            </td>
                            <td>
                                <c:if test="${cfg.dateModification != null}">
                                    <i class="fas fa-calendar"></i> ${cfg.dateModification.toLocalDate()}
                                    <i class="fas fa-clock"></i> ${cfg.dateModification.toLocalTime()}
                                </c:if>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${currentConfig != null && cfg.id == currentConfig.id}">
                                        <span class="badge badge-success"><i class="fas fa-check"></i> Active</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-secondary">Ancienne</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/configurations/edit/${cfg.id}" class="btn btn-sm btn-info">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <form method="post" action="${pageContext.request.contextPath}/configurations/delete/${cfg.id}" style="display:inline">
                                    <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Supprimer cette configuration ?')">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
                <c:if test="${empty configs}">
                    <tfoot>
                        <tr>
                            <td colspan="7" class="text-muted text-center">
                                <i class="fas fa-info-circle"></i> Aucune configuration trouvée.
                            </td>
                        </tr>
                    </tfoot>
                </c:if>
            </table>
        </div>

        <!-- Statistiques -->
        <div class="stats-section mt-3">
            <span class="stats-item">
                <i class="fas fa-database"></i> Total: <strong>${configs.size()}</strong> configuration(s)
            </span>
        </div>
    </div>
</div>

<style>
    .current-config-card {
        background: linear-gradient(135deg, var(--primary-color, #1e3a5f), #2c5282);
        color: white;
        padding: 25px;
        border-radius: 12px;
        box-shadow: 0 4px 15px rgba(30, 58, 95, 0.3);
    }
    .current-config-card h4 {
        margin-bottom: 20px;
        font-size: 1.2em;
    }
    .current-config-card h4 i {
        color: #ffc107;
        margin-right: 10px;
    }
    .config-details {
        display: flex;
        gap: 40px;
        flex-wrap: wrap;
    }
    .config-item {
        display: flex;
        flex-direction: column;
    }
    .config-label {
        font-size: 0.85em;
        opacity: 0.8;
        margin-bottom: 5px;
    }
    .config-value {
        font-size: 1.3em;
        font-weight: 600;
    }
    .badge-primary {
        background-color: var(--primary-color, #1e3a5f);
        color: white;
        padding: 5px 12px;
        border-radius: 4px;
        font-size: 0.9em;
    }
    .badge-success {
        background-color: #28a745;
        color: white;
        padding: 4px 10px;
        border-radius: 4px;
        font-size: 0.85em;
    }
    .badge-secondary {
        background-color: #6c757d;
        color: white;
        padding: 4px 10px;
        border-radius: 4px;
        font-size: 0.85em;
    }
    .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
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
