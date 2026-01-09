<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Liste des Classes</h3>
        <a href="${pageContext.request.contextPath}/classes/create" class="btn btn-primary">Nouveau</a>
    </div>
    <div class="card-body">
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
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
