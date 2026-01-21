<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h3 class="card-title">Billets</h3>
        <button type="button" id="openPayAllModal" class="btn btn-sm btn-success">Payer tous les billets</button>
    </div>

    <!-- Modal Payer tous les billets -->
    <div id="payAllModal" class="modal" tabindex="-1" role="dialog" style="display:none;">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Payer tous les billets non payés</h5>
                    <button type="button" class="close" id="closePayAllModal">&times;</button>
                </div>
                <div class="modal-body">
                    <form id="payAllForm" method="post" action="${pageContext.request.contextPath}/paiements/pay-all-billets">
                        <div class="mb-3">
                            <label>Méthode de paiement</label>
                            <select name="methodeId" class="form-control" required id="payAllMethode">
                                <option value="">-- Méthode --</option>
                                <c:forEach var="m" items="${methodes}">
                                    <option value="${m.id}">${m.nomMethode}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label>Devise</label>
                            <select name="deviseId" class="form-control" required>
                                <option value="">-- Devise --</option>
                                <c:forEach var="d" items="${devises}">
                                    <option value="${d.id}">${d.nomDevise}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label>Caisse</label>
                            <select name="caisseId" class="form-control" required id="payAllCaisse">
                                <option value="">-- Caisse --</option>
                                <c:forEach var="c" items="${caisses}">
                                    <option value="${c.id}">Caisse #${c.id}<c:if test="${not empty c.nomCaisse}"> - ${c.nomCaisse}</c:if></option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mt-3 text-right">
                            <button type="submit" class="btn btn-success">Payer Tout</button>
                            <button type="button" class="btn btn-secondary" id="cancelPayAll">Annuler</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
    (function(){
        const openBtn = document.getElementById('openPayAllModal');
        const modal = document.getElementById('payAllModal');
        const closeBtn = document.getElementById('closePayAllModal');
        const cancelBtn = document.getElementById('cancelPayAll');
        const methodeSelect = document.getElementById('payAllMethode');
        const caisseSelect = document.getElementById('payAllCaisse');

        const allCaisses = [];
        <c:forEach var="c" items="${caisses}">
            allCaisses.push({id: ${c.id}, nom: '${c.nomCaisse}'});
        </c:forEach>

        function showModal(){ modal.style.display='block'; }
        function hideModal(){ modal.style.display='none'; }

        openBtn && openBtn.addEventListener('click', showModal);
        closeBtn && closeBtn.addEventListener('click', hideModal);
        cancelBtn && cancelBtn.addEventListener('click', hideModal);

        function formatCaisseLabel(item){
            return 'Caisse #' + item.id + (item.nom ? ' - ' + item.nom : '');
        }

        function populateCaisseOptions(list){
            caisseSelect.innerHTML = '<option value="">-- Caisse --</option>';
            list.forEach(item => {
                const opt = document.createElement('option');
                opt.value = item.id;
                opt.textContent = formatCaisseLabel(item);
                caisseSelect.appendChild(opt);
            });
        }

        function fetchCaissesByMethode(methodeId){
            if(!methodeId) return Promise.resolve([]);
            return fetch('${pageContext.request.contextPath}/api/caisses/by-methode/' + encodeURIComponent(methodeId))
                .then(r => r.ok ? r.json() : [])
                .catch(() => []);
        }

        methodeSelect && methodeSelect.addEventListener('change', function(){
            const val = this.value;
            if(!val){
                populateCaisseOptions(allCaisses);
                return;
            }
            fetchCaissesByMethode(val).then(data => populateCaisseOptions(data.length ? data : allCaisses));
        });
    })();
    </script>

    <div class="card-body">
        <div class="mb-3">
            <form method="get" action="${pageContext.request.contextPath}/billets" class="form-inline">
                <input type="text" name="numero" value="${filterNumero}" placeholder="Numéro Billet" class="form-control mr-2" />
                <input type="text" name="passager" value="${filterPassager}" placeholder="Passager (nom/prenom)" class="form-control mr-2" />
                <input type="text" name="vol" value="${filterVol}" placeholder="Vol (numéro)" class="form-control mr-2" />
                <select name="status" class="form-control mr-2">
                    <option value="">-- Statut --</option>
                    <c:forEach var="s" items="${statusList}">
                        <option value="${s.id}" <c:if test="${filterStatus != null and filterStatus == s.id}">selected</c:if>>${s.status}</option>
                    </c:forEach>
                </select>
                <input type="date" name="dateEmission" value="${filterDate}" class="form-control mr-2" />
                <button class="btn btn-sm btn-primary" type="submit">Filtrer</button>
                <a href="${pageContext.request.contextPath}/billets" class="btn btn-sm btn-secondary ml-2">Réinitialiser</a>
            </form>
        </div>
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Numéro Billet</th>
                        <th>Passager</th>
                        <th>Vol</th>
                        <th>Date Vol</th>
                        <th>Siège</th>
                        <th>Prix</th>
                        <th>Date émission</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="totalPrix" value="0" />
                    <c:forEach var="b" items="${billets}">
                        
                        <tr>
                            <td>${b.id}</td>
                            <td>${b.numeroBillet}</td>
                            <td>
                                <c:if test="${not empty b.reservation and not empty b.reservation.passager}">
                                    ${b.reservation.passager.nom} ${b.reservation.passager.prenom}
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${not empty b.reservation and not empty b.reservation.vol and not empty b.reservation.vol.vol}">
                                    ${b.reservation.vol.vol.numeroVol}
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${not empty b.reservation and not empty b.reservation.vol}">${b.reservation.vol.dateDepart}</c:if>
                            </td>
                            <td>
                                <c:if test="${not empty b.siege}">${b.siege.numero}</c:if>
                            </td>
                            <td>
                                <c:if test="${not empty b.prixFinal}">${b.prixFinal}</c:if>
                            </td>
                            <td>
                                <c:if test="${not empty b.dateEmission}">${b.dateEmission}</c:if>
                            </td>
                            <td>
                                <c:if test="${not empty b.statusBillet}">${b.statusBillet.status}</c:if>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${b.statusBillet != null and b.statusBillet.status == 'Payer et Emis'}">
                                        <div style="display:flex;gap:8px;justify-content:flex-end;align-items:center">
                                            <form method="post" action="${pageContext.request.contextPath}/paiements/rembourser/${b.id}" style="display:inline" onsubmit="return confirm('Rembourser ce billet ?');">
                                                <button type="submit" class="btn btn-sm btn-outline-danger">Rembourser</button>
                                            </form>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div style="display:flex;gap:8px;justify-content:flex-end;align-items:center">
                                            <a href="${pageContext.request.contextPath}/paiements/create?billetId=${b.id}" class="btn btn-sm btn-primary">Payer</a>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <c:set var="totalPrix" value="${totalPrix + (b.prixFinal != null ? b.prixFinal : 0)}" />
                    </c:forEach>
                </tbody>
                            <c:if test="${not empty revenueSummaries}">
                                <tfoot>
                                    <c:forEach var="s" items="${revenueSummaries}">
                                        <tr>
                                            <td colspan="6" style="text-align:right">Total pour ${s.label} :</td>
                                            <td style="font-weight:bold"><fmt:formatNumber value="${s.total}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                                            <td colspan="2"></td>
                                        </tr>
                                    </c:forEach>
                                    <tr>
                                        <td colspan="6" style="text-align:right;font-weight:bold">Total Général:</td>
                                        <td style="font-weight:bold"><fmt:formatNumber value="${totalPrix}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                                        <td colspan="2"></td>
                                    </tr>
                                </tfoot>
                            </c:if>
            </table>
        </div>
        <c:if test="${empty billets}">
            <div class="alert alert-info">Aucun billet trouvé pour ces critères.</div>
        </c:if>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
