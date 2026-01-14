<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Liste des Modèles</h3>
        <a href="${pageContext.request.contextPath}/modeles/create" class="btn btn-primary">Nouveau</a>
    </div>
    <div class="card-body">
        <div class="mb-3">
            <form method="get" action="${pageContext.request.contextPath}/modeles" class="form-inline">
                <input type="text" name="nomModele" value="${param.nomModele}" placeholder="Nom modèle" class="form-control mr-2" />
                <button class="btn btn-sm btn-primary" type="submit">Rechercher</button>
                <a href="${pageContext.request.contextPath}/modeles" class="btn btn-sm btn-secondary ml-2">Réinitialiser</a>
            </form>
        </div>
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                <tr><th>Nom Modèle</th><th>Actions</th></tr>
                </thead>
                <tbody>
                <c:forEach var="m" items="${modeles}">
                    <tr>
                        <td>${m.nomModele}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/modeles/edit/${m.id}" class="btn btn-sm btn-info">Modifier</a>
                            <form method="post" action="${pageContext.request.contextPath}/modeles/delete/${m.id}" style="display:inline">
                                <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Supprimer ?')">Supprimer</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
                <c:if test="${empty modeles}">
                    <tfoot>
                        <tr><td colspan="2" class="text-muted text-center">Aucun modèle trouvé pour ces critères.</td></tr>
                    </tfoot>
                </c:if>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
