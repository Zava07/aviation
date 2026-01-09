<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Liste des Sièges</h3>
        <a href="${pageContext.request.contextPath}/sieges/create" class="btn btn-primary">Nouveau</a>
    </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                <tr><th>Numéro</th><th>Avion</th><th>Classe</th><th>Actions</th></tr>
                </thead>
                <tbody>
                <c:forEach var="s" items="${sieges}">
                    <tr>
                        <td>${s.numero}</td>
                        <td>${s.avion != null ? s.avion.codeAvion : ''}</td>
                        <td>${s.classe != null ? s.classe.nomClasse : ''}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/sieges/edit/${s.id}" class="btn btn-sm btn-info">Modifier</a>
                            <form method="post" action="${pageContext.request.contextPath}/sieges/delete/${s.id}" style="display:inline">
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
