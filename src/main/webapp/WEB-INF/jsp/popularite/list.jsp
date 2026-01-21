<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title"><i class="fas fa-chart-line"></i> Popularité des Vols</h3>
    </div>
    <div class="card-body">
        <!-- Filtres -->
        <form method="get" action="${pageContext.request.contextPath}/popularite-vols" class="mb-4">
            <div style="display:flex;gap:10px;flex-wrap:wrap;align-items:center">
                <select name="annee" class="form-control" style="width:120px">
                    <c:forEach var="y" items="${years}">
                        <option value="${y}" ${filterAnnee == y ? 'selected' : ''}>${y}</option>
                    </c:forEach>
                </select>
                <select name="mois" class="form-control" style="width:150px">
                    <option value="">-- Toute l'année --</option>
                    <c:forEach var="i" begin="0" end="11">
                        <option value="${i + 1}" ${filterMois != null && filterMois == i + 1 ? 'selected' : ''}>${monthNames[i]}</option>
                    </c:forEach>
                </select>
                <select name="volId" class="form-control" style="width:200px">
                    <option value="">-- Tous les vols --</option>
                    <c:forEach var="v" items="${vols}">
                        <option value="${v.id}" ${filterVolId != null && filterVolId == v.id ? 'selected' : ''}>${v.numeroVol}</option>
                    </c:forEach>
                </select>
                <button type="submit" class="btn btn-primary">Filtrer</button>
                <a href="${pageContext.request.contextPath}/popularite-vols" class="btn btn-secondary">Réinitialiser</a>
            </div>
        </form>

        <div style="display:flex;gap:20px;flex-wrap:wrap">
            <!-- Classement période -->
            <div style="flex:2;min-width:400px">
                <h4>
                    <i class="fas fa-trophy"></i> Classement 
                    <c:choose>
                        <c:when test="${filterMois != null}">
                            ${monthNames[filterMois - 1]} ${filterAnnee}
                        </c:when>
                        <c:otherwise>
                            Année ${filterAnnee}
                        </c:otherwise>
                    </c:choose>
                </h4>
                
                <c:if test="${selectedVol != null}">
                    <div class="alert alert-info">
                        <strong>Vol sélectionné: ${selectedVol.numeroVol}</strong>
                        (${selectedVol.aeroportOrigine.nomAeroport} → ${selectedVol.aeroportDestination.nomAeroport})
                        <br/>
                        <strong>Total points:</strong> ${volTotalPoints != null ? volTotalPoints : 0}
                    </div>
                    <h5>Historique des points</h5>
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Points</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="h" items="${volHistory}">
                                <tr>
                                    <td>${h.datePoint}</td>
                                    <td><span class="badge badge-success">+${h.point}</span></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${empty volHistory}">
                        <p class="text-muted">Aucun historique pour cette période.</p>
                    </c:if>
                </c:if>

                <c:if test="${selectedVol == null}">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th style="width:60px">#</th>
                                <th>Vol</th>
                                <th>Route</th>
                                <th>Points (période)</th>
                                <th>Points (total)</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="r" items="${ranking}">
                                <tr>
                                    <td>
                                        <c:choose>
                                            <c:when test="${r.position == 1}">
                                                <span style="font-size:1.5em">🥇</span>
                                            </c:when>
                                            <c:when test="${r.position == 2}">
                                                <span style="font-size:1.5em">🥈</span>
                                            </c:when>
                                            <c:when test="${r.position == 3}">
                                                <span style="font-size:1.5em">🥉</span>
                                            </c:when>
                                            <c:otherwise>
                                                ${r.position}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><strong>${r.vol.numeroVol}</strong></td>
                                    <td>${r.vol.aeroportOrigine.nomAeroport} → ${r.vol.aeroportDestination.nomAeroport}</td>
                                    <td><span class="badge badge-primary">${r.pointsPeriode} pts</span></td>
                                    <td><span class="badge badge-success">${r.pointsTotal} pts</span></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/popularite-vols/detail/${r.vol.id}" class="btn btn-sm btn-info">
                                            <i class="fas fa-eye"></i> Détail
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${empty ranking}">
                        <p class="text-muted text-center">Aucune donnée pour cette période.</p>
                    </c:if>
                </c:if>
            </div>

            <!-- Top 10 Global -->
            <div style="flex:1;min-width:300px">
                <h4><i class="fas fa-star"></i> Top 10 Global</h4>
                <div class="top-list">
                    <c:forEach var="g" items="${globalRanking}">
                        <div class="top-item" style="display:flex;justify-content:space-between;padding:8px;border-bottom:1px solid #eee">
                            <span>
                                <c:choose>
                                    <c:when test="${g.position == 1}">🥇</c:when>
                                    <c:when test="${g.position == 2}">🥈</c:when>
                                    <c:when test="${g.position == 3}">🥉</c:when>
                                    <c:otherwise>${g.position}.</c:otherwise>
                                </c:choose>
                                <strong>${g.vol.numeroVol}</strong>
                            </span>
                            <span class="badge badge-primary">${g.points} pts</span>
                        </div>
                    </c:forEach>
                </div>
                <c:if test="${empty globalRanking}">
                    <p class="text-muted">Aucune donnée disponible.</p>
                </c:if>
            </div>
        </div>
    </div>
</div>

<style>
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
    .top-list {
        background: #f8f9fa;
        border-radius: 8px;
        padding: 10px;
    }
    .top-item:last-child {
        border-bottom: none !important;
    }
</style>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
