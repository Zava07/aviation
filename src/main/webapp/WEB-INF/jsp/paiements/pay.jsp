<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Payer Billet</h3>
    </div>
    <div class="card-body">
        <c:if test="${not empty reservation}">
            <div class="mb-3">
                <h5>Réservation #${reservation.id}</h5>
                <p>Passager:
                    <c:if test="${not empty reservation.passager}">
                        <c:out value="${reservation.passager.nom}"/> <c:out value="${reservation.passager.prenom}"/>
                    </c:if>
                </p>
                <p>Vol: <c:out value="${reservation.vol != null && reservation.vol.vol != null ? reservation.vol.vol.numeroVol : ''}"/> - Date: <c:out value="${reservation.vol != null ? reservation.vol.dateDepart : ''}"/></p>
                <p><strong>Montant total à payer: </strong> <fmt:formatNumber value="${reservation.prix}" type="number" minFractionDigits="2" maxFractionDigits="2"/></p>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/paiements/save-multiple" id="payForm">
                <input type="hidden" name="reservationId" value="${reservation.id}" />

                <div id="paymentsContainer">
                    <div class="payment-row">
                        <div style="display:flex;gap:8px;align-items:center;margin-bottom:8px">
                            <select name="methodeId" class="form-control" style="width:200px">
                                <option value="">-- Méthode --</option>
                                <c:forEach var="m" items="${methodes}">
                                    <option value="${m.id}">${m.nomMethode}</option>
                                </c:forEach>
                            </select>
                            <select name="deviseId" class="form-control" style="width:120px">
                                <c:forEach var="d" items="${devises}">
                                    <option value="${d.id}">${d.nomDevise}</option>
                                </c:forEach>
                            </select>
                            <input type="number" step="0.01" name="montant" placeholder="Montant" class="form-control" style="width:160px" />
                            <select name="statusId" class="form-control" style="width:160px">
                                <c:forEach var="s" items="${statusList}">
                                    <option value="${s.id}">${s.status}</option>
                                </c:forEach>
                            </select>
                            <button type="button" class="btn btn-sm btn-danger remove-row">Supprimer</button>
                        </div>
                    </div>
                </div>

                <div style="margin-bottom:12px">
                    <button type="button" id="addRow" class="btn btn-sm btn-outline-primary">Ajouter méthode de paiement</button>
                </div>

                <div style="margin-top:12px">
                    <button type="submit" class="btn btn-success">Payer</button>
                    <a href="${pageContext.request.contextPath}/paiements" class="btn btn-secondary">Annuler</a>
                </div>
            </form>

            <style>
                .payment-row{border:1px dashed #ddd;padding:8px;border-radius:6px}
            </style>

            <script>
                (function(){
                    const container = document.getElementById('paymentsContainer');
                    document.getElementById('addRow').addEventListener('click', function(){
                        const row = container.querySelector('.payment-row');
                        const clone = row.cloneNode(true);
                        // clear values
                        clone.querySelectorAll('select, input').forEach(el => { if(el.tagName === 'INPUT') el.value=''; else el.selectedIndex = 0; });
                        container.appendChild(clone);
                    });
                    container.addEventListener('click', function(e){
                        if(e.target && e.target.classList.contains('remove-row')){
                            const rows = container.querySelectorAll('.payment-row');
                            if(rows.length > 1){
                                e.target.closest('.payment-row').remove();
                            }
                        }
                    });

                    // before submit, ensure the inputs are arrays (name already repeated). nothing extra needed.
                })();
            </script>
        </c:if>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
