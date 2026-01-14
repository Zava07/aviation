<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h3 class="card-title">Gestion des Caisses</h3>
    </div>
    <div class="card-body">
        <div class="mb-3 d-flex justify-content-between align-items-center">
            <div>
                <h5>Dashboard</h5>
                <p>Total chiffre d'affaire global: <strong><fmt:formatNumber value="${totalCA}" type="number" minFractionDigits="2" maxFractionDigits="2"/></strong></p>
            </div>
        </div>
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nom</th>
                        <th>Solde</th>
                        <th>Chiffre d'affaire (credits)</th>
                        <th>Balance (credits - debits)</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="c" items="${caisses}">
                        <tr>
                            <td><c:out value="${c.id}"/></td>
                            <td><c:out value="${c.nomCaisse}"/></td>
                            <td><fmt:formatNumber value="${c.solde}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                            <td><fmt:formatNumber value="${totalCredits[c.id]}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                            <td><fmt:formatNumber value="${balances[c.id]}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/caisses/${c.id}" class="btn btn-sm btn-outline-primary view-caisse" data-caisse-id="${c.id}">Détails</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- modal -->
<div id="caisseDetailsModal" class="modal" style="display:none;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Détails Caisse</h5>
                <button type="button" class="close" onclick="hideCaisseModal();">&times;</button>
            </div>
            <div class="modal-body">
                <div id="caisseInfo">
                    <p>Nom: <span id="caisseNom"></span></p>
                    <p>Solde: <span id="caisseSolde"></span></p>
                    <p>Balance calculée: <span id="caisseBalance"></span></p>
                    <p>Total credits: <span id="caisseCredits"></span></p>
                </div>
                <h6>Mouvements</h6>
                <table class="data-table" id="mouvementsTable">
                    <thead>
                        <tr><th>ID</th><th>Montant</th><th>Type</th><th>Date</th><th>Paiement</th></tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" onclick="hideCaisseModal();">Fermer</button>
            </div>
        </div>
    </div>
</div>


<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>