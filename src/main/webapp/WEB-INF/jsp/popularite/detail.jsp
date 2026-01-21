<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">
            <i class="fas fa-plane"></i> Popularité Vol: ${vol.numeroVol}
        </h3>
        <a href="${pageContext.request.contextPath}/popularite-vols" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Retour
        </a>
    </div>
    <div class="card-body">
        <!-- Info Vol -->
        <div class="vol-info-card mb-4">
            <div class="vol-info-header">
                <h4>${vol.numeroVol}</h4>
                <span class="route">${vol.aeroportOrigine.nomAeroport} → ${vol.aeroportDestination.nomAeroport}</span>
            </div>
            <div class="vol-stats">
                <div class="stat-item">
                    <span class="stat-label">Total Points</span>
                    <span class="stat-value">${totalPoints != null ? totalPoints : 0}</span>
                </div>
                <div class="stat-item">
                    <span class="stat-label">Réservations tracées</span>
                    <span class="stat-value">${history.size()}</span>
                </div>
            </div>
        </div>

        <div style="display:flex;gap:20px;flex-wrap:wrap">
            <!-- Points par mois -->
            <div style="flex:1;min-width:300px">
                <h4><i class="fas fa-chart-bar"></i> Points par Mois (Année courante)</h4>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Mois</th>
                            <th>Points</th>
                            <th>Graphe</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="maxPoints" value="0" />
                        <c:forEach var="mp" items="${monthlyPoints}">
                            <c:if test="${mp.points > maxPoints}">
                                <c:set var="maxPoints" value="${mp.points}" />
                            </c:if>
                        </c:forEach>
                        <c:forEach var="mp" items="${monthlyPoints}">
                            <tr>
                                <td>${mp.mois}</td>
                                <td><span class="badge badge-primary">${mp.points}</span></td>
                                <td>
                                    <div class="bar-container">
                                        <c:if test="${maxPoints > 0}">
                                            <div class="bar" style="width: ${(mp.points / maxPoints) * 100}%"></div>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Historique détaillé -->
            <div style="flex:1;min-width:300px">
                <h4><i class="fas fa-history"></i> Historique Détaillé</h4>
                <div style="max-height:400px;overflow-y:auto">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Points</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="h" items="${history}">
                                <tr>
                                    <td>
                                        <c:if test="${h.datePoint != null}">
                                            ${h.datePoint.toLocalDate()} ${h.datePoint.toLocalTime()}
                                        </c:if>
                                    </td>
                                    <td><span class="badge badge-success">+${h.point}</span></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${empty history}">
                        <p class="text-muted text-center">Aucun historique disponible.</p>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    .vol-info-card {
        background: linear-gradient(135deg, var(--primary-color, #1e3a5f), #2c5282);
        color: white;
        padding: 25px;
        border-radius: 12px;
        box-shadow: 0 4px 15px rgba(30, 58, 95, 0.3);
    }
    .vol-info-header {
        margin-bottom: 20px;
    }
    .vol-info-header h4 {
        font-size: 1.5em;
        margin-bottom: 5px;
    }
    .vol-info-header .route {
        opacity: 0.9;
        font-size: 1.1em;
    }
    .vol-stats {
        display: flex;
        gap: 40px;
    }
    .stat-item {
        display: flex;
        flex-direction: column;
    }
    .stat-label {
        font-size: 0.85em;
        opacity: 0.8;
    }
    .stat-value {
        font-size: 1.8em;
        font-weight: 600;
    }
    .badge-primary {
        background-color: var(--primary-color, #1e3a5f);
        color: white;
        padding: 4px 10px;
        border-radius: 4px;
    }
    .badge-success {
        background-color: #28a745;
        color: white;
        padding: 4px 10px;
        border-radius: 4px;
    }
    .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .bar-container {
        background: #e9ecef;
        border-radius: 4px;
        height: 20px;
        width: 100%;
    }
    .bar {
        background: linear-gradient(90deg, #28a745, #20c997);
        height: 100%;
        border-radius: 4px;
        transition: width 0.3s ease;
    }
</style>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
