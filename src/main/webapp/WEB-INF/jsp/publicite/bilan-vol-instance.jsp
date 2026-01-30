<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<style>
    .bilan-container {
        padding: 20px;
        background: #f4f6f9;
        min-height: 100vh;
    }
    
    .page-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 30px;
        padding-bottom: 20px;
        border-bottom: 2px solid #e0e0e0;
    }
    .page-header h2 {
        margin: 0;
        color: #2c3e50;
        font-size: 1.8rem;
        display: flex;
        align-items: center;
        gap: 12px;
    }
    .page-header h2 i {
        color: #9b59b6;
    }
    
    .btn-back {
        padding: 10px 20px;
        background: #ecf0f1;
        color: #2c3e50;
        border: none;
        border-radius: 8px;
        text-decoration: none;
        font-weight: 500;
        transition: all 0.3s;
        display: flex;
        align-items: center;
        gap: 8px;
    }
    .btn-back:hover {
        background: #3498db;
        color: white;
    }
    
    /* Societe Info */
    .societe-info {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        padding: 25px 30px;
        border-radius: 16px;
        margin-bottom: 25px;
        display: flex;
        align-items: center;
        gap: 20px;
    }
    .societe-avatar {
        width: 70px;
        height: 70px;
        background: rgba(255,255,255,0.2);
        border-radius: 16px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 2rem;
        font-weight: 700;
    }
    .societe-details h3 {
        margin: 0 0 5px 0;
        font-size: 1.5rem;
    }
    .societe-details p {
        margin: 0;
        opacity: 0.8;
    }
    
    /* Stats Row */
    .stats-row {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 20px;
        margin-bottom: 30px;
    }
    .stat-card {
        background: white;
        border-radius: 16px;
        padding: 25px;
        box-shadow: 0 4px 20px rgba(0,0,0,0.08);
        position: relative;
        overflow: hidden;
    }
    .stat-card::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 5px;
        height: 100%;
    }
    .stat-card.purple::before { background: linear-gradient(180deg, #9b59b6, #8e44ad); }
    .stat-card.blue::before { background: linear-gradient(180deg, #3498db, #2980b9); }
    .stat-card.green::before { background: linear-gradient(180deg, #27ae60, #1e8449); }
    .stat-card.orange::before { background: linear-gradient(180deg, #e74c3c, #c0392b); }
    
    .stat-card .stat-icon {
        width: 50px;
        height: 50px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.4rem;
        margin-bottom: 15px;
    }
    .stat-card.purple .stat-icon { background: #f5eef8; color: #9b59b6; }
    .stat-card.blue .stat-icon { background: #ebf5fb; color: #3498db; }
    .stat-card.green .stat-icon { background: #e8f8f5; color: #27ae60; }
    .stat-card.orange .stat-icon { background: #fdedec; color: #e74c3c; }
    
    .stat-card .stat-label {
        font-size: 0.85rem;
        color: #7f8c8d;
        text-transform: uppercase;
        letter-spacing: 1px;
        margin-bottom: 5px;
    }
    .stat-card .stat-value {
        font-size: 1.5rem;
        font-weight: 700;
        color: #2c3e50;
    }
    
    /* Content Card */
    .content-card {
        background: white;
        border-radius: 16px;
        box-shadow: 0 4px 20px rgba(0,0,0,0.08);
        overflow: hidden;
    }
    .card-header {
        padding: 20px 25px;
        background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
        color: white;
    }
    .card-header h3 {
        margin: 0;
        font-size: 1.1rem;
        display: flex;
        align-items: center;
        gap: 10px;
    }
    .card-body {
        padding: 25px;
    }
    
    /* Bilan Table */
    .bilan-table {
        width: 100%;
        border-collapse: separate;
        border-spacing: 0;
    }
    .bilan-table thead th {
        background: #f8f9fa;
        padding: 15px;
        text-align: left;
        font-weight: 600;
        color: #495057;
        font-size: 0.85rem;
        text-transform: uppercase;
        letter-spacing: 0.5px;
        border-bottom: 2px solid #dee2e6;
    }
    .bilan-table tbody tr {
        transition: all 0.2s;
    }
    .bilan-table tbody tr:hover {
        background: #f8f9fa;
    }
    .bilan-table tbody td {
        padding: 18px 15px;
        border-bottom: 1px solid #eee;
        vertical-align: middle;
    }
    
    .vol-info {
        display: flex;
        align-items: center;
        gap: 15px;
    }
    .vol-icon {
        width: 50px;
        height: 50px;
        background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-size: 1.2rem;
    }
    .vol-details {
        display: flex;
        flex-direction: column;
        gap: 3px;
    }
    .vol-route {
        font-weight: 600;
        color: #2c3e50;
        font-size: 1rem;
    }
    .vol-date {
        font-size: 0.85rem;
        color: #7f8c8d;
    }
    .vol-avion {
        font-size: 0.8rem;
        color: #95a5a6;
    }
    
    .badge-diffusions {
        display: inline-block;
        padding: 8px 16px;
        background: linear-gradient(135deg, #9b59b6 0%, #8e44ad 100%);
        color: white;
        border-radius: 25px;
        font-size: 0.9rem;
        font-weight: 600;
    }
    
    .amount {
        font-weight: 600;
        font-size: 1rem;
    }
    .amount.total { color: #3498db; }
    .amount.paid { color: #27ae60; }
    .amount.rest { color: #e74c3c; }
    
    /* Progress Bar */
    .progress-bar-wrapper {
        width: 100%;
        height: 8px;
        background: #ecf0f1;
        border-radius: 10px;
        overflow: hidden;
        margin-top: 8px;
    }
    .progress-bar {
        height: 100%;
        background: linear-gradient(90deg, #27ae60, #2ecc71);
        border-radius: 10px;
        transition: width 0.5s ease;
    }
    
    /* Status Badge */
    .status-badge {
        display: inline-block;
        padding: 6px 14px;
        border-radius: 20px;
        font-size: 0.8rem;
        font-weight: 600;
    }
    .status-badge.complete {
        background: #d4edda;
        color: #155724;
    }
    .status-badge.partial {
        background: #fff3cd;
        color: #856404;
    }
    .status-badge.pending {
        background: #f8d7da;
        color: #721c24;
    }
    
    /* Total Row */
    .bilan-table tfoot td {
        padding: 18px 15px;
        background: #2c3e50;
        color: white;
        font-weight: 600;
        font-size: 1rem;
    }
    .bilan-table tfoot td:first-child {
        border-radius: 0 0 0 12px;
    }
    .bilan-table tfoot td:last-child {
        border-radius: 0 0 12px 0;
    }
    
    .empty-state {
        text-align: center;
        padding: 50px 20px;
        color: #95a5a6;
    }
    .empty-state i {
        font-size: 3rem;
        margin-bottom: 15px;
        opacity: 0.5;
    }
    
    @media (max-width: 1200px) {
        .stats-row { grid-template-columns: repeat(2, 1fr); }
    }
    @media (max-width: 768px) {
        .stats-row { grid-template-columns: 1fr; }
    }
</style>

<div class="bilan-container">
    <!-- Page Header -->
    <div class="page-header">
        <h2><i class="fas fa-plane-departure"></i> Bilan par Vol Instance</h2>
        <a href="${pageContext.request.contextPath}/publicites/paiements?societeId=${societe.id}" class="btn-back">
            <i class="fas fa-arrow-left"></i> Retour aux paiements
        </a>
    </div>
    
    <!-- Societe Info -->
    <div class="societe-info">
        <div class="societe-avatar">${fn:substring(societe.nomSociete, 0, 1)}</div>
        <div class="societe-details">
            <h3>${societe.nomSociete}</h3>
            <p>Bilan détaillé des diffusions et paiements par vol instance</p>
        </div>
    </div>
    
    <!-- Stats Row -->
    <div class="stats-row">
        <div class="stat-card purple">
            <div class="stat-icon"><i class="fas fa-broadcast-tower"></i></div>
            <div class="stat-label">Total Diffusions</div>
            <div class="stat-value">${totalNbDiffusions}</div>
        </div>
        <div class="stat-card blue">
            <div class="stat-icon"><i class="fas fa-file-invoice-dollar"></i></div>
            <div class="stat-label">Montant Total</div>
            <div class="stat-value"><fmt:formatNumber value="${totalDiffusions}" type="number" groupingUsed="true"/> MGA</div>
        </div>
        <div class="stat-card green">
            <div class="stat-icon"><i class="fas fa-check-double"></i></div>
            <div class="stat-label">Total Payé</div>
            <div class="stat-value"><fmt:formatNumber value="${totalPaye}" type="number" groupingUsed="true"/> MGA</div>
        </div>
        <div class="stat-card orange">
            <div class="stat-icon"><i class="fas fa-hourglass-half"></i></div>
            <div class="stat-label">Reste à Payer</div>
            <div class="stat-value"><fmt:formatNumber value="${totalReste}" type="number" groupingUsed="true"/> MGA</div>
        </div>
    </div>
    
    <!-- Bilan Table -->
    <div class="content-card">
        <div class="card-header">
            <h3><i class="fas fa-list-alt"></i> Détail par Vol Instance</h3>
        </div>
        <div class="card-body">
            <c:if test="${not empty bilanVolInstances}">
                <table class="bilan-table">
                    <thead>
                        <tr>
                            <th>Vol Instance</th>
                            <th>Nb Diffusions</th>
                            <th>Montant Total</th>
                            <th>Montant Payé</th>
                            <th>Reste à Payer</th>
                            <th>Progression</th>
                            <th>Statut</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="bilan" items="${bilanVolInstances}">
                            <c:set var="percentage" value="${bilan.montantTotal > 0 ? (bilan.montantPaye / bilan.montantTotal * 100) : 0}"/>
                            <tr>
                                <td>
                                    <div class="vol-info">
                                        <div class="vol-icon">
                                            <i class="fas fa-plane"></i>
                                        </div>
                                        <div class="vol-details">
                                            <span class="vol-route">
                                                <c:if test="${bilan.volInstance.vol != null}">
                                                    ${bilan.volInstance.vol.aeroportOrigine.nomAeroport} → ${bilan.volInstance.vol.aeroportDestination.nomAeroport}
                                                </c:if>
                                                <c:if test="${bilan.volInstance.vol == null}">
                                                    Instance #${bilan.volInstance.id}
                                                </c:if>
                                            </span>
                                            <span class="vol-date">
                                                <i class="far fa-calendar-alt"></i>
                                                <c:if test="${bilan.volInstance.dateDepart != null}">
                                                    <fmt:parseDate value="${bilan.volInstance.dateDepart}" pattern="yyyy-MM-dd'T'HH:mm" var="depDate" type="both"/>
                                                    <fmt:formatDate value="${depDate}" pattern="dd/MM/yyyy HH:mm"/>
                                                </c:if>
                                            </span>
                                            <span class="vol-avion">
                                                <c:if test="${bilan.volInstance.avion != null}">
                                                    <i class="fas fa-plane-departure"></i> ${bilan.volInstance.avion.codeAvion}
                                                </c:if>
                                            </span>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <span class="badge-diffusions">${bilan.nombreDiffusions}</span>
                                </td>
                                <td>
                                    <span class="amount total">
                                        <fmt:formatNumber value="${bilan.montantTotal}" type="number" groupingUsed="true"/> MGA
                                    </span>
                                </td>
                                <td>
                                    <span class="amount paid">
                                        <fmt:formatNumber value="${bilan.montantPaye}" type="number" groupingUsed="true"/> MGA
                                    </span>
                                </td>
                                <td>
                                    <span class="amount ${bilan.resteAPayer > 0 ? 'rest' : 'paid'}">
                                        <fmt:formatNumber value="${bilan.resteAPayer}" type="number" groupingUsed="true"/> MGA
                                    </span>
                                </td>
                                <td>
                                    <div class="progress-bar-wrapper">
                                        <div class="progress-bar" style="width: ${percentage}%"></div>
                                    </div>
                                    <small style="color: #7f8c8d;"><fmt:formatNumber value="${percentage}" maxFractionDigits="1"/>%</small>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${bilan.resteAPayer <= 0}">
                                            <span class="status-badge complete"><i class="fas fa-check"></i> Soldé</span>
                                        </c:when>
                                        <c:when test="${bilan.montantPaye > 0}">
                                            <span class="status-badge partial"><i class="fas fa-clock"></i> Partiel</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status-badge pending"><i class="fas fa-exclamation-circle"></i> Non payé</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td><strong>TOTAUX</strong></td>
                            <td>${totalNbDiffusions}</td>
                            <td><fmt:formatNumber value="${totalDiffusions}" type="number" groupingUsed="true"/> MGA</td>
                            <td><fmt:formatNumber value="${totalPaye}" type="number" groupingUsed="true"/> MGA</td>
                            <td><fmt:formatNumber value="${totalReste}" type="number" groupingUsed="true"/> MGA</td>
                            <td colspan="2">
                                <c:set var="globalPct" value="${totalDiffusions > 0 ? (totalPaye / totalDiffusions * 100) : 0}"/>
                                <fmt:formatNumber value="${globalPct}" maxFractionDigits="1"/>% payé
                            </td>
                        </tr>
                    </tfoot>
                </table>
            </c:if>
            <c:if test="${empty bilanVolInstances}">
                <div class="empty-state">
                    <i class="fas fa-plane-slash"></i>
                    <p>Aucune diffusion trouvée pour cette société</p>
                </div>
            </c:if>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
