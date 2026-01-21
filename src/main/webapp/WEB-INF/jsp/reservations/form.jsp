<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Nouvelle Réservation</h3>
    </div>
    <div class="card-body">
        <form id="filterForm" method="get" action="${pageContext.request.contextPath}/reservations/create">
            <div class="form-row">
                <div style="width:30%">
                    <label>Date du vol</label>
                    <input type="date" name="date" class="form-control" value="${param.date}" />
                </div>
                <div style="width:30%">
                    <label>Aéroport départ</label>
                    <select name="departId" class="form-control">
                        <option value="">-- Choisir --</option>
                        <c:forEach var="a" items="${aeroports}">
                            <option value="${a.id}" ${param.departId == a.id ? 'selected' : ''}>${a.nomAeroport}</option>
                        </c:forEach>
                    </select>
                </div>
                <div style="width:30%">
                    <label>Aéroport arrivée</label>
                    <select name="destId" class="form-control">
                        <option value="">-- Choisir --</option>
                        <c:forEach var="a" items="${aeroports}">
                            <option value="${a.id}" ${param.destId == a.id ? 'selected' : ''}>${a.nomAeroport}</option>
                        </c:forEach>
                    </select>
                </div>
                <div style="width:10%; align-self:flex-end; margin-left:8px">
                    <button class="btn btn-primary">Rechercher</button>
                </div>
            </div>
        </form>

        <c:if test="${noFlights}">
            <div class="alert alert-warning">Aucun vol disponible pour la recherche indiquée.</div>
        </c:if>

        <c:if test="${not empty vols}">
            <div class="form-group mt-3">
                <label>Vol disponible</label>
                <form id="selectVolForm" method="get" action="${pageContext.request.contextPath}/reservations/create">
                    <input type="hidden" name="date" value="${param.date}" />
                    <input type="hidden" name="departId" value="${param.departId}" />
                    <input type="hidden" name="destId" value="${param.destId}" />
                    <select name="volId" onchange="this.form.submit()" class="form-control">
                        <c:forEach var="v" items="${vols}">
                            <option value="${v.id}" ${selectedVol != null && selectedVol.id == v.id ? 'selected' : ''}>
                                <c:out value="${v.vol != null ? v.vol.numeroVol : 'Vol'}"/> - <c:out value="${v.dateDepart}"/>
                            </option>
                        </c:forEach>
                    </select>
                </form>
            </div>

            <div class="row">
                <div style="width:60%">
                    <h4>Disposition des sièges</h4>
                    <div id="seatMap">
                        <c:forEach var="s" items="${seats}">
                            <c:if test="${not empty s.numero}">
                                <c:set var="status" value="${s.status}" />
                                <c:set var="selectable" value="${s.selectable}" />
                                <button class="seat-btn status-${fn:toLowerCase(status)}" data-id="${s.id}" data-status="${status}" data-classe="${s.classeId}" data-price="${s.price}" data-default-price="${s.price}" ${!selectable ? 'disabled' : ''}>
                                    <span class="seat-number">${s.numero}</span>
                                    <span class="seat-class">${s.classeNom}</span>
                                </button>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
                <div style="width:35%">
                    <h4>Détails du passager</h4>
                    <form method="post" action="${pageContext.request.contextPath}/reservations/save" id="reservationForm">
                        <input type="hidden" name="vol.id" value="${selectedVol.id}" />
                        <input type="hidden" name="siegeIds" id="siegeIds" />
                        <input type="hidden" name="classe.id" id="classeId" />
                        <input type="hidden" name="prix" id="totalPrix" />

                        <div class="form-group">
                            <label>Nom</label>
                            <input type="text" name="passager.nom" class="form-control" required />
                        </div>
                        <div class="form-group">
                            <label>Prénom</label>
                            <input type="text" name="passager.prenom" class="form-control" required />
                        </div>
                        <div class="form-group">
                            <label>Email</label>
                            <input type="email" name="passager.email" class="form-control" required />
                        </div>
                        <div class="form-group">
                            <label>Type de passager</label>
                            <select name="passager.typePassager.id" class="form-control" required>
                                <option value="">-- Choisir --</option>
                                <c:forEach var="t" items="${types}">
                                    <option value="${t.id}">${t.type}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>Catégorie passager</label>
                            <select name="passager.categoriePassager.id" id="categorieId" class="form-control">
                                <option value="">-- Choisir --</option>
                                <c:forEach var="c" items="${categories}">
                                    <option value="${c.id}">${c.categorie}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>Statut de la réservation</label>
                            <select name="statutReservation.id" class="form-control">
                                <option value="">-- Choisir --</option>
                                <c:forEach var="s" items="${statusList}">
                                    <option value="${s.id}">${s.nomStatus}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>Date de réservation</label>
                            <input type="datetime-local" name="dateReservation" class="form-control" />
                        </div>

                        <div class="form-group">
                            <label>Siège choisi</label>
                            <div id="chosenSeat">Aucun</div>
                        </div>

                        <div class="form-group">
                            <label>Prix total</label>
                            <div id="chosenPrice">0.00</div>
                        </div>

                        <div class="form-group mt-3">
                            <button type="submit" class="btn btn-success">Confirmer Réservation</button>
                            <a href="${pageContext.request.contextPath}/reservations" class="btn btn-secondary">Annuler</a>
                        </div>
                    </form>
                </div>
            </div>

            <!-- ========== SECTION RÉSERVATION MULTIPLE - PLEINE LARGEUR ========== -->
            <div class="multi-reservation-section">
                <div class="multi-reservation-header">
                    <div class="multi-header-icon">
                        <i class="fas fa-users"></i>
                    </div>
                    <div class="multi-header-text">
                        <h4>Réservation Multiple</h4>
                        <p>Attribution automatique des sièges - Ajoutez plusieurs passagers en une seule opération</p>
                    </div>
                </div>
                
                <form method="post" action="${pageContext.request.contextPath}/reservations/save-multiple" id="multiReservationForm">
                    <input type="hidden" name="volInstanceId" value="${selectedVol.id}" />

                    <div class="multi-form-grid">
                        <!-- Colonne Passager -->
                        <div class="multi-form-card">
                            <div class="card-section-title">
                                <i class="fas fa-user"></i> Informations Passager
                            </div>
                            <div class="form-group">
                                <label>Nom <span class="required">*</span></label>
                                <input type="text" name="passagerNom" class="form-control" required placeholder="Nom du passager" />
                            </div>
                            <div class="form-group">
                                <label>Prénom <span class="required">*</span></label>
                                <input type="text" name="passagerPrenom" class="form-control" required placeholder="Prénom du passager" />
                            </div>
                            <div class="form-group">
                                <label>Email <span class="required">*</span></label>
                                <input type="email" name="passagerEmail" class="form-control" required placeholder="email@exemple.com" />
                            </div>
                        </div>

                        <!-- Colonne Réservation -->
                        <div class="multi-form-card">
                            <div class="card-section-title">
                                <i class="fas fa-calendar-alt"></i> Détails Réservation
                            </div>
                            <div class="form-group">
                                <label>Date de réservation</label>
                                <input type="datetime-local" name="dateReservation" class="form-control" />
                            </div>
                            <div class="form-group">
                                <label>Statut de la réservation</label>
                                <select name="statutId" class="form-control">
                                    <option value="">-- Par défaut (En attente) --</option>
                                    <c:forEach var="s" items="${statusList}">
                                        <option value="${s.id}">${s.nomStatus}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <!-- Tableau des lignes -->
                    <div class="multi-lines-section">
                        <div class="lines-header">
                            <div class="lines-title">
                                <i class="fas fa-list"></i> Lignes de réservation
                            </div>
                            <button type="button" class="btn btn-primary" id="addLineBtn">
                                <i class="fas fa-plus"></i> Ajouter une ligne
                            </button>
                        </div>
                        
                        <div class="table-responsive">
                            <table class="table multi-lines-table" id="multiLinesTable">
                                <thead>
                                    <tr>
                                        <th style="width: 25%">Classe</th>
                                        <th style="width: 25%">Catégorie</th>
                                        <th style="width: 12%">Nombre</th>
                                        <th style="width: 15%">Prix unitaire</th>
                                        <th style="width: 15%">Total ligne</th>
                                        <th style="width: 8%">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <!-- Les lignes seront ajoutées dynamiquement -->
                                </tbody>
                            </table>
                        </div>
                        
                        <div class="multi-footer">
                            <div class="total-section">
                                <div class="total-label">Total Global</div>
                                <div class="total-amount" id="globalTotal">0.00 MGA</div>
                            </div>
                            <button type="submit" class="btn btn-success btn-lg">
                                <i class="fas fa-check-circle"></i> Valider les Réservations
                            </button>
                        </div>
                    </div>
                </form>
            </div>
            <!-- ========== FIN SECTION RÉSERVATION MULTIPLE ========== -->
        </c:if>
    </div>
