<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title"> <c:choose><c:when test="${not empty editInstance}">Modifier Instance</c:when><c:otherwise>Nouvelle Instance</c:otherwise></c:choose></h3>
    </div>
    <div class="card-body">
        <form method="post" action="${pageContext.request.contextPath}/vol-instances/save">
            <input type="hidden" name="id" value="${editInstance != null ? editInstance.id : ''}" />

            <div class="form-group">
                <label>Vol (route)</label>
                <select name="vol.id" required class="form-control">
                    <option value="">-- Sélectionner --</option>
                    <c:forEach var="v" items="${volRoutes}">
                        <option value="${v.id}" ${editInstance != null and editInstance.vol != null and editInstance.vol.id == v.id ? 'selected' : ''}>${v.numeroVol} (${v.aeroportOrigine.nomAeroport} → ${v.aeroportDestination.nomAeroport})</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label>Avion</label>
                <select name="avion.id" required class="form-control">
                    <option value="">-- Sélectionner --</option>
                    <c:forEach var="a" items="${avions}">
                        <option value="${a.id}" ${editInstance != null and editInstance.avion != null and editInstance.avion.id == a.id ? 'selected' : ''}>${a.codeAvion}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label>Date Heure Départ</label>
                <input type="datetime-local" name="dateDepart" required class="form-control" value="${editInstance != null ? editInstance.dateDepart : ''}" />
            </div>

            <div class="form-group">
                <label>Date Heure Arrivée</label>
                <input type="datetime-local" name="dateArrivee" required class="form-control" value="${editInstance != null ? editInstance.dateArrivee : ''}" />
            </div>

            <div class="form-group">
                <label>Statut</label>
                <select name="statusVol.id" class="form-control">
                    <option value="">-- Sélectionner --</option>
                    <c:forEach var="s" items="${statusList}">
                        <option value="${s.id}" ${editInstance != null and editInstance.statusVol != null and editInstance.statusVol.id == s.id ? 'selected' : ''}>${s.status}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group mt-3">
                <a href="${pageContext.request.contextPath}/vol-instances" class="btn btn-secondary">Annuler</a>
                <button type="submit" class="btn btn-primary">Enregistrer</button>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>