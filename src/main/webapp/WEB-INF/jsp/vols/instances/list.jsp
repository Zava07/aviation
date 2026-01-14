<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Instances de Vol</h3>
        <a href="${pageContext.request.contextPath}/vol-instances/create" class="btn btn-primary">Nouvelle Instance</a>
        <a href="${pageContext.request.contextPath}/vols" class="btn btn-secondary">Retour Vols</a>
    </div>
    <div class="card-body">
        <div class="mb-3">
            <form method="get" action="${pageContext.request.contextPath}/vol-instances" class="form-inline">
                <input type="date" name="date" value="${param.date}" class="form-control mr-2" />
                <select name="destinationId" class="form-control mr-2">
                    <option value="">-- Destination --</option>
                    <c:forEach var="a" items="${aeroports}">
                        <option value="${a.id}" <c:if test="${param.destinationId == a.id}">selected</c:if>>${a.nomAeroport}</option>
                    </c:forEach>
                </select>
                <select name="statusId" class="form-control mr-2">
                    <option value="">-- Statut --</option>
                    <c:forEach var="s" items="${statusList}">
                        <option value="${s.id}" <c:if test="${param.statusId == s.id}">selected</c:if>>${s.status}</option>
                    </c:forEach>
                </select>
                <button class="btn btn-sm btn-primary" type="submit">Rechercher</button>
                <a href="${pageContext.request.contextPath}/vol-instances" class="btn btn-sm btn-secondary ml-2">Réinitialiser</a>
            </form>
        </div>

        <div class="table-responsive">
            <table class="data-table">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Vol</th>
                    <th>Avion</th>
                    <th>Date Départ</th>
                    <th>Date Arrivée</th>
                    <th>Statut</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty vols}">
                        <c:forEach var="vol" items="${vols}">
                            <tr>
                                <td>${vol.id}</td>
                                <td>${vol.vol != null ? vol.vol.numeroVol : ''}</td>
                                <td>${vol.avion != null ? vol.avion.codeAvion : ''}</td>
                                <td>${vol.dateDepart}</td>
                                <td>${vol.dateArrivee}</td>
                                <td>${vol.statusVol != null ? vol.statusVol.status : ''}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/vol-instances/edit/${vol.id}" class="btn btn-sm btn-info">Modifier</a>
                                    <form method="post" action="${pageContext.request.contextPath}/vol-instances/delete/${vol.id}" style="display:inline">
                                        <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Confirmer suppression ?')">Supprimer</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="7" class="text-muted text-center">Aucune instance de vol trouvée pour ces critères.</td></tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>