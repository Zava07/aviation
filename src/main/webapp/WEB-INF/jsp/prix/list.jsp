<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Liste des Prix Vol/Classe</h3>
        <a href="${pageContext.request.contextPath}/prix/create" class="btn btn-primary">Nouveau</a>
    </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                <tr><th>Vol</th><th>Classe</th><th>Prix</th><th>Actions</th></tr>
                </thead>
                <tbody>
                <c:forEach var="p" items="${prixList}">
                    <tr>
                        <td>${p.vol != null ? p.vol.numeroVol : ''}</td>
                        <td>${p.classe != null ? p.classe.nomClasse : ''}</td>
                        <td>${p.prix}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/prix/edit/${p.id}" class="btn btn-sm btn-info">Modifier</a>
                            <form method="post" action="${pageContext.request.contextPath}/prix/delete/${p.id}" style="display:inline">
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
