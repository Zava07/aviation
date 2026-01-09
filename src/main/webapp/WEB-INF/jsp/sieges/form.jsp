<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header"><h3>Formulaire Siège</h3></div>
    <div class="card-body">
        <form method="post" action="${pageContext.request.contextPath}/sieges/save">
            <input type="hidden" name="id" value="${editSiege.id}" />
            <div>
                <label>Numéro</label>
                <input type="text" name="numero" value="${editSiege.numero}" required />
            </div>
            <div>
                <label>Avion</label>
                <select name="avion.id" required>
                    <option value="">-- Choisir --</option>
                    <c:forEach var="a" items="${avions}">
                        <option value="${a.id}" ${editSiege.avion != null && editSiege.avion.id == a.id ? 'selected' : ''}>${a.codeAvion}</option>
                    </c:forEach>
                </select>
            </div>
            <div>
                <label>Classe</label>
                <select name="classe.id" required>
                    <option value="">-- Choisir --</option>
                    <c:forEach var="c1" items="${classes}">
                        <option value="${c1.id}" ${editSiege.classe != null && editSiege.classe.id == c1.id ? 'selected' : ''}>${c1.nomClasse}</option>
                    </c:forEach>
                </select>
            </div>
            <div style="margin-top:10px">
                <button type="submit" class="btn btn-primary">Enregistrer</button>
                <a href="${pageContext.request.contextPath}/sieges" class="btn btn-secondary">Annuler</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
