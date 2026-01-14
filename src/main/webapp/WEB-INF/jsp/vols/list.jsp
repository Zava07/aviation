<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title"><i class="fas fa-plane-departure"></i> Liste des Vols
            <span class="badge badge-primary">${empty vols ? 0 : vols.size()} vol(s)</span>
        </h3>
        <a href="${pageContext.request.contextPath}/vols/create" class="btn btn-primary">Nouveau Vol</a>
    </div>
    <div class="card-body">
        <div class="mb-3">
            <form method="get" action="${pageContext.request.contextPath}/vols" class="form-inline">
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
                <a href="${pageContext.request.contextPath}/vols" class="btn btn-sm btn-secondary ml-2">Réinitialiser</a>
            </form>
        </div>
        <form method="get" action="${pageContext.request.contextPath}/vols" class="search-form">
            <input type="date" name="date" />
            <select name="destinationId">
                <option value="">Toutes destinations</option>
                <c:forEach var="a" items="${aeroports}">
                    <option value="${a.id}">${a.nomAeroport}</option>
                </c:forEach>
            </select>
            <select name="statusId">
                <option value="">Tous statuts</option>
                <c:forEach var="s" items="${statusList}">
                    <option value="${s.id}">${s.status}</option>
                </c:forEach>
            </select>
            <button type="submit" class="btn btn-sm btn-secondary">Rechercher</button>
        </form>

        <div class="table-responsive">
            <table class="data-table">
                <thead>
                <tr>
                    <th>N° Vol</th>
                    <th>Origine</th>
                    <th>Destination</th>
                    <th>Instances</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty vols}">
                        <c:forEach var="vol" items="${vols}">
                            <tr>
                                <td>${vol.numeroVol}</td>
                                <td>${vol.aeroportOrigine != null ? vol.aeroportOrigine.nomAeroport : ''}</td>
                                <td>${vol.aeroportDestination != null ? vol.aeroportDestination.nomAeroport : ''}</td>
                                <td>${empty vol.instances ? 0 : vol.instances.size()}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/vols/edit/${vol.id}" class="btn btn-sm btn-info">Modifier</a>
                                    <a href="${pageContext.request.contextPath}/vol-instances?volId=${vol.id}" class="btn btn-sm btn-secondary">Instances</a>
                                    <form method="post" action="${pageContext.request.contextPath}/vols/delete/${vol.id}" style="display:inline">
                                        <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Confirmer suppression ?')">Supprimer</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="5" class="text-muted text-center">Aucun vol trouvé pour ces critères.</td></tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>