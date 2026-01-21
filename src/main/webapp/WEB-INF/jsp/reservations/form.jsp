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
                    <div id="seatMap" style="display:flex; flex-wrap:wrap; gap:8px;">
                        <c:forEach var="s" items="${seats}">
                            <c:set var="status" value="${s.status}" />
                            <c:set var="selectable" value="${s.selectable}" />
                            <button class="seat-btn status-${fn:toLowerCase(status)}" data-id="${s.id}" data-status="${status}" data-classe="${s.classeId}" data-price="${s.price}" data-default-price="${s.price}" ${!selectable ? 'disabled' : ''}>${s.numero}<br/><small>${s.classeNom}</small></button>
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

                    <!-- ========== SECTION RÉSERVATION MULTIPLE ========== -->
                    <hr/>
                    <h4>Réservation Multiple (Attribution automatique des sièges)</h4>
                    <p>Ajoutez des lignes (classe, catégorie, nombre de places). Les sièges libres seront attribués automatiquement.</p>
                    <form method="post" action="${pageContext.request.contextPath}/reservations/save-multiple" id="multiReservationForm">
                        <input type="hidden" name="volInstanceId" value="${selectedVol.id}" />

                        <div class="form-group">
                            <label>Date de réservation</label>
                            <input type="datetime-local" name="dateReservation" class="form-control" />
                        </div>
                        <div class="form-group">
                            <label>Nom</label>
                            <input type="text" name="passagerNom" class="form-control" required />
                        </div>
                        <div class="form-group">
                            <label>Prénom</label>
                            <input type="text" name="passagerPrenom" class="form-control" required />
                        </div>
                        <div class="form-group">
                            <label>Email</label>
                            <input type="email" name="passagerEmail" class="form-control" required />
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

                        <table class="table table-bordered" id="multiLinesTable">
                            <thead>
                                <tr>
                                    <th>Classe</th>
                                    <th>Catégorie</th>
                                    <th>Nombre</th>
                                    <th>Prix unitaire</th>
                                    <th>Total ligne</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colspan="4" style="text-align:right"><strong>Total global:</strong></td>
                                    <td id="globalTotal">0.00</td>
                                    <td></td>
                                </tr>
                            </tfoot>
                        </table>
                        <div class="mb-3">
                            <button type="button" class="btn btn-sm btn-outline-primary" id="addLineBtn">+ Ajouter une ligne</button>
                        </div>

                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">Valider Réservations Multiples</button>
                        </div>
                    </form>
                    <!-- ========== FIN SECTION RÉSERVATION MULTIPLE ========== -->
                </div>
            </div>
        </c:if>
    </div>
</div>

<style>
    .seat-btn{padding:8px 10px;border:1px solid #ccc;background:#f7f7f7;border-radius:4px;cursor:pointer}
    .seat-btn.selected{outline:3px solid #2b8a3e}
    .seat-btn.status-libre{background:#e9f7ef}
    .seat-btn.status-reserve{background:#fff3cd}
    .seat-btn.status-bloque{background:#f8d7da;color:#7a1f1f;cursor:not-allowed}
    .seat-btn.status-occupe{background:#e2e3e5;color:#6c757d;cursor:not-allowed}
    .seat-btn[disabled]{opacity:0.6;cursor:not-allowed}
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
            globalTotalEl.textContent = total.toFixed(2);
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
