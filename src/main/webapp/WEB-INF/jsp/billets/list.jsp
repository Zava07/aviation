<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Billets</h3>
    </div>
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
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
