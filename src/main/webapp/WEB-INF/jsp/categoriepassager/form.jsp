<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header"><h3>Formulaire Catégorie Passager</h3></div>
    <div class="card-body">
        <form method="post" action="${pageContext.request.contextPath}/categorie-passager/save">
            <input type="hidden" name="id" value="${editCategorie.id}" />
            <div>
                <label>Catégorie</label>
                <input type="text" name="categorie" value="${editCategorie.categorie}" required />
            </div>
            <div style="margin-top:10px">
                <label>Pourcentage réduction (%)</label>
                <input type="number" name="pourcentageReduction" step="0.01" min="0" max="100" value="${editCategorie.pourcentageReduction}" />
                <small class="text-muted">Laisser vide si pas de réduction (NULL)</small>
            </div>
            <div style="margin-top:10px">
                <button type="submit" class="btn btn-primary">Enregistrer</button>
                <a href="${pageContext.request.contextPath}/categorie-passager" class="btn btn-secondary">Annuler</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
