<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title"><i class="fas fa-plane"></i> <c:choose><c:when test="${not empty editAvion}">Modifier Avion</c:when><c:otherwise>Nouveau Avion</c:otherwise></c:choose></h3>
    </div>
    <div class="card-body">
        <form method="post" action="${pageContext.request.contextPath}/avions/save">
            <input type="hidden" name="id" value="${editAvion != null ? editAvion.id : ''}" />

            <div class="form-group">
                <label>Code Avion</label>
                <input type="text" name="codeAvion" required class="form-control" value="${editAvion != null ? editAvion.codeAvion : ''}">
            </div>

            <div class="form-group">
                <label>Modèle</label>
                <select name="modeleAvion.id" required class="form-control">
                    <option value="">-- Sélectionner --</option>
                    <c:forEach var="m" items="${modeles}">
                        <option value="${m.id}" ${editAvion != null and editAvion.modeleAvion != null and editAvion.modeleAvion.id == m.id ? 'selected' : ''}>${m.nomModele}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label>Capacité Passagers</label>
                <input type="number" name="capacitePassagers" required class="form-control" value="${editAvion != null ? editAvion.capacitePassagers : ''}">
            </div>

            <div class="form-group">
                <label>Sièges par Classe</label>
                <div class="seats-by-class">
                    <c:forEach var="c" items="${classes}">
                        <div class="form-row">
                            <label style="width:60%">${c.nomClasse} </label> </br>
                            <input type="number" min="0" name="seatsByClass_${c.id}" class="form-control" style="width:35%" value="" />
                        </div>
                    </c:forEach>
                    <small class="form-text text-muted">La somme des champs ci-dessus doit être égale à la capacité totale.</small>
                </div>
            </div>

            <div class="form-group">
                <label>Capacité Cargo</label>
                <input type="number" step="0.01" name="capaciteCargo" required class="form-control" value="${editAvion != null ? editAvion.capaciteCargo : ''}">
            </div>

            <div class="form-group">
                <label>État</label>
                <select name="etatAvion.id" required class="form-control">
                    <option value="">-- Sélectionner --</option>
                    <c:forEach var="e" items="${etats}">
                        <option value="${e.id}" ${editAvion != null and editAvion.etatAvion != null and editAvion.etatAvion.id == e.id ? 'selected' : ''}>${e.etat}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group mt-3">
                <a href="${pageContext.request.contextPath}/avions" class="btn btn-secondary">Annuler</a>
                <button type="submit" class="btn btn-primary">Enregistrer</button>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>

<script>
    (function(){
        const form = document.querySelector('form[action$="/avions/save"]');
        if (!form) return;
        form.addEventListener('submit', function(e){
            const capacityEl = form.querySelector('input[name="capacitePassagers"]');
            const capacity = parseInt(capacityEl ? capacityEl.value || '0' : '0', 10);
            let sum = 0;
            [...form.elements].forEach(function(el){
                if (el.name && el.name.startsWith('seatsByClass_')){
                    const v = parseInt(el.value || '0', 10);
                    if (!isNaN(v)) sum += v;
                }
            });
            if (sum !== capacity) {
                e.preventDefault();
                alert('La somme des sièges par classe ('+sum+') doit être égale à la capacité totale ('+capacity+').');
            }
        });
    })();
</script>