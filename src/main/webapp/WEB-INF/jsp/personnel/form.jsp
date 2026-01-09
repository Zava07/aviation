<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header"><h3>Formulaire Personnel</h3></div>
    <div class="card-body">
        <form method="post" action="${pageContext.request.contextPath}/personnel/save">
            <input type="hidden" name="id" value="${editPersonnel.id}" />
            <div>
                <label>Nom </label>
                <input type="text" name="nom" value="${editPersonnel.nom}" required />
            </div>
            <div>
                <label>Prenom </label>
                <input type="text" name="prenom" value="${editPersonnel.prenom}" required />
            </div>
            <div class="form-group">
                <label>Type</label>
                <select name="typeMembre.id" required class="form-control">
                    <option value="">-- Sélectionner --</option>
                    <c:forEach var="a" items="${typeMembre}">
                        <option value="${a.id}" ${editPersonnel != null and editPersonnel.typeMembre != null and editPersonnel.typeMembre.id == a.id ? 'selected' : ''}>${a.type}</option>
                    </c:forEach>
                </select>
            </div>
            <div style="margin-top:10px">
                <button type="submit" class="btn btn-primary">Enregistrer</button>
                <a href="${pageContext.request.contextPath}/modeles" class="btn btn-secondary">Annuler</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
