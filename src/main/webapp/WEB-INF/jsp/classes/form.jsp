<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header"><h3>Formulaire Classe</h3></div>
    <div class="card-body">
        <form method="post" action="${pageContext.request.contextPath}/classes/save">
            <input type="hidden" name="id" value="${editClasse.id}" />
            <div>
                <label>Nom</label>
                <input type="text" name="nomClasse" value="${editClasse.nomClasse}" required />
            </div>
            <div>
                <label>Niveau</label>
                <input type="number" name="niveau" value="${editClasse.niveau}" />
            </div>
            <div style="margin-top:10px">
                <button type="submit" class="btn btn-primary">Enregistrer</button>
                <a href="${pageContext.request.contextPath}/classes" class="btn btn-secondary">Annuler</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
