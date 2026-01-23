<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<style>
    .details-container { padding: 20px; background: #f4f6f9; min-height: 100vh; }
    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; }
    .page-header h2 { margin: 0; color: #2c3e50; font-size: 1.6rem; display: flex; align-items: center; gap: 12px; }
    .page-header h2 i { color: #3498db; }
    .btn-back { padding: 10px 20px; background: #ecf0f1; color: #7f8c8d; border: none; border-radius: 8px; font-weight: 600; text-decoration: none; display: flex; align-items: center; gap: 6px; }
    .btn-back:hover { background: #bdc3c7; color: #2c3e50; }
    
    .vol-info { background: white; border-radius: 12px; padding: 20px; margin-bottom: 20px; box-shadow: 0 2px 10px rgba(0,0,0,0.06); }
    .vol-title { font-size: 1.5rem; font-weight: 700; color: #2c3e50; display: flex; align-items: center; gap: 12px; margin-bottom: 20px; }
    .route-badge { padding: 5px 12px; background: #ebf5fb; color: #3498db; border-radius: 8px; font-size: 0.95rem; font-weight: 500; }
    
    .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 15px; margin-bottom: 20px; }
    .stat-card { padding: 15px; border-radius: 10px; text-align: center; }
    .stat-card.tickets { background: linear-gradient(135deg, #a8e6cf, #88d8b0); }
    .stat-card.ads { background: linear-gradient(135deg, #ffd93d, #f9c80e); }
    .stat-card.paid { background: linear-gradient(135deg, #6bcb77, #4ade80); }
    .stat-card.rest { background: linear-gradient(135deg, #ff6b6b, #f87171); }
    .stat-card.total { background: linear-gradient(135deg, #667eea, #764ba2); color: white; }
    .stat-label { font-size: 0.75rem; text-transform: uppercase; font-weight: 600; opacity: 0.8; margin-bottom: 5px; }
    .stat-value { font-size: 1.1rem; font-weight: 700; }
    .stat-card.total .stat-label, .stat-card.total .stat-value { color: white; }
    
    .filter-row { display: flex; gap: 10px; align-items: center; margin-bottom: 20px; }
    .filter-row input { padding: 8px 12px; border: 1px solid #ddd; border-radius: 6px; }
    .btn-apply { padding: 8px 16px; background: #3498db; color: white; border: none; border-radius: 6px; font-weight: 600; cursor: pointer; }
    .btn-apply:hover { background: #2980b9; }
    
    .content-card { background: white; border-radius: 12px; box-shadow: 0 2px 10px rgba(0,0,0,0.06); overflow: hidden; }
    .card-head { padding: 15px 20px; background: linear-gradient(135deg, #2c3e50, #34495e); color: white; }
    .card-head h3 { margin: 0; font-size: 1rem; display: flex; align-items: center; gap: 8px; }
    
    .instance-table { width: 100%; border-collapse: collapse; }
    .instance-table thead th { background: #f8f9fa; padding: 12px 10px; text-align: left; font-weight: 600; color: #495057; font-size: 0.75rem; text-transform: uppercase; border-bottom: 2px solid #dee2e6; }
    .instance-table tbody td { padding: 12px 10px; border-bottom: 1px solid #eee; vertical-align: middle; font-size: 0.9rem; }
    .instance-table tbody tr:hover { background: #f8f9fa; }
    
    .amt { font-weight: 600; }
    .amt.tickets { color: #27ae60; }
    .amt.ads { color: #f39c12; }
    .amt.paid { color: #27ae60; }
    .amt.rest { color: #e74c3c; }
    .amt.total { color: #8e44ad; font-weight: 700; }
    
    .progress-bar-sm { width: 100%; height: 4px; background: #eee; border-radius: 5px; margin-top: 4px; overflow: hidden; }
    .progress-bar-sm .fill { height: 100%; background: #27ae60; border-radius: 5px; }
    
    .btn-diffusion { padding: 5px 10px; background: #ebf5fb; color: #3498db; border: none; border-radius: 6px; font-size: 0.75rem; font-weight: 600; text-decoration: none; }
    .btn-diffusion:hover { background: #3498db; color: white; }
    
    .empty-state { text-align: center; padding: 40px 20px; color: #95a5a6; }
    .empty-state i { font-size: 2.5rem; margin-bottom: 10px; opacity: 0.5; }
</style>

<div class="details-container">
    <div class="page-header">
        <h2><i class="fas fa-plane"></i> Détails du Vol</h2>
        <a href="${pageContext.request.contextPath}/vols" class="btn-back"><i class="fas fa-arrow-left"></i> Retour</a>
    </div>
    
    <div class="vol-info">
        <div class="vol-title">
            <i class="fas fa-plane-departure"></i> ${vol.numeroVol}
            <span class="route-badge">${vol.aeroportOrigine != null ? vol.aeroportOrigine.nomAeroport : '—'} <i class="fas fa-long-arrow-alt-right"></i> ${vol.aeroportDestination != null ? vol.aeroportDestination.nomAeroport : '—'}</span>
        </div>
        
        <div class="stats-grid">
            <div class="stat-card tickets">
                <div class="stat-label">CA Billets</div>
                <div class="stat-value"><fmt:formatNumber value="${totalTickets}" type="number" groupingUsed="true"/> MGA</div>
            </div>
            <div class="stat-card ads">
                <div class="stat-label">CA Publicité</div>
                <div class="stat-value"><fmt:formatNumber value="${totalAds}" type="number" groupingUsed="true"/> MGA</div>
            </div>
            <div class="stat-card paid">
                <div class="stat-label">Pub Payé</div>
                <div class="stat-value"><fmt:formatNumber value="${totalAdsPaid}" type="number" groupingUsed="true"/> MGA</div>
            </div>
            <div class="stat-card rest">
                <div class="stat-label">Pub Reste</div>
                <div class="stat-value"><fmt:formatNumber value="${totalAdsRest}" type="number" groupingUsed="true"/> MGA</div>
            </div>
            <div class="stat-card total">
                <div class="stat-label">Total CA</div>
                <div class="stat-value"><fmt:formatNumber value="${totalCA}" type="number" groupingUsed="true"/> MGA</div>
            </div>
        </div>
        
        <form method="get" action="${pageContext.request.contextPath}/vols/details/${vol.id}" class="filter-row">
            <label><i class="fas fa-filter"></i> Filtrer par date :</label>
            <input type="date" name="date" value="${filterDate}" />
            <button type="submit" class="btn-apply"><i class="fas fa-check"></i> Appliquer</button>
        </form>
    </div>
    
    <div class="content-card">
        <div class="card-head"><h3><i class="fas fa-calendar-alt"></i> Instances de Vol</h3></div>
        <c:choose>
            <c:when test="${not empty volInstances}">
                <table class="instance-table">
                    <thead>
                        <tr>
                            <th>#ID</th>
                            <th>Avion</th>
                            <th>Date</th>
                            <th>Heure</th>
                            <th>Tickets</th>
                            <th>Pub Total</th>
                            <th>Pub Payé</th>
                            <th>Pub Reste</th>
                            <th>Total CA</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="vi" items="${volInstances}">
                            <c:set var="pct" value="${vi.ads > 0 ? (vi.adsPaid / vi.ads * 100) : 0}"/>
                            <tr>
                                <td><strong>#${vi.id}</strong></td>
                                <td>${vi.avion != null ? vi.avion.codeAvion : '—'}</td>
                                <td><fmt:formatDate value="${vi.dateDepartDate}" pattern="dd/MM/yyyy"/></td>
                                <td><fmt:formatDate value="${vi.dateDepartDate}" pattern="HH:mm"/></td>
                                <td class="amt tickets"><fmt:formatNumber value="${vi.tickets}" type="number" groupingUsed="true"/> MGA</td>
                                <td class="amt ads"><fmt:formatNumber value="${vi.ads}" type="number" groupingUsed="true"/> MGA</td>
                                <td class="amt paid">
                                    <fmt:formatNumber value="${vi.adsPaid}" type="number" groupingUsed="true"/> MGA
                                    <div class="progress-bar-sm"><div class="fill" style="width:${pct}%"></div></div>
                                </td>
                                <td class="amt rest"><fmt:formatNumber value="${vi.adsRest}" type="number" groupingUsed="true"/> MGA</td>
                                <td class="amt total"><fmt:formatNumber value="${vi.total}" type="number" groupingUsed="true"/> MGA</td>
                                <td><a href="${pageContext.request.contextPath}/publicites/diffusions?volInstanceId=${vi.id}&amp;year=${vi.dateDepartYear != null ? vi.dateDepartYear : ''}&amp;month=${vi.dateDepartMonth != null ? vi.dateDepartMonth : ''}" class="btn-diffusion"><i class="fas fa-ad"></i> Diffusions</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <i class="fas fa-calendar-times"></i>
                    <p>Aucune instance de vol pour ce vol (ou pas de données pour la période sélectionnée).</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>