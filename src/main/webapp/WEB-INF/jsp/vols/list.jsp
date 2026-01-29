<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<style>
    .vols-container { padding: 20px; background: #f4f6f9; min-height: 100vh; }
    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; }
    .page-header h2 { margin: 0; color: #2c3e50; font-size: 1.6rem; display: flex; align-items: center; gap: 12px; }
    .page-header h2 i { color: #3498db; }
    .badge-count { background: linear-gradient(135deg, #3498db, #2980b9); color: white; padding: 5px 12px; border-radius: 15px; font-size: 0.85rem; }
    .btn-primary-custom { padding: 10px 20px; background: linear-gradient(135deg, #3498db, #2980b9); color: white; border: none; border-radius: 8px; font-weight: 600; text-decoration: none; display: flex; align-items: center; gap: 6px; }
    .btn-primary-custom:hover { color: white; box-shadow: 0 4px 15px rgba(52,152,219,0.4); }
    
    .filter-card { background: white; border-radius: 12px; padding: 15px 20px; margin-bottom: 20px; box-shadow: 0 2px 10px rgba(0,0,0,0.06); }
    .filter-form { display: flex; gap: 12px; align-items: center; flex-wrap: wrap; }
    .filter-form input, .filter-form select { padding: 8px 12px; border: 1px solid #ddd; border-radius: 6px; font-size: 0.9rem; }
    .filter-form input:focus, .filter-form select:focus { outline: none; border-color: #3498db; }
    .btn-filter { padding: 8px 16px; border-radius: 6px; font-weight: 500; border: none; cursor: pointer; font-size: 0.9rem; }
    .btn-filter.primary { background: #3498db; color: white; }
    .btn-filter.secondary { background: #ecf0f1; color: #7f8c8d; text-decoration: none; }
    
    .content-card { background: white; border-radius: 12px; box-shadow: 0 2px 10px rgba(0,0,0,0.06); overflow: hidden; margin-bottom: 20px; }
    .card-head { padding: 15px 20px; background: linear-gradient(135deg, #2c3e50, #34495e); color: white; }
    .card-head h3 { margin: 0; font-size: 1rem; display: flex; align-items: center; gap: 8px; }
    
    .data-table { width: 100%; border-collapse: collapse; }
    .data-table thead th { background: #f8f9fa; padding: 12px 15px; text-align: left; font-weight: 600; color: #495057; font-size: 0.8rem; text-transform: uppercase; border-bottom: 2px solid #dee2e6; }
    .data-table tbody td { padding: 14px 15px; border-bottom: 1px solid #eee; vertical-align: middle; }
    .data-table tbody tr:hover { background: #f8f9fa; }
    
    .vol-code { font-weight: 700; color: #2c3e50; }
    .route-badge { display: inline-flex; align-items: center; gap: 6px; padding: 4px 10px; background: #ebf5fb; color: #3498db; border-radius: 6px; font-size: 0.85rem; }
    .instance-count { display: inline-flex; align-items: center; justify-content: center; width: 30px; height: 30px; background: linear-gradient(135deg, #9b59b6, #8e44ad); color: white; border-radius: 50%; font-weight: 600; font-size: 0.85rem; }
    
    .actions-cell { display: flex; gap: 6px; flex-wrap: wrap; }
    .btn-action { padding: 6px 10px; border-radius: 6px; font-size: 0.75rem; font-weight: 600; text-decoration: none; border: none; cursor: pointer; }
    .btn-action.info { background: #ebf5fb; color: #3498db; }
    .btn-action.info:hover { background: #3498db; color: white; }
    .btn-action.secondary { background: #f4f6f7; color: #7f8c8d; }
    .btn-action.secondary:hover { background: #95a5a6; color: white; }
    .btn-action.primary { background: #e8f8f5; color: #27ae60; }
    .btn-action.primary:hover { background: #27ae60; color: white; }
    .btn-action.danger { background: #fdedec; color: #e74c3c; }
    .btn-action.danger:hover { background: #e74c3c; color: white; }
    
    .ca-card { background: linear-gradient(135deg, #667eea, #764ba2); color: white; }
    .ca-card .card-head { background: rgba(255,255,255,0.1); }
    .ca-table { width: 100%; border-collapse: collapse; }
    .ca-table thead th { background: rgba(255,255,255,0.1); padding: 12px 15px; text-align: left; font-weight: 600; color: rgba(255,255,255,0.9); font-size: 0.75rem; text-transform: uppercase; border-bottom: 1px solid rgba(255,255,255,0.2); }
    .ca-table tbody td { padding: 14px 15px; border-bottom: 1px solid rgba(255,255,255,0.1); color: white; }
    .ca-table tbody tr:hover { background: rgba(255,255,255,0.1); }
    
    .amt { font-weight: 600; font-size: 0.9rem; }
    .amt.tickets { color: #a8e6cf; }
    .amt.ads { color: #ffd93d; }
    .amt.paid { color: #6bcb77; }
    .amt.rest { color: #ff6b6b; }
    .amt.total { color: white; font-size: 0.95rem; }
    .progress-bar-sm { width: 100%; height: 4px; background: rgba(255,255,255,0.3); border-radius: 5px; margin-top: 4px; overflow: hidden; }
    .progress-bar-sm .fill { height: 100%; background: #6bcb77; border-radius: 5px; }
    
    .empty-state { text-align: center; padding: 40px 20px; color: #95a5a6; }
    .empty-state i { font-size: 2.5rem; margin-bottom: 10px; opacity: 0.5; }
</style>

<div class="vols-container">
    <div class="page-header">
        <h2><i class="fas fa-plane-departure"></i> Liste des Vols <span class="badge-count">${empty vols ? 0 : vols.size()} vol(s)</span></h2>
        <a href="${pageContext.request.contextPath}/vols/create" class="btn-primary-custom"><i class="fas fa-plus"></i> Nouveau Vol</a>
    </div>
    
    <div class="filter-card">
        <form method="get" action="${pageContext.request.contextPath}/vols" class="filter-form">
            <input type="date" name="date" value="${param.date}" placeholder="Date" />
            <select name="destinationId">
                <option value="">Toutes destinations</option>
                <c:forEach var="a" items="${aeroports}">
                    <option value="${a.id}" <c:if test="${param.destinationId == a.id}">selected</c:if>>${a.nomAeroport}</option>
                </c:forEach>
            </select>
            <button type="submit" class="btn-filter primary"><i class="fas fa-search"></i> Rechercher</button>
            <a href="${pageContext.request.contextPath}/vols" class="btn-filter secondary"><i class="fas fa-undo"></i> Réinitialiser</a>
        </form>
    </div>
    
    <div class="content-card">
        <div class="card-head"><h3><i class="fas fa-list"></i> Vols</h3></div>
        <table class="data-table">
            <thead>
                <tr><th>N° Vol</th><th>Route</th><th>Instances</th><th>Actions</th></tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty vols}">
                        <c:forEach var="vol" items="${vols}">
                            <tr>
                                <td><span class="vol-code">${vol.numeroVol}</span></td>
                                <td><span class="route-badge">${vol.aeroportOrigine != null ? vol.aeroportOrigine.nomAeroport : '—'} <i class="fas fa-arrow-right"></i> ${vol.aeroportDestination != null ? vol.aeroportDestination.nomAeroport : '—'}</span></td>
                                <td><span class="instance-count">${empty vol.instances ? 0 : vol.instances.size()}</span></td>
                                <td>
                                    <div class="actions-cell">
                                        <a href="${pageContext.request.contextPath}/vols/edit/${vol.id}" class="btn-action info"><i class="fas fa-edit"></i> Modifier</a>
                                        <a href="${pageContext.request.contextPath}/vol-instances?volId=${vol.id}" class="btn-action secondary"><i class="fas fa-calendar"></i> Instances</a>
                                        <a href="${pageContext.request.contextPath}/vols/details/${vol.id}" class="btn-action primary"><i class="fas fa-chart-line"></i> Détails</a>
                                        <form method="post" action="${pageContext.request.contextPath}/vols/delete/${vol.id}" style="display:inline">
                                            <button type="submit" class="btn-action danger" onclick="return confirm('Confirmer ?')"><i class="fas fa-trash"></i> Supprimer</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="4"><div class="empty-state"><i class="fas fa-plane-slash"></i><p>Aucun vol trouvé</p></div></td></tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
    
    <div class="content-card ca-card">
        <div class="card-head"><h3><i class="fas fa-chart-pie"></i> Chiffre d'Affaires par Vol</h3></div>
        <table class="ca-table">
            <thead>
                <tr><th>Vol</th><th>Route</th><th>CA Billets</th><th>CA Publicité</th><th>Pub Payé</th><th>Pub Reste</th><th>Total CA</th></tr>
            </thead>
            <tbody>
                <c:forEach var="m" items="${volCaList}">
                    <c:set var="pct" value="${m.adsSum > 0 ? (m.adsPaid / m.adsSum * 100) : 0}"/>
                    <tr>
                        <td><strong>${m.vol.numeroVol}</strong></td>
                        <td>${m.vol.aeroportOrigine != null ? m.vol.aeroportOrigine.nomAeroport : '—'} → ${m.vol.aeroportDestination != null ? m.vol.aeroportDestination.nomAeroport : '—'}</td>
                        <td class="amt tickets"><fmt:formatNumber value="${m.ticketsSum}" type="number" groupingUsed="true"/> MGA</td>
                        <td class="amt ads"><fmt:formatNumber value="${m.adsSum}" type="number" groupingUsed="true"/> MGA</td>
                        <td class="amt paid"><fmt:formatNumber value="${m.adsPaid}" type="number" groupingUsed="true"/> MGA<div class="progress-bar-sm"><div class="fill" style="width:${pct}%"></div></div></td>
                        <td class="amt rest"><fmt:formatNumber value="${m.adsRest}" type="number" groupingUsed="true"/> MGA</td>
                        <td class="amt total"><fmt:formatNumber value="${m.total}" type="number" groupingUsed="true"/> MGA</td>
                    </tr>
                </c:forEach>
                <tr style="font-weight:700; background: rgba(255,255,255,0.06);">
                    <td colspan="2">Total</td>
                    <td class="amt tickets"><fmt:formatNumber value="${totalTicketsAll}" type="number" groupingUsed="true"/> MGA</td>
                    <td class="amt ads"><fmt:formatNumber value="${totalAdsAll}" type="number" groupingUsed="true"/> MGA</td>
                    <td class="amt paid"><fmt:formatNumber value="${totalAdsPaidAll}" type="number" groupingUsed="true"/> MGA</td>
                    <td class="amt rest"><fmt:formatNumber value="${totalAdsRestAll}" type="number" groupingUsed="true"/> MGA</td>
                    <td class="amt total"><fmt:formatNumber value="${totalCAAll}" type="number" groupingUsed="true"/> MGA</td>
                </tr>
                <c:if test="${empty volCaList}">
                    <tr><td colspan="7"><div class="empty-state"><i class="fas fa-chart-bar"></i><p>Aucune donnée</p></div></td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>