<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<style>
    .details-container {
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
        color: #27ae60;
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
    
    /* Info Card */
    .info-card {
        background: white;
        border-radius: 16px;
        box-shadow: 0 4px 20px rgba(0,0,0,0.08);
        margin-bottom: 25px;
        overflow: hidden;
    }
    .info-card-header {
        padding: 20px 25px;
        background: linear-gradient(135deg, #27ae60 0%, #2ecc71 100%);
        color: white;
    }
    .info-card-header h3 {
        margin: 0;
        font-size: 1.2rem;
        display: flex;
        align-items: center;
        gap: 10px;
    }
    .info-card-body {
        padding: 25px;
    }
    .info-grid {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 20px;
    }
    .info-item {
        text-align: center;
        padding: 15px;
        background: #f8f9fa;
        border-radius: 12px;
    }
    .info-label {
        font-size: 0.85rem;
        color: #7f8c8d;
        text-transform: uppercase;
        letter-spacing: 0.5px;
        margin-bottom: 8px;
    }
    .info-value {
        font-size: 1.3rem;
        font-weight: 700;
        color: #2c3e50;
    }
    .info-value.green { color: #27ae60; }
    .info-value.blue { color: #3498db; }
    
    /* Details Table */
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
    
    .details-table {
        width: 100%;
        border-collapse: separate;
        border-spacing: 0;
    }
    .details-table thead th {
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
    .details-table tbody tr {
        transition: all 0.2s;
    }
    .details-table tbody tr:hover {
        background: #f8f9fa;
    }
    .details-table tbody td {
        padding: 16px 15px;
        border-bottom: 1px solid #eee;
        vertical-align: middle;
    }
    
    .vol-info {
        display: flex;
        flex-direction: column;
        gap: 4px;
    }
    .vol-route {
        font-weight: 600;
        color: #2c3e50;
    }
    .vol-date {
        font-size: 0.85rem;
        color: #7f8c8d;
    }
    
    .badge-diffusions {
        display: inline-block;
        padding: 6px 14px;
        background: linear-gradient(135deg, #9b59b6 0%, #8e44ad 100%);
        color: white;
        border-radius: 20px;
        font-size: 0.85rem;
        font-weight: 600;
    }
    
    .amount {
        font-weight: 600;
        font-size: 0.95rem;
    }
    .amount.total { color: #3498db; }
    .amount.paid { color: #27ae60; }
    .amount.rest { color: #e74c3c; }
    
    .percentage-badge {
        display: inline-block;
        padding: 6px 12px;
        background: linear-gradient(135deg, #f39c12 0%, #e67e22 100%);
        color: white;
        border-radius: 20px;
        font-size: 0.8rem;
        font-weight: 600;
    }
    
    /* Total Row */
    .details-table tfoot td {
        padding: 18px 15px;
        background: #2c3e50;
        color: white;
        font-weight: 600;
        font-size: 1rem;
    }
    .details-table tfoot td:first-child {
        border-radius: 0 0 0 12px;
    }
    .details-table tfoot td:last-child {
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
        .info-grid { grid-template-columns: repeat(2, 1fr); }
    }
    @media (max-width: 768px) {
        .info-grid { grid-template-columns: 1fr; }
    }
</style>

<div class="details-container">
    <!-- Page Header -->
    <div class="page-header">
        <h2><i class="fas fa-receipt"></i> Détails du Paiement #${paiement.id}</h2>
        <a href="${pageContext.request.contextPath}/publicites/paiements?societeId=${paiement.societe.id}" class="btn-back">
            <i class="fas fa-arrow-left"></i> Retour aux paiements
        </a>
    </div>
    
    <!-- Paiement Info -->
    <div class="info-card">
        <div class="info-card-header">
            <h3><i class="fas fa-info-circle"></i> Informations du Paiement</h3>
        </div>
        <div class="info-card-body">
            <div class="info-grid">
                <div class="info-item">
                    <div class="info-label">Société</div>
                    <div class="info-value">${paiement.societe.nomSociete}</div>
                </div>
                <div class="info-item">
                    <div class="info-label">Date du paiement</div>
                    <div class="info-value">
                        <fmt:parseDate value="${paiement.datePaiement}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
                        <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy"/>
                    </div>
                </div>
                <div class="info-item">
                    <div class="info-label">Montant Total Payé</div>
                    <div class="info-value green">
                        <fmt:formatNumber value="${paiement.montant}" type="number" groupingUsed="true"/> MGA
                    </div>
                </div>
                <div class="info-item">
                    <div class="info-label">Méthode / Caisse</div>
                    <div class="info-value blue">${paiement.methodePaiement.nomMethode}</div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Details Table -->
    <div class="content-card">
        <div class="card-header">
            <h3><i class="fas fa-list-alt"></i> Répartition Proportionnelle par Vol Instance</h3>
        </div>
        <div class="card-body">
            <c:if test="${not empty details}">
                <table class="details-table">
                    <thead>
                        <tr>
                            <th>Vol Instance</th>
                            <th>Nb Diffusions</th>
                            <th>Montant Diffusions</th>
                            <th>Déjà Payé (avant)</th>
                            <th>Montant Payé (ce paiement)</th>
                            <th>Reste à Payer</th>
                            <th>% Appliqué</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="sumDiffusions" value="0"/>
                        <c:set var="sumNbDiff" value="0"/>
                        <c:set var="sumDejaPaye" value="0"/>
                        <c:set var="sumPaye" value="0"/>
                        <c:set var="sumReste" value="0"/>
                        
                        <c:forEach var="detail" items="${details}">
                            <c:set var="sumDiffusions" value="${sumDiffusions + detail.montantTotalDiffusions}"/>
                            <c:set var="sumNbDiff" value="${sumNbDiff + detail.nombreDiffusions}"/>
                            <c:set var="sumDejaPaye" value="${sumDejaPaye + (detail.montantDejaPaye != null ? detail.montantDejaPaye : 0)}"/>
                            <c:set var="sumPaye" value="${sumPaye + detail.montantPaye}"/>
                            <c:set var="sumReste" value="${sumReste + detail.resteAPayer}"/>
                            
                            <tr>
                                <td>
                                    <div class="vol-info">
                                        <span class="vol-route">
                                            <c:if test="${detail.volInstance.vol != null}">
                                                ${detail.volInstance.vol.aeroportOrigine.nomAeroport} → ${detail.volInstance.vol.aeroportDestination.nomAeroport}
                                            </c:if>
                                            <c:if test="${detail.volInstance.vol == null}">
                                                Instance #${detail.volInstance.id}
                                            </c:if>
                                        </span>
                                        <span class="vol-date">
                                            <i class="far fa-calendar-alt"></i>
                                            <c:if test="${detail.volInstance.dateDepart != null}">
                                                <fmt:parseDate value="${detail.volInstance.dateDepart}" pattern="yyyy-MM-dd'T'HH:mm" var="depDate" type="both"/>
                                                <fmt:formatDate value="${depDate}" pattern="dd/MM/yyyy HH:mm"/>
                                            </c:if>
                                        </span>
                                    </div>
                                </td>
                                <td>
                                    <span class="badge-diffusions">${detail.nombreDiffusions} diffusions</span>
                                </td>
                                <td>
                                    <span class="amount total">
                                        <fmt:formatNumber value="${detail.montantTotalDiffusions}" type="number" groupingUsed="true"/> MGA
                                    </span>
                                </td>
                                <td>
                                    <span class="amount paid">
                                        <fmt:formatNumber value="${detail.montantDejaPaye != null ? detail.montantDejaPaye : 0}" type="number" groupingUsed="true"/> MGA
                                    </span>
                                </td>
                                <td>
                                    <span class="amount paid">
                                        +<fmt:formatNumber value="${detail.montantPaye}" type="number" groupingUsed="true"/> MGA
                                    </span>
                                </td>
                                <td>
                                    <span class="amount ${detail.resteAPayer > 0 ? 'rest' : 'paid'}">
                                        <fmt:formatNumber value="${detail.resteAPayer}" type="number" groupingUsed="true"/> MGA
                                    </span>
                                </td>
                                <td>
                                    <span class="percentage-badge">
                                        <fmt:formatNumber value="${detail.pourcentageApplique}" type="number" maxFractionDigits="2"/>%
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td><strong>TOTAUX</strong></td>
                            <td>${sumNbDiff} diff.</td>
                            <td><fmt:formatNumber value="${sumDiffusions}" type="number" groupingUsed="true"/> MGA</td>
                            <td><fmt:formatNumber value="${sumDejaPaye}" type="number" groupingUsed="true"/> MGA</td>
                            <td><fmt:formatNumber value="${sumPaye}" type="number" groupingUsed="true"/> MGA</td>
                            <td><fmt:formatNumber value="${sumReste}" type="number" groupingUsed="true"/> MGA</td>
                            <td>—</td>
                        </tr>
                    </tfoot>
                </table>
            </c:if>
            <c:if test="${empty details}">
                <div class="empty-state">
                    <i class="fas fa-inbox"></i>
                    <p>Aucun détail de répartition disponible pour ce paiement</p>
                </div>
            </c:if>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