</div>

<style>
    /* ========== STYLES PAGE RÉSERVATION ========== */
    
    /* Card principale */
    .card {
        border: none;
        border-radius: 16px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
        overflow: hidden;
    }
    
    .card-header {
        background: linear-gradient(135deg, #1e3a5f 0%, #2d5a87 100%);
        padding: 20px 30px;
        border: none;
    }
    
    .card-header .card-title {
        color: white;
        font-size: 1.5rem;
        font-weight: 700;
        margin: 0;
    }
    
    .card-body {
        padding: 30px;
        background: #f8fafc;
    }

    /* Section Filtres de recherche */
    .form-row {
        display: flex;
        gap: 20px;
        align-items: flex-end;
        background: white;
        padding: 24px;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
        border: 1px solid #e2e8f0;
        margin-bottom: 24px;
    }
    
    .form-row > div {
        flex: 1;
    }
    
    .form-row label {
        font-weight: 600;
        color: #4a5568;
        margin-bottom: 8px;
        display: block;
        font-size: 0.9rem;
    }
    
    .form-row .form-control {
        border: 2px solid #e2e8f0;
        border-radius: 10px;
        padding: 12px 16px;
        font-size: 0.95rem;
        transition: all 0.2s;
    }
    
    .form-row .form-control:focus {
        border-color: #667eea;
        box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.15);
        outline: none;
    }
    
    .form-row .btn-primary {
        padding: 12px 24px;
        border-radius: 10px;
        font-weight: 600;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border: none;
        white-space: nowrap;
    }
    
    .form-row .btn-primary:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
    }

    /* Sélection du vol */
    .form-group.mt-3 {
        background: white;
        padding: 20px 24px;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
        border: 1px solid #e2e8f0;
        margin-bottom: 30px;
    }
    
    .form-group.mt-3 > label {
        font-weight: 600;
        color: #4a5568;
        font-size: 0.9rem;
        margin-bottom: 10px;
        display: block;
    }

    /* Layout principal: sièges + détails */
    .row {
        display: flex;
        gap: 30px;
        margin-bottom: 30px;
    }
    
    .row > div:first-child {
        flex: 0 0 60%;
        background: white;
        padding: 24px;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
        border: 1px solid #e2e8f0;
    }
    
    .row > div:last-child {
        flex: 0 0 calc(40% - 30px);
        background: white;
        padding: 24px;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
        border: 1px solid #e2e8f0;
    }
    
    .row h4 {
        font-size: 1.1rem;
        font-weight: 700;
        color: #2d3748;
        margin-bottom: 20px;
        padding-bottom: 12px;
        border-bottom: 2px solid #e9ecef;
    }

    /* Plan des sièges */
    #seatMap {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;
        max-height: 450px;
        overflow-y: auto;
        padding: 15px;
        background: #f7fafc;
        border-radius: 10px;
        align-content: flex-start;
    }
    
    .seat-btn {
        width: 80px;
        height: 65px;
        padding: 8px 6px !important;
        border: 2px solid #cbd5e0 !important;
        background: #f7f7f7;
        border-radius: 8px !important;
        cursor: pointer;
        text-align: center;
        font-weight: 600;
        transition: all 0.2s;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
    }
    
    .seat-btn .seat-number {
        font-size: 0.9rem;
        font-weight: 700;
        color: #2d3748;
        display: block;
    }
    
    .seat-btn .seat-class {
        font-size: 0.65rem;
        font-weight: 500;
        color: #718096;
        display: block;
        margin-top: 2px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        max-width: 100%;
    }
    
    .seat-btn:hover:not([disabled]) {
        transform: translateY(-2px);
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }
    
    .seat-btn.selected {
        outline: none !important;
        border-color: #2b8a3e !important;
        background: #c6f6d5 !important;
        box-shadow: 0 0 0 3px rgba(43, 138, 62, 0.3);
    }
    
    .seat-btn.selected .seat-number {
        color: #22543d;
    }
    
    .seat-btn.status-libre {
        background: #e9f7ef;
        border-color: #9ae6b4 !important;
    }
    
    .seat-btn.status-reserve {
        background: #fef3cd;
        border-color: #fbd38d !important;
    }
    
    .seat-btn.status-bloque {
        background: #fed7d7;
        border-color: #feb2b2 !important;
        color: #c53030;
        cursor: not-allowed;
    }
    
    .seat-btn.status-occupe {
        background: #e2e8f0;
        border-color: #cbd5e0 !important;
        color: #718096;
        cursor: not-allowed;
    }
    
    .seat-btn[disabled] {
        opacity: 0.5;
        cursor: not-allowed;
    }

    /* Formulaire détails passager */
    #reservationForm .form-group {
        margin-bottom: 18px;
    }
    
    #reservationForm label {
        font-weight: 600;
        color: #4a5568;
        font-size: 0.9rem;
        margin-bottom: 8px;
        display: block;
    }
    
    #reservationForm .form-control {
        border: 2px solid #e2e8f0;
        border-radius: 10px;
        padding: 12px 16px;
        font-size: 0.95rem;
        transition: all 0.2s;
    }
    
    #reservationForm .form-control:focus {
        border-color: #667eea;
        box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.15);
        outline: none;
    }
    
    #chosenSeat, #chosenPrice {
        font-size: 1.1rem;
        font-weight: 700;
        color: #2b8a3e;
        background: #e9f7ef;
        padding: 12px 16px;
        border-radius: 8px;
    }
    
    #reservationForm .btn-success {
        padding: 14px 28px;
        border-radius: 10px;
        font-weight: 600;
        background: linear-gradient(135deg, #38a169 0%, #2f855a 100%);
        border: none;
        font-size: 1rem;
    }
    
    #reservationForm .btn-success:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(56, 161, 105, 0.4);
    }
    
    #reservationForm .btn-secondary {
        padding: 14px 28px;
        border-radius: 10px;
        font-weight: 600;
        margin-left: 10px;
    }

    /* ========== STYLES RÉSERVATION MULTIPLE ========== */
    .multi-reservation-section {
        margin-top: 40px;
        background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
        border-radius: 16px;
        padding: 30px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
        border: 1px solid #e9ecef;
    }

    .multi-reservation-header {
        display: flex;
        align-items: center;
        gap: 20px;
        margin-bottom: 30px;
        padding-bottom: 20px;
        border-bottom: 2px solid #e9ecef;
    }

    .multi-header-icon {
        width: 60px;
        height: 60px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border-radius: 16px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-size: 24px;
        flex-shrink: 0;
    }

    .multi-header-text h4 {
        margin: 0 0 5px 0;
        font-size: 1.5rem;
        font-weight: 700;
        color: #2d3748;
        border: none;
        padding: 0;
    }

    .multi-header-text p {
        margin: 0;
        color: #718096;
        font-size: 0.95rem;
    }

    .multi-form-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 24px;
        margin-bottom: 30px;
    }

    .multi-form-card {
        background: white;
        border-radius: 12px;
        padding: 24px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
        border: 1px solid #e2e8f0;
    }

    .card-section-title {
        font-size: 1rem;
        font-weight: 600;
        color: #4a5568;
        margin-bottom: 20px;
        padding-bottom: 12px;
        border-bottom: 1px solid #e2e8f0;
    }

    .card-section-title i {
        margin-right: 8px;
        color: #667eea;
    }

    .multi-form-card .form-group {
        margin-bottom: 16px;
    }

    .multi-form-card .form-group:last-child {
        margin-bottom: 0;
    }

    .multi-form-card label {
        font-weight: 500;
        color: #4a5568;
        margin-bottom: 6px;
        display: block;
    }

    .multi-form-card label .required {
        color: #e53e3e;
    }

    .multi-form-card .form-control {
        border: 1px solid #e2e8f0;
        border-radius: 8px;
        padding: 10px 14px;
        font-size: 0.95rem;
        transition: border-color 0.2s, box-shadow 0.2s;
    }

    .multi-form-card .form-control:focus {
        border-color: #667eea;
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.15);
        outline: none;
    }

    .multi-lines-section {
        background: white;
        border-radius: 12px;
        padding: 24px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
        border: 1px solid #e2e8f0;
    }

    .lines-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
    }

    .lines-title {
        font-size: 1rem;
        font-weight: 600;
        color: #4a5568;
    }

    .lines-title i {
        margin-right: 8px;
        color: #667eea;
    }

    .multi-lines-table {
        width: 100%;
        border-collapse: separate;
        border-spacing: 0;
    }

    .multi-lines-table thead th {
        background: #f7fafc;
        color: #4a5568;
        font-weight: 600;
        font-size: 0.85rem;
        text-transform: uppercase;
        letter-spacing: 0.5px;
        padding: 14px 16px;
        border-bottom: 2px solid #e2e8f0;
        text-align: left;
    }

    .multi-lines-table tbody tr {
        transition: background-color 0.2s;
    }

    .multi-lines-table tbody tr:hover {
        background-color: #f7fafc;
    }

    .multi-lines-table tbody td {
        padding: 12px 16px;
        border-bottom: 1px solid #e2e8f0;
        vertical-align: middle;
    }

    .multi-lines-table .form-control {
        border: 1px solid #e2e8f0;
        border-radius: 6px;
        padding: 8px 12px;
    }

    .multi-lines-table .btn-danger {
        padding: 6px 12px;
        border-radius: 6px;
        font-size: 0.85rem;
    }

    .multi-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-top: 24px;
        padding-top: 24px;
        border-top: 2px solid #e2e8f0;
    }

    .total-section {
        display: flex;
        align-items: center;
        gap: 16px;
    }

    .total-label {
        font-size: 1rem;
        font-weight: 600;
        color: #4a5568;
    }

    .total-amount {
        font-size: 1.75rem;
        font-weight: 700;
        color: #2b8a3e;
        background: #e9f7ef;
        padding: 8px 20px;
        border-radius: 8px;
    }

    .multi-footer .btn-success {
        padding: 12px 32px;
        font-size: 1rem;
        font-weight: 600;
        border-radius: 10px;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    /* Responsive */
    @media (max-width: 768px) {
        .multi-form-grid {
            grid-template-columns: 1fr;
        }
        
        .multi-footer {
            flex-direction: column;
            gap: 20px;
        }
        
        .total-section {
            flex-direction: column;
            text-align: center;
        }
    }
</style>

<script>
    (function(){
        const seatButtons = Array.from(document.querySelectorAll('#seatMap .seat-btn'));
        const siegeIdsInput = document.getElementById('siegeIds');
        const classeInput = document.getElementById('classeId');
        const chosenSeat = document.getElementById('chosenSeat');
        const chosenPrice = document.getElementById('chosenPrice');
        const totalPrixInput = document.getElementById('totalPrix');

            // Build priceMatrix from server-provided model (JS object)
            const priceMatrix = {};
            <c:forEach var="entry" items="${priceMatrix}">
                priceMatrix['${entry.key}'] = {};
                <c:forEach var="inner" items="${entry.value}">
                    priceMatrix['${entry.key}']['${inner.key}'] = ${inner.value};
                </c:forEach>
            </c:forEach>
            // Expose to global scope for multi-reservation use
            window.priceMatrix = priceMatrix;

            // Build categoryInfo with pourcentageReduction for each category
            const categoryInfo = {};
            <c:forEach var="c" items="${categories}">
                categoryInfo['${c.id}'] = {
                    nom: '${c.categorie}',
                    pourcentage: <c:choose><c:when test="${c.pourcentageReduction != null}">${c.pourcentageReduction}</c:when><c:otherwise>null</c:otherwise></c:choose>
                };
            </c:forEach>
            window.categoryInfo = categoryInfo;

            // Find Adulte category ID
            let adulteCatId = null;
            for(const [cid, info] of Object.entries(categoryInfo)){
                if(info.nom && info.nom.toLowerCase() === 'adulte'){
                    adulteCatId = cid;
                    break;
                }
            }
            window.adulteCatId = adulteCatId;

            const categorieSelect = document.getElementById('categorieId');
            function updateSeatPricesForCategory(catId){
                const catInfo = categoryInfo[catId];
                const pct = catInfo ? catInfo.pourcentage : null;

                seatButtons.forEach(b => {
                    const classId = b.getAttribute('data-classe');
                    let newPrice = null;

                    if(pct !== null && adulteCatId !== null){
                        // Category has a percentage reduction -> price = adultePrice * pct / 100
                        let adultePrice = priceMatrix[classId] && priceMatrix[classId][adulteCatId] ? priceMatrix[classId][adulteCatId] : null;
                        if(adultePrice === null){
                            // fallback to default price as adulte base
                            adultePrice = parseFloat(b.getAttribute('data-default-price') || '0');
                        }
                        newPrice = adultePrice * pct / 100;
                    } else if(priceMatrix[classId] && priceMatrix[classId][catId]){
                        // Category-specific price exists
                        newPrice = priceMatrix[classId][catId];
                    }

                    if(newPrice !== null){
                        b.setAttribute('data-price', parseFloat(newPrice));
                    } else {
                        // fallback to default price
                        b.setAttribute('data-price', parseFloat(b.getAttribute('data-default-price') || '0'));
                    }
                });
                // refresh displayed selection total
                updateSelectionDisplay();
            }

            if(categorieSelect){
                categorieSelect.addEventListener('change', function(){
                    const catId = this.value;
                    if(catId) updateSeatPricesForCategory(catId);
                });
                // initialize using current selected value (if any)
                if(categorieSelect.value) updateSeatPricesForCategory(categorieSelect.value);
            }

        function updateSelectionDisplay(){
            const selected = seatButtons.filter(b => b.classList.contains('selected'));
            if(selected.length === 0){
                chosenSeat.textContent = 'Aucun';
                chosenPrice.textContent = '0.00';
                if(totalPrixInput) totalPrixInput.value = '0.00';
                siegeIdsInput.value = '';
                classeInput.value = '';
                return;
            }
            const nums = selected.map(b => b.textContent.trim());
            const sum = selected.reduce((acc,b) => acc + parseFloat(b.getAttribute('data-price') || '0'), 0);
            const ids = selected.map(b => b.getAttribute('data-id'));
            chosenSeat.textContent = nums.join(', ');
            chosenPrice.textContent = sum.toFixed(2);
            if(totalPrixInput) totalPrixInput.value = sum.toFixed(2);
            siegeIdsInput.value = ids.join(',');
            // set classe to first selected's classe
            classeInput.value = selected[0].getAttribute('data-classe');
        }

        seatButtons.forEach(btn => {
            if(btn.disabled) return;
            btn.addEventListener('click', function(){
                // single selection: deselect others, toggle current
                const already = btn.classList.contains('selected');
                seatButtons.forEach(b => b.classList.remove('selected'));
                if(!already){
                    btn.classList.add('selected');
                }
                updateSelectionDisplay();
            });
        });
    })();

    // ========== MULTI-RESERVATION JS ==========
    (function(){
        const addLineBtn = document.getElementById('addLineBtn');
        const tableBody = document.querySelector('#multiLinesTable tbody');
        const globalTotalEl = document.getElementById('globalTotal');

        // Helper: compute unit price for a given class and category
        function computeUnitPrice(classId, catId){
            const priceMatrix = window.priceMatrix || {};
            const categoryInfo = window.categoryInfo || {};
            const adulteCatId = window.adulteCatId;

            if(!classId) return 0;

            // 1) Check if explicit category price exists
            if(catId && priceMatrix[classId] && priceMatrix[classId][catId] !== undefined){
                return parseFloat(priceMatrix[classId][catId]);
            }

            // 2) If category has pourcentageReduction, apply it to Adulte price
            const catInfo = categoryInfo[catId];
            if(catInfo && catInfo.pourcentage != null && adulteCatId){
                let adultePrice = priceMatrix[classId] && priceMatrix[classId][adulteCatId] !== undefined
                    ? parseFloat(priceMatrix[classId][adulteCatId]) : 0;
                return adultePrice * (parseFloat(catInfo.pourcentage) / 100);
            }

            // 3) Fallback: use any price for this class (first available)
            if(priceMatrix[classId]){
                const vals = Object.values(priceMatrix[classId]);
                if(vals.length) return parseFloat(vals[0]);
            }
            return 0;
        }

        function updateGlobalTotal(){
            let total = 0;
            tableBody.querySelectorAll('tr').forEach(tr => {
                const lineTotalEl = tr.querySelector('.line-total');
                if(lineTotalEl) total += parseFloat(lineTotalEl.textContent) || 0;
            });
            globalTotalEl.textContent = total.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ' ') + ' MGA';
        }

        function createLineRow(){
            const tr = document.createElement('tr');

            // Classe select
            const classeTd = document.createElement('td');
            const selectClasse = document.createElement('select');
            selectClasse.name = 'linesClassId';
            selectClasse.className = 'form-control';
            selectClasse.required = true;
            selectClasse.innerHTML = '<option value="">-- Classe --</option>';
            <c:forEach var="cl" items="${classes}">
                selectClasse.innerHTML += '<option value="${cl.id}">${cl.nomClasse}</option>';
            </c:forEach>
            classeTd.appendChild(selectClasse);

            // Catégorie select
            const catTd = document.createElement('td');
            const selectCat = document.createElement('select');
            selectCat.name = 'linesCategorieId';
            selectCat.className = 'form-control';
            selectCat.innerHTML = '<option value="">-- Catégorie --</option>';
            <c:forEach var="c" items="${categories}">
                selectCat.innerHTML += '<option value="${c.id}">${c.categorie}</option>';
            </c:forEach>
            catTd.appendChild(selectCat);

            // Quantité
            const qtyTd = document.createElement('td');
            const qtyInput = document.createElement('input');
            qtyInput.type = 'number'; qtyInput.name = 'linesQty'; qtyInput.min = '1'; qtyInput.value = '1';
            qtyInput.className = 'form-control'; qtyInput.required = true;
            qtyTd.appendChild(qtyInput);

            // Prix unitaire (read-only display + hidden input to send to server)
            const unitPriceTd = document.createElement('td');
            const unitPriceSpan = document.createElement('span');
            unitPriceSpan.className = 'unit-price';
            unitPriceSpan.textContent = '0.00';
            const hiddenPriceInput = document.createElement('input');
            hiddenPriceInput.type = 'hidden';
            hiddenPriceInput.name = 'linesPrice';
            hiddenPriceInput.value = '0';
            unitPriceTd.appendChild(unitPriceSpan);
            unitPriceTd.appendChild(hiddenPriceInput);

            // Total ligne (read-only display)
            const lineTotalTd = document.createElement('td');
            const lineTotalSpan = document.createElement('span');
            lineTotalSpan.className = 'line-total';
            lineTotalSpan.textContent = '0.00';
            lineTotalTd.appendChild(lineTotalSpan);

            // Supprimer button
            const rmTd = document.createElement('td');
            const rmBtn = document.createElement('button');
            rmBtn.type = 'button'; rmBtn.className = 'btn btn-sm btn-danger'; rmBtn.textContent = 'Supprimer';
            rmBtn.addEventListener('click', () => { tr.remove(); updateGlobalTotal(); });
            rmTd.appendChild(rmBtn);

            tr.appendChild(classeTd);
            tr.appendChild(catTd);
            tr.appendChild(qtyTd);
            tr.appendChild(unitPriceTd);
            tr.appendChild(lineTotalTd);
            tr.appendChild(rmTd);

            function updateRowPrice(){
                const classId = selectClasse.value;
                const catId = selectCat.value;
                const qty = parseInt(qtyInput.value) || 1;
                const unitPrice = computeUnitPrice(classId, catId);
                const lineTotal = unitPrice * qty;
                unitPriceSpan.textContent = unitPrice.toFixed(2);
                hiddenPriceInput.value = unitPrice.toFixed(2); // Send unit price to server
                lineTotalSpan.textContent = lineTotal.toFixed(2);
                updateGlobalTotal();
            }

            selectClasse.addEventListener('change', updateRowPrice);
            selectCat.addEventListener('change', updateRowPrice);
            qtyInput.addEventListener('input', updateRowPrice);

            // Initialize
            updateRowPrice();
            return tr;
        }

        addLineBtn.addEventListener('click', function(){
            tableBody.appendChild(createLineRow());
        });

        // Add one initial row
        tableBody.appendChild(createLineRow());
        
        // DEBUG: Log form data before submit
        document.getElementById('multiReservationForm').addEventListener('submit', function(e) {
            const formData = new FormData(this);
            console.log('=== FORM DATA DEBUG ===');
            for (let [key, value] of formData.entries()) {
                console.log(key + ': ' + value);
            }
            // Check hidden inputs specifically
            const hiddenPrices = document.querySelectorAll('input[name="linesPrice"]');
            console.log('Hidden price inputs found:', hiddenPrices.length);
            hiddenPrices.forEach((input, idx) => {
                console.log('linesPrice[' + idx + '] value:', input.value);
            });
        });
    })();
</script>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
