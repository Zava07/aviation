<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Réservations</h3>
        <a href="${pageContext.request.contextPath}/reservations/create" class="btn btn-primary">Nouvelle Réservation</a>
    </div>
    <div class="card-body">
        <form method="get" action="${pageContext.request.contextPath}/reservations" class="filters">
            <div style="display:flex;gap:8px;flex-wrap:wrap">
                <input type="text" name="numero" placeholder="Numéro" value="${filterNumero}" class="form-control" />
                <input type="text" name="passager" placeholder="Passager (nom/prenom)" value="${filterPassager}" class="form-control" />
                <select name="vol" class="form-control">
                    <option value="">-- Vol --</option>
                    <c:forEach var="v" items="${vols}">
                        <option value="${v.numeroVol}" ${filterVol == v.numeroVol ? 'selected' : ''}>${v.numeroVol} - ${v.dateDepart}</option>
                    </c:forEach>
                </select>
                <select name="status" class="form-control">
                    <option value="">-- Statut --</option>
                    <c:forEach var="s" items="${statusList}">
                        <option value="${s.id}" ${filterStatus == s.id ? 'selected' : ''}>${s.nomStatus}</option>
                    </c:forEach>
                </select>
                <input type="date" name="dateReservation" value="${filterDate}" class="form-control" />
                <button class="btn btn-secondary">Filtrer</button>
                <a href="${pageContext.request.contextPath}/reservations" class="btn btn-light">Réinitialiser</a>
            </div>
        </form>

        <div class="table-responsive mt-3">
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
                                <c:if test="${not empty r.vol}">${r.vol.numeroVol}</c:if>
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
                                <a href="${pageContext.request.contextPath}/reservations/edit/${r.id}" class="btn btn-sm btn-info">Modifier</a>
                                <form method="post" action="${pageContext.request.contextPath}/reservations/delete/${r.id}" style="display:inline">
                                    <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Annuler réservation ?')">Annuler</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
