<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<style>
    .paiements-container {
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
    .header-nav {
        display: flex;
        gap: 10px;
    }
    .header-nav .btn {
        padding: 10px 20px;
        border-radius: 8px;
        font-weight: 500;
        text-decoration: none;
        transition: all 0.3s;
    }
    .btn-nav {
        background: #ecf0f1;
        color: #2c3e50;
        border: none;
    }
    .btn-nav:hover {
        background: #3498db;
        color: white;
    }
    
    /* Stats Cards */
    .stats-row {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
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
        font-size: 1.6rem;
        font-weight: 700;
        color: #2c3e50;
    }
    
    /* Main Content Grid */
    .content-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 25px;
    }
    
    /* Cards */
    .content-card {
        background: white;
        border-radius: 16px;
        box-shadow: 0 4px 20px rgba(0,0,0,0.08);
        overflow: hidden;
    }
    .content-card.full-width {
        grid-column: 1 / -1;
    }
    .card-header {
        padding: 20px 25px;
        background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
        color: white;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .card-header h3 {
        margin: 0;
        font-size: 1.1rem;
        font-weight: 600;
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
    .bilan-table thead th:last-child {
        text-align: center;
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
    .societe-name {
        font-weight: 600;
        color: #2c3e50;
        display: flex;
        align-items: center;
        gap: 10px;
    }
    .societe-name .avatar {
        width: 40px;
        height: 40px;
        border-radius: 10px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: 700;
        font-size: 1rem;
    }
    .amount {
        font-weight: 600;
        font-size: 0.95rem;
    }
    .amount.positive { color: #27ae60; }
    .amount.negative { color: #e74c3c; }
    .amount.neutral { color: #7f8c8d; }
    
    .actions-cell {
        text-align: center;
        display: flex;
        gap: 8px;
        justify-content: center;
    }
    .btn-icon {
        width: 38px;
        height: 38px;
        border-radius: 10px;
        border: none;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        transition: all 0.3s;
        text-decoration: none;
    }
    .btn-icon.blue { background: #ebf5fb; color: #3498db; }
    .btn-icon.blue:hover { background: #3498db; color: white; }
    .btn-icon.green { background: #e8f8f5; color: #27ae60; }
    .btn-icon.green:hover { background: #27ae60; color: white; }
    
    /* Payment Form */
    .payment-form-wrapper {
        display: none;
    }
    .payment-form-wrapper.active {
        display: block;
    }
    .form-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
        padding-bottom: 15px;
        border-bottom: 2px solid #eee;
    }
    .form-header h4 {
        margin: 0;
        color: #2c3e50;
        display: flex;
        align-items: center;
        gap: 10px;
    }
    .reste-badge {
        background: linear-gradient(135deg, #ff6b6b 0%, #ee5a5a 100%);
        color: white;
        padding: 8px 16px;
        border-radius: 20px;
        font-weight: 600;
        font-size: 0.9rem;
    }
    .form-grid {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 20px;
        margin-bottom: 20px;
    }
    .form-group label {
        display: block;
        font-weight: 600;
        color: #495057;
        margin-bottom: 8px;
        font-size: 0.9rem;
    }
    .form-group input,
    .form-group select {
        width: 100%;
        padding: 12px 15px;
        border: 2px solid #e9ecef;
        border-radius: 10px;
        font-size: 1rem;
        transition: all 0.3s;
        background: #fff;
    }
    .form-group input:focus,
    .form-group select:focus {
        outline: none;
        border-color: #3498db;
        box-shadow: 0 0 0 4px rgba(52, 152, 219, 0.1);
    }
    .form-actions {
        display: flex;
        gap: 12px;
    }
    .btn-submit {
        padding: 12px 30px;
        background: linear-gradient(135deg, #27ae60 0%, #2ecc71 100%);
        color: white;
        border: none;
        border-radius: 10px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s;
        display: flex;
        align-items: center;
        gap: 8px;
    }
    .btn-submit:hover {
        transform: translateY(-2px);
        box-shadow: 0 5px 20px rgba(39, 174, 96, 0.4);
    }
    .btn-cancel {
        padding: 12px 25px;
        background: #ecf0f1;
        color: #7f8c8d;
        border: none;
        border-radius: 10px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s;
    }
    .btn-cancel:hover {
        background: #bdc3c7;
        color: #2c3e50;
    }
    
    /* History Table */
    .history-table {
        width: 100%;
        border-collapse: separate;
        border-spacing: 0;
    }
    .history-table thead th {
        background: #f8f9fa;
        padding: 14px 15px;
        text-align: left;
        font-weight: 600;
        color: #495057;
        font-size: 0.8rem;
        text-transform: uppercase;
        letter-spacing: 0.5px;
        border-bottom: 2px solid #dee2e6;
    }
    .history-table tbody tr {
        transition: all 0.2s;
    }
    .history-table tbody tr:hover {
        background: #f8f9fa;
    }
    .history-table tbody td {
        padding: 16px 15px;
        border-bottom: 1px solid #eee;
        vertical-align: middle;
        font-size: 0.95rem;
    }
    .date-cell {
        color: #7f8c8d;
        font-size: 0.9rem;
    }
    .date-cell i {
        margin-right: 6px;
    }
    .method-badge {
        display: inline-block;
        padding: 6px 14px;
        background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
        color: white;
        border-radius: 20px;
        font-size: 0.8rem;
        font-weight: 500;
    }
    .reference-text {
        color: #95a5a6;
        font-style: italic;
    }
    
    /* Empty State */
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
    .empty-state p {
        margin: 0;
        font-size: 1rem;
    }
    
    /* Alerts */
    .alert {
        padding: 15px 20px;
        border-radius: 10px;
        margin-bottom: 20px;
        display: flex;
        align-items: center;
        gap: 12px;
    }
    .alert-success {
        background: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
    }
    .alert-danger {
        background: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
    }
    
    @media (max-width: 1200px) {
        .content-grid { grid-template-columns: 1fr; }
        .form-grid { grid-template-columns: 1fr; }
        .stats-row { grid-template-columns: 1fr; }
    }
</style>

<div class="paiements-container">
    <!-- Page Header -->
    <div class="page-header">
        <h2><i class="fas fa-money-bill-wave"></i> Gestion des Paiements</h2>
        <div class="header-nav">
            <a href="${pageContext.request.contextPath}/publicites/societes" class="btn btn-nav">
                <i class="fas fa-building"></i> Sociétés
            </a>
            <a href="${pageContext.request.contextPath}/publicites/videos" class="btn btn-nav">
                <i class="fas fa-video"></i> Vidéos
            </a>
            <a href="${pageContext.request.contextPath}/publicites/diffusions" class="btn btn-nav">
                <i class="fas fa-broadcast-tower"></i> Diffusions
            </a>
        </div>
    </div>
    
    <!-- Alerts -->
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">
            <i class="fas fa-check-circle"></i> ${successMessage}
        </div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">
            <i class="fas fa-exclamation-circle"></i> ${errorMessage}
        </div>
    </c:if>
    
    <!-- Stats Cards -->
    <c:set var="totalDiff" value="0"/>
    <c:set var="totalPaye" value="0"/>
    <c:set var="totalReste" value="0"/>
    <c:forEach var="bilan" items="${bilans}">
        <c:set var="totalDiff" value="${totalDiff + bilan.totalDiffusions}"/>
        <c:set var="totalPaye" value="${totalPaye + bilan.totalPaiements}"/>
        <c:set var="totalReste" value="${totalReste + bilan.resteAPayer}"/>
    </c:forEach>
    
    <div class="stats-row">
        <div class="stat-card blue">
            <div class="stat-icon"><i class="fas fa-broadcast-tower"></i></div>
            <div class="stat-label">Total Diffusions</div>
            <div class="stat-value"><fmt:formatNumber value="${totalDiff}" type="number" groupingUsed="true"/> MGA</div>
        </div>
        <div class="stat-card green">
            <div class="stat-icon"><i class="fas fa-check-double"></i></div>
            <div class="stat-label">Total Encaissé</div>
            <div class="stat-value"><fmt:formatNumber value="${totalPaye}" type="number" groupingUsed="true"/> MGA</div>
        </div>
        <div class="stat-card orange">
            <div class="stat-icon"><i class="fas fa-hourglass-half"></i></div>
            <div class="stat-label">Reste à Percevoir</div>
            <div class="stat-value"><fmt:formatNumber value="${totalReste}" type="number" groupingUsed="true"/> MGA</div>
        </div>
    </div>
    
    <!-- Main Content -->
    <div class="content-grid">
        <!-- Bilan Card -->
        <div class="content-card">
            <div class="card-header">
                <h3><i class="fas fa-chart-pie"></i> Bilan par Société</h3>
            </div>
            <div class="card-body">
                <table class="bilan-table">
                    <thead>
                        <tr>
                            <th>Société</th>
                            <th>Diffusions</th>
                            <th>Payé</th>
                            <th>Reste</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="bilan" items="${bilans}">
                            <tr>
                                <td>
                                    <div class="societe-name">
                                        <div class="avatar">${fn:substring(bilan.societe.nomSociete, 0, 1)}</div>
                                        <span>${bilan.societe.nomSociete}</span>
                                    </div>
                                </td>
                                <td>
                                    <span class="amount neutral">
                                        <fmt:formatNumber value="${bilan.totalDiffusions}" type="number" groupingUsed="true"/>
                                    </span>
                                </td>
                                <td>
                                    <span class="amount positive">
                                        <fmt:formatNumber value="${bilan.totalPaiements}" type="number" groupingUsed="true"/>
                                    </span>
                                </td>
                                <td>
                                    <span class="amount ${bilan.resteAPayer > 0 ? 'negative' : 'positive'}">
                                        <fmt:formatNumber value="${bilan.resteAPayer}" type="number" groupingUsed="true"/>
                                    </span>
                                </td>
                                <td>
                                    <div class="actions-cell">
                                        <a href="${pageContext.request.contextPath}/publicites/paiements?societeId=${bilan.societe.id}" 
                                           class="btn-icon blue" title="Historique">
                                            <i class="fas fa-history"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/publicites/paiements/bilan-vol/${bilan.societe.id}" 
                                           class="btn-icon" style="background: #f5eef8; color: #9b59b6;" title="Bilan par Vol">
                                            <i class="fas fa-plane"></i>
                                        </a>
                                        <c:if test="${bilan.resteAPayer > 0}">
                                            <button type="button" class="btn-icon green" 
                                                    onclick="openPaymentForm(${bilan.societe.id}, '${bilan.societe.nomSociete}', ${bilan.resteAPayer})"
                                                    title="Nouveau paiement">
                                                <i class="fas fa-plus"></i>
                                            </button>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty bilans}">
                            <tr>
                                <td colspan="5">
                                    <div class="empty-state">
                                        <i class="fas fa-building"></i>
                                        <p>Aucune société avec des diffusions</p>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
        
        <!-- Payment Form Card -->
        <div class="content-card">
            <div class="card-header">
                <h3><i class="fas fa-credit-card"></i> Nouveau Paiement</h3>
            </div>
            <div class="card-body">
                <div id="paymentFormSection" class="payment-form-wrapper">
                    <div class="form-header">
                        <h4><i class="fas fa-building"></i> <span id="societeName"></span></h4>
                        <div class="reste-badge">
                            Reste: <span id="resteDisplay"></span> MGA
                        </div>
                    </div>
                    <form method="post" action="${pageContext.request.contextPath}/publicites/paiements/save">
                        <input type="hidden" id="societeIdInput" name="societeId">
                        <div class="form-grid">
                            <div class="form-group">
                                <label><i class="fas fa-calendar-alt"></i> Date du paiement</label>
                                <input type="date" id="datePaiement" name="datePaiement" required>
                            </div>
                            <div class="form-group">
                                <label><i class="fas fa-coins"></i> Montant (MGA)</label>
                                <input type="number" id="montant" name="montant" required min="1" placeholder="0">
                            </div>
                            <div class="form-group">
                                <label><i class="fas fa-cash-register"></i> Caisse</label>
                                <select id="caisseId" name="caisseId" required>
                                    <option value="">-- Choisir --</option>
                                    <c:forEach var="caisse" items="${caisses}">
                                        <option value="${caisse.id}">
                                            ${caisse.nomCaisse} (${caisse.methodePaiement.nomMethode})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label><i class="fas fa-file-alt"></i> Référence</label>
                                <input type="text" id="reference" name="reference" placeholder="Ex: VIR-001">
                            </div>
                        </div>
                        <div class="form-actions">
                            <button type="submit" class="btn-submit">
                                <i class="fas fa-save"></i> Enregistrer
                            </button>
                            <button type="button" class="btn-cancel" onclick="closePaymentForm()">
                                <i class="fas fa-times"></i> Annuler
                            </button>
                        </div>
                    </form>
                </div>
                <div id="noSelectionMessage" class="empty-state">
                    <i class="fas fa-hand-pointer"></i>
                    <p>Sélectionnez une société pour enregistrer un paiement</p>
                </div>
            </div>
        </div>
        
        <!-- History Card -->
        <div class="content-card full-width">
            <div class="card-header">
                <h3>
                    <i class="fas fa-history"></i> Historique des Paiements
                </h3>
                <c:if test="${not empty selectedSocieteId}">
                    <a href="${pageContext.request.contextPath}/publicites/paiements" class="btn btn-nav" style="padding: 8px 16px; font-size: 0.85rem;">
                        <i class="fas fa-list"></i> Voir tous
                    </a>
                </c:if>
            </div>
            <div class="card-body">
                <table class="history-table">
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Société</th>
                            <th>Montant</th>
                            <th>Méthode</th>
                            <th>Caisse</th>
                            <th>Référence</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="paiement" items="${paiements}">
                            <tr>
                                <td class="date-cell">
                                    <i class="far fa-calendar-alt"></i>
                                    <fmt:parseDate value="${paiement.datePaiement}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
                                    <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm"/>
                                </td>
                                <td>
                                    <strong>${paiement.societe.nomSociete}</strong>
                                </td>
                                <td>
                                    <span class="amount positive">
                                        +<fmt:formatNumber value="${paiement.montant}" type="number" groupingUsed="true"/> MGA
                                    </span>
                                </td>
                                <td>
                                    <span class="method-badge">${paiement.methodePaiement.nomMethode}</span>
                                </td>
                                <td>${paiement.caisse.nomCaisse}</td>
                                <td>
                                    <span class="reference-text">${not empty paiement.reference ? paiement.reference : '—'}</span>
                                </td>
                                <td>
                                    <div class="actions-cell">
                                        <a href="${pageContext.request.contextPath}/publicites/paiements/details/${paiement.id}" 
                                           class="btn-icon blue" title="Voir la répartition">
                                            <i class="fas fa-receipt"></i>
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty paiements}">
                            <tr>
                                <td colspan="7">
                                    <div class="empty-state">
                                        <i class="fas fa-inbox"></i>
                                        <p>Aucun paiement enregistré</p>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script>
function openPaymentForm(societeId, societeName, reste) {
    document.getElementById('paymentFormSection').classList.add('active');
    document.getElementById('noSelectionMessage').style.display = 'none';
    document.getElementById('societeIdInput').value = societeId;
    document.getElementById('societeName').textContent = societeName;
    document.getElementById('resteDisplay').textContent = new Intl.NumberFormat('fr-FR').format(reste);
    document.getElementById('montant').max = reste;
    document.getElementById('montant').value = reste;
    // Pré-remplir avec la date du jour
    document.getElementById('datePaiement').value = new Date().toISOString().split('T')[0];
}

function closePaymentForm() {
    document.getElementById('paymentFormSection').classList.remove('active');
    document.getElementById('noSelectionMessage').style.display = 'block';
    document.getElementById('societeIdInput').value = '';
    document.getElementById('montant').value = '';
    document.getElementById('reference').value = '';
    document.getElementById('datePaiement').value = '';
}
</script>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
