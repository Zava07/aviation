<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Billets</h3>
    </div>
    <div class="card-body">
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
                    </tr>
                </thead>
                <tbody>
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
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
