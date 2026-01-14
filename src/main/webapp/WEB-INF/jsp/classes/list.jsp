<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Liste des Classes</h3>
        <a href="${pageContext.request.contextPath}/classes/create" class="btn btn-primary">Nouveau</a>
    </div>
    <div class="card-body">
        <div class="mb-3">
            <form method="get" action="${pageContext.request.contextPath}/classes" class="form-inline">
                <input type="text" name="nomClasse" value="${param.nomClasse}" placeholder="Nom classe" class="form-control mr-2" />
                <input type="number" name="niveau" value="${param.niveau}" placeholder="Niveau" class="form-control mr-2" style="width:110px" />
                <button class="btn btn-sm btn-primary" type="submit">Rechercher</button>
                <a href="${pageContext.request.contextPath}/classes" class="btn btn-sm btn-secondary ml-2">Réinitialiser</a>
            </form>
        </div>
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                <tr><th>Nom</th><th>Niveau</th><th>Actions</th></tr>
                </thead>
                <tbody>
                <c:forEach var="c1" items="${classes}">
                    <tr>
                        <td>${c1.nomClasse}</td>
                        <td>${c1.niveau}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/classes/edit/${c1.id}" class="btn btn-sm btn-info">Modifier</a>
                            <form method="post" action="${pageContext.request.contextPath}/classes/delete/${c1.id}" style="display:inline">
                                <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Supprimer ?')">Supprimer</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
                <c:if test="${empty classes}">
                    <tfoot>
                        <tr><td colspan="3" class="text-muted text-center">Aucune classe trouvée pour ces critères.</td></tr>
                    </tfoot>
                </c:if>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
