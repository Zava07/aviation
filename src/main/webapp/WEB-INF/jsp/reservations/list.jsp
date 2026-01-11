<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Réservations</h3>
        <a href="${pageContext.request.contextPath}/reservations/create" class="btn btn-primary">Nouvelle Réservation</a>
    </div>
    <div class="card-body">
        <div class="mb-3">
            <form method="get" action="${pageContext.request.contextPath}/reservations" class="form-inline">
                <input type="text" name="q" value="${param.q}" placeholder="Recherche..." class="form-control mr-2" />
                <button class="btn btn-sm btn-primary" type="submit">Rechercher</button>
                <a href="${pageContext.request.contextPath}/reservations" class="btn btn-sm btn-secondary ml-2">Réinitialiser</a>
            </form>
            <form method="post" action="${pageContext.request.contextPath}/reservations/recalculate-prices" style="display:inline;margin-left:8px">
                <button class="btn btn-sm btn-warning" type="submit" onclick="return confirm('Recalculer les prix pour toutes les réservations ?')">Recalculer les prix</button>
            </form>
        </div>
        <form method="get" action="${pageContext.request.contextPath}/reservations" class="filters">
            <div style="display:flex;gap:8px;flex-wrap:wrap">
                <input type="text" name="numero" placeholder="Numéro" value="${filterNumero}" class="form-control" />
                <input type="text" name="passager" placeholder="Passager (nom/prenom)" value="${filterPassager}" class="form-control" />
                <!-- Filtre par numéro de vol (texte) -->
                <input type="text" name="vol" placeholder="Vol (numéro)" value="${filterVol}" class="form-control" />
                <select name="status" class="form-control">
                    <option value="">-- Statut --</option>
                    <c:forEach var="s" items="${statusList}">
                        <option value="${s.id}" <c:if test="${filterStatus != null and filterStatus == s.id}">selected</c:if>>${s.nomStatus}</option>
                    </c:forEach>
                </select>
                <input type="date" name="dateReservation" value="${filterDate}" class="form-control" />
                <button class="btn btn-secondary">Filtrer</button>
                <a href="${pageContext.request.contextPath}/reservations" class="btn btn-light">Réinitialiser</a>
            </div>
        </form>

        <div class="table-responsive mt-3">
            <style>
                .actions{display:flex;gap:8px;justify-content:flex-end;align-items:center}
                .actions form{display:inline-block}
            </style>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Passager</th>
                        <th>Vol</th>
                        <th>Date Réservation</th>
                        <th>Classe</th>
                        <th>Siège</th>
                        <th>Prix</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="totalPrix" value="0" />
                    <c:forEach var="r" items="${reservations}">
                        <tr>
                            <td>${r.id}</td>
                            <td>
                                <c:if test="${not empty r.passager}">
                                    ${r.passager.nom} ${r.passager.prenom}<br/>
                                    <small>${r.passager.email}</small>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${not empty r.vol}">
                                    <c:choose>
                                        <c:when test="${not empty r.vol.vol}">
                                            ${r.vol.vol.numeroVol} - ${r.vol.dateDepart}
                                        </c:when>
                                        <c:otherwise/>
                                    </c:choose>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${not empty r.dateReservation}">${r.dateReservation}</c:if>
                            </td>
                            <td>
                                <c:if test="${not empty r.classe}">${r.classe.nomClasse}</c:if>
                            </td>
                            <td>
                                <c:if test="${not empty r.siege}">${r.siege.numero}</c:if>
                            </td>
                            <td>
                                <c:if test="${not empty r.prix}">${r.prix}</c:if>
                            </td>
                            <td>
                                <c:if test="${not empty r.statutReservation}">${r.statutReservation.nomStatus}</c:if>
                            </td>
                            <td>
                                <div class="actions">
                                    <c:if test="${empty r.statutReservation or not fn:contains(fn:toLowerCase(r.statutReservation.nomStatus),'confirm')}">
                                        <form method="post" action="${pageContext.request.contextPath}/reservations/confirm/${r.id}" style="margin:0;">
                                            <button type="submit" class="btn btn-sm btn-success" onclick="return confirm('Confirmer cette réservation ?')">Confirmer&Payer</button>
                                        </form>
                                    </c:if>
                                    <a href="${pageContext.request.contextPath}/reservations/edit/${r.id}" class="btn btn-sm btn-info">Modifier</a>
                                    <form method="post" action="${pageContext.request.contextPath}/reservations/delete/${r.id}" style="margin:0;">
                                        <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Annuler réservation ?')">Annuler</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                        <c:set var="totalPrix" value="${totalPrix + (r.prix != null ? r.prix : 0)}" />
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
