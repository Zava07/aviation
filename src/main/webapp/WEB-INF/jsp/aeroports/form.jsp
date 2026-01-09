<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header"><h3>Formulaire Aéroport</h3></div>
    <div class="card-body">
        <form method="post" action="${pageContext.request.contextPath}/aeroports/save">
            <input type="hidden" name="id" value="${editAeroport.id}" />
            <div>
                <label>Nom Aéroport</label>
                <input type="text" name="nomAeroport" value="${editAeroport.nomAeroport}" required />
            </div>
            <div style="margin-top:10px">
                <button type="submit" class="btn btn-primary">Enregistrer</button>
                <a href="${pageContext.request.contextPath}/aeroports" class="btn btn-secondary">Annuler</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
