<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h3 class="card-title">Liste des paiements</h3>
        <div>
            <button type="button" id="openPayModal" class="btn btn-sm btn-primary">Payer</button>
            <a href="${pageContext.request.contextPath}/paiements" class="btn btn-sm btn-outline-secondary">Réinitialiser</a>
        </div>

        <!-- Single payment modal -->
        <div id="payModal" class="modal" tabindex="-1" role="dialog" style="display:none;">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Payer un billet</h5>
                        <button type="button" class="close" id="closePayModal">&times;</button>
                    </div>
                    <div class="modal-body">
                        <form id="paySingleForm" method="post" action="${pageContext.request.contextPath}/paiements/save-single">
                            <div class="mb-2">
                                <label>Numéro Billet</label>
                                <input list="billetsList" name="billetNumero" id="billetNumero" class="form-control" placeholder="Tapez le numéro de billet" autocomplete="off" required />
                                <input type="hidden" name="billetId" id="billetId" />
                                <datalist id="billetsList">
                                    <c:forEach var="b" items="${billets}">
                                        <option value="${b.numeroBillet}"></option>
                                    </c:forEach>
                                </datalist>
                            </div>

                            <div class="mb-2">
                                <label>Montant</label>
                                <input type="number" step="0.01" name="montant" id="montant" class="form-control" placeholder="Montant" />
                            </div>

                            <div style="display:flex;gap:8px">
                                <div style="flex:1">
                                    <label>Méthode</label>
                                    <select name="methodeId" class="form-control">
                                        <option value="">-- Méthode --</option>
                                        <c:forEach var="m" items="${methodes}">
                                            <option value="${m.id}">${m.nomMethode}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div style="width:140px">
                                    <label>Devise</label>
                                    <select name="deviseId" class="form-control">
                                        <option value="">-- Devise --</option>
                                        <c:forEach var="d" items="${devises}">
                                            <option value="${d.id}">${d.nomDevise}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div style="width:160px">
                                    <label>Caisse (optionnel)</label>
                                    <select name="caisseId" class="form-control">
                                        <option value="">-- Caisse --</option>
                                        <c:forEach var="c" items="${caisses}">
                                            <option value="${c.id}">Caisse #${c.id}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <div class="mt-3 text-right">
                                <button type="submit" class="btn btn-success">Payer</button>
                                <button type="button" class="btn btn-secondary" id="cancelPay">Annuler</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script>
            (function(){
                const openBtn = document.getElementById('openPayModal');
                const modal = document.getElementById('payModal');
                const closeBtn = document.getElementById('closePayModal');
                const cancelBtn = document.getElementById('cancelPay');
                const billetInput = document.getElementById('billetNumero');
                const billetIdHidden = document.getElementById('billetId');
                const montantInput = document.getElementById('montant');

                // initial small JS map of billets (for instant exact matches). For suggestions we call the search endpoint.
                const billets = [];
                <c:forEach var="b" items="${billets}">
                    <c:choose>
                        <c:when test="${not empty b.prixFinal}">
                            billets.push({id: ${b.id}, numero: '${b.numeroBillet}', prix: ${b.prixFinal}});
                        </c:when>
                        <c:otherwise>
                            billets.push({id: ${b.id}, numero: '${b.numeroBillet}', prix: null});
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                function showModal(){ modal.style.display='block'; }
                function hideModal(){ modal.style.display='none'; billetInput.value=''; billetIdHidden.value=''; montantInput.value=''; }

                openBtn && openBtn.addEventListener('click', function(){ showModal(); });
                closeBtn && closeBtn.addEventListener('click', function(){ hideModal(); });
                cancelBtn && cancelBtn.addEventListener('click', function(){ hideModal(); });

                // helper: populate datalist from server results
                function populateDatalist(results){
                    const list = document.getElementById('billetsList');
                    list.innerHTML = '';
                    results.forEach(r => {
                        const opt = document.createElement('option');
                        opt.value = r.numero;
                        list.appendChild(opt);
                    });
                }

                // when user types, fetch suggestions if length >= 2
                let lastQuery = '';
                billetInput && billetInput.addEventListener('input', function(){
                    const v = billetInput.value.trim();
                    // exact local match first
                    const found = billets.find(b => b.numero === v);
                    if(found){
                        billetIdHidden.value = found.id;
                        if(found.prix !== null && found.prix !== undefined) montantInput.value = found.prix;
                    } else {
                        billetIdHidden.value = '';
                    }

                    if (v.length >= 2 && v !== lastQuery) {
                        lastQuery = v;
                        fetch(`${pageContext.request.contextPath}/billets/search?q=` + encodeURIComponent(v))
                            .then(r => r.json())
                            .then(data => {
                                populateDatalist(data);
                                // if only one exact match returned, preselect it
                                if (data.length === 1 && data[0].numero.toLowerCase() === v.toLowerCase()){
                                    billetIdHidden.value = data[0].id;
                                    if(data[0].prix) montantInput.value = data[0].prix;
                                }
                            }).catch(e => { /* ignore */ });
                    }
                });

            })();
        </script>
    </div>
    <div class="card-body">
        <form method="get" action="${pageContext.request.contextPath}/paiements" class="mb-3">
            <div style="display:flex;gap:8px;flex-wrap:wrap;align-items:center">
                <select name="reservationId" class="form-control" style="width:200px">
                    <option value="">-- Réservation --</option>
                    <c:forEach var="r" items="${reservations}">
                        <option value="${r.id}" ${filterReservationId != null && filterReservationId == r.id ? 'selected' : ''}>Réservation #${r.id}</option>
                    </c:forEach>
                </select>

                <input type="text" name="passager" class="form-control" placeholder="Passager (nom/prénom)" style="width:220px" value="${filterPassager}" />

                <select name="statusId" class="form-control" style="width:180px">
                    <option value="">-- Statut --</option>
                    <c:forEach var="s" items="${statusList}">
                        <option value="${s.id}" ${filterStatusId != null && filterStatusId == s.id ? 'selected' : ''}>${s.status}</option>
                    </c:forEach>
                </select>

                <select name="methodeId" class="form-control" style="width:180px">
                    <option value="">-- Méthode --</option>
                    <c:forEach var="m" items="${methodes}">
                        <option value="${m.id}" ${filterMethodeId != null && filterMethodeId == m.id ? 'selected' : ''}>${m.nomMethode}</option>
                    </c:forEach>
                </select>

                <select name="deviseId" class="form-control" style="width:120px">
                    <option value="">-- Devise --</option>
                    <c:forEach var="d" items="${devises}">
                        <option value="${d.id}" ${filterDeviseId != null && filterDeviseId == d.id ? 'selected' : ''}>${d.nomDevise}</option>
                    </c:forEach>
                </select>

                <select name="caisseId" class="form-control" style="width:160px">
                    <option value="">-- Caisse --</option>
                    <c:forEach var="c" items="${caisses}">
                        <option value="${c.id}" ${filterCaisseId != null && filterCaisseId == c.id ? 'selected' : ''}>Caisse #${c.id}</option>
                    </c:forEach>
                </select>

                <input type="date" name="dateFrom" class="form-control" style="width:160px" value="${filterDateFrom}" />
                <input type="date" name="dateTo" class="form-control" style="width:160px" value="${filterDateTo}" />

                <input type="number" step="0.01" name="montantMin" class="form-control" placeholder="Montant min" style="width:140px" value="${filterMontantMin}" />
                <input type="number" step="0.01" name="montantMax" class="form-control" placeholder="Montant max" style="width:140px" value="${filterMontantMax}" />

                <button type="submit" class="btn btn-primary">Filtrer</button>
            </div>
        </form>
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Réservation</th>
                        <th>Passager</th>
                        <th>Montant</th>
                        <th>Devise</th>
                        <th>Méthode</th>
                        <th>Statut</th>
                        <th>Caisses</th>
                        <th>Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${paiements}">
                        <tr>
                            <td><c:out value="${p.id}"/></td>
                            <td><c:out value="${p.reservation != null ? p.reservation.id : ''}"/></td>
                            <td>
                                <c:if test="${p.reservation != null && p.reservation.passager != null}">
                                    <c:out value="${p.reservation.passager.nom}"/> <c:out value="${p.reservation.passager.prenom}"/>
                                </c:if>
                            </td>
                            <td><fmt:formatNumber value="${p.montant}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                            <td><c:out value="${p.devise != null ? p.devise.nomDevise : ''}"/></td>
                            <td><c:out value="${p.methodePaiement != null ? p.methodePaiement.nomMethode : ''}"/></td>
                            <td><c:out value="${p.statusPaiement != null ? p.statusPaiement.status : ''}"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty paiementToCaisses[p.id]}">
                                        Caisse #<c:out value="${paiementToCaisses[p.id][0]}"/>
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${p.datePaiement}</td>

                            <td>
                                <a href="${pageContext.request.contextPath}/paiements/edit/${p.id}" class="btn btn-sm btn-outline-primary">Voir</a>
                                <form method="post" action="${pageContext.request.contextPath}/paiements/delete/${p.id}" style="display:inline" onsubmit="return confirm('Supprimer ce paiement ?');">
                                    <button type="submit" class="btn btn-sm btn-outline-danger">Rembourser</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <c:if test="${empty paiements}">
            <div class="alert alert-info">Aucun paiement trouvé pour ces critères.</div>
        </c:if>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>