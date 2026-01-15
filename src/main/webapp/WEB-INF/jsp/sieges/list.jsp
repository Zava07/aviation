<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Liste des Sièges</h3>
        <a href="${pageContext.request.contextPath}/sieges/create" class="btn btn-primary">Nouveau</a>
    </div>
    <div class="card-body">
        <div class="mb-3">
            <form method="get" action="${pageContext.request.contextPath}/sieges" class="form-inline">
                <input type="text" name="numero" value="${param.numero}" placeholder="Numéro" class="form-control mr-2" />
                <select name="avionId" class="form-control mr-2">
                    <option value="">-- Avion --</option>
                    <c:forEach var="a" items="${avions}">
                        <option value="${a.id}" <c:if test="${param.avionId == a.id}">selected</c:if>>${a.codeAvion}</option>
                    </c:forEach>
                </select>
                <select name="classeId" class="form-control mr-2">
                    <option value="">-- Classe --</option>
                    <c:forEach var="c" items="${classes}">
                        <option value="${c.id}" <c:if test="${param.classeId == c.id}">selected</c:if>>${c.nomClasse}</option>
                    </c:forEach>
                </select>
                <button class="btn btn-sm btn-primary" type="submit">Rechercher</button>
                <a href="${pageContext.request.contextPath}/sieges" class="btn btn-sm btn-secondary ml-2">Réinitialiser</a>
            </form>
        </div>
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
                <c:if test="${empty sieges}">
                    <tfoot>
                        <tr><td colspan="4" class="text-muted text-center">Aucun siège trouvé pour ces critères.</td></tr>
                    </tfoot>
                </c:if>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
