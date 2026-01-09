<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title"> <c:choose><c:when test="${not empty editVol}">Modifier Vol</c:when><c:otherwise>Nouveau Vol</c:otherwise></c:choose></h3>
    </div>
    <div class="card-body">
        <form method="post" action="${pageContext.request.contextPath}/vols/save">
            <input type="hidden" name="id" value="${editVol != null ? editVol.id : ''}" />
            <div class="form-group">
                <label>Numéro Vol</label>
                <input type="text" name="numeroVol" required class="form-control" value="${editVol != null ? editVol.numeroVol : ''}" />
            </div>
            <div class="form-group">
                <label>Avion</label>
                <select name="avion.id" required class="form-control">
                    <option value="">-- Sélectionner --</option>
                    <c:forEach var="a" items="${avions}">
                        <option value="${a.id}" ${editVol != null and editVol.avion != null and editVol.avion.id == a.id ? 'selected' : ''}>${a.codeAvion}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label>Aéroport Origine</label>
                <select name="aeroportOrigine.id" required class="form-control">
                    <option value="">-- Sélectionner --</option>
                    <c:forEach var="ap" items="${aeroports}">
                        <option value="${ap.id}" ${editVol != null and editVol.aeroportOrigine != null and editVol.aeroportOrigine.id == ap.id ? 'selected' : ''}>${ap.nomAeroport}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label>Aéroport Destination</label>
                <select name="aeroportDestination.id" required class="form-control">
                    <option value="">-- Sélectionner --</option>
                    <c:forEach var="ap" items="${aeroports}">
                        <option value="${ap.id}" ${editVol != null and editVol.aeroportDestination != null and editVol.aeroportDestination.id == ap.id ? 'selected' : ''}>${ap.nomAeroport}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label>Date Heure Départ</label>
                <input type="datetime-local" name="dateDepart" required class="form-control" value="${editVol != null ? editVol.dateDepart : ''}" />
            </div>

            <div class="form-group">
                <label>Date Heure Arrivée</label>
                <input type="datetime-local" name="dateArrivee" required class="form-control" value="${editVol != null ? editVol.dateArrivee : ''}" />
            </div>

            <div class="form-group mt-3">
                <a href="${pageContext.request.contextPath}/vols" class="btn btn-secondary">Annuler</a>
                <button type="submit" class="btn btn-primary">Enregistrer</button>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>