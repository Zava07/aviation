<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header"><h3>Formulaire Prix Vol/Classe</h3></div>
    <div class="card-body">
        <form method="post" action="${pageContext.request.contextPath}/prix/save">
            <input type="hidden" name="id" value="${editPrix.id}" />
            <div>
                <label>Vol</label>
                <select name="vol.id" required>
                    <option value="">-- Choisir --</option>
                    <c:forEach var="v" items="${vols}">
                        <option value="${v.id}" ${editPrix.vol != null && editPrix.vol.id == v.id ? 'selected' : ''}>${v.numeroVol}</option>
                    </c:forEach>
                </select>
            </div>
            <div>
                <label>Classe</label>
                <select name="classe.id" required>
                    <option value="">-- Choisir --</option>
                    <c:forEach var="c1" items="${classes}">
                        <option value="${c1.id}" ${editPrix.classe != null && editPrix.classe.id == c1.id ? 'selected' : ''}>${c1.nomClasse}</option>
                    </c:forEach>
                </select>
            </div>
            <div>
                <label>Prix</label>
                <input type="number" step="0.01" name="prix" value="${editPrix.prix}" required />
            </div>
            <div style="margin-top:10px">
                <button type="submit" class="btn btn-primary">Enregistrer</button>
                <a href="${pageContext.request.contextPath}/prix" class="btn btn-secondary">Annuler</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
