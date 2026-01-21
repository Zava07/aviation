<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Liste des catégories passagers</h3>
        <a href="${pageContext.request.contextPath}/categorie-passager/create" class="btn btn-primary">Nouveau</a>
    </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr><th>ID</th><th>Catégorie</th><th>Pourcentage réduction</th><th>Actions</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="c" items="${categories}">
                        <tr>
                            <td>${c.id}</td>
                            <td>${c.categorie}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${c.pourcentageReduction != null}">${c.pourcentageReduction}%</c:when>
                                    <c:otherwise>—</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/categorie-passager/edit/${c.id}" class="btn btn-sm btn-info">Modifier</a>
                                <form method="post" action="${pageContext.request.contextPath}/categorie-passager/delete/${c.id}" style="display:inline">
                                    <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Supprimer ?')">Supprimer</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
                <c:if test="${empty categories}">
                    <tfoot><tr><td colspan="4" class="text-muted text-center">Aucune catégorie.</td></tr></tfoot>
                </c:if>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
