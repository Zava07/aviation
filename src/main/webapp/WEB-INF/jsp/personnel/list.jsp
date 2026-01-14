<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Liste des Personnel</h3>
        <a href="${pageContext.request.contextPath}/personnel/create" class="btn btn-primary">Nouveau</a>
    </div>
    <div class="card-body">
        <div class="mb-3">
            <form method="get" action="${pageContext.request.contextPath}/personnel" class="form-inline">
                <input type="text" name="nom" value="${param.nom}" placeholder="Nom" class="form-control mr-2" />
                <input type="text" name="prenom" value="${param.prenom}" placeholder="Prénom" class="form-control mr-2" />
                <select name="typeId" class="form-control mr-2">
                    <option value="">-- Type --</option>
                    <c:forEach var="t" items="${types}">
                        <option value="${t.id}" <c:if test="${param.typeId == t.id}">selected</c:if>>${t.type}</option>
                    </c:forEach>
                </select>
                <button class="btn btn-sm btn-primary" type="submit">Rechercher</button>
                <a href="${pageContext.request.contextPath}/personnel" class="btn btn-sm btn-secondary ml-2">Réinitialiser</a>
            </form>
        </div>
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                <tr><th>Nom</th>
                    <th>Prénom</th>
                    <th>Poste</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="m" items="${membres}">
                    <tr>
                        <td>${m.nom}</td>
                        <td>${m.prenom}</td>
                        <td>${m.typeMembre.type}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/personnel/edit/${m.id}" class="btn btn-sm btn-info">Modifier</a>
                            <form method="post" action="${pageContext.request.contextPath}/personnel/delete/${m.id}" style="display:inline">
                                <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Supprimer ?')">Supprimer</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
                <c:if test="${empty membres}">
                    <tfoot>
                        <tr><td colspan="4" class="text-muted text-center">Aucun personnel trouvé pour ces critères.</td></tr>
                    </tfoot>
                </c:if>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
