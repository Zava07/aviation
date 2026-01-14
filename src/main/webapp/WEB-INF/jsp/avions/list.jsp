<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title"><i class="fas fa-plane"></i> Liste des Avions
            <span class="badge badge-primary">${empty avions ? 0 : avions.size()} avion(s)</span>
        </h3>
        <a href="${pageContext.request.contextPath}/avions/create" class="btn btn-primary">
            <i class="fas fa-plus"></i> Nouveau Avion
        </a>
    </div>
    <div class="card-body">
        <div class="mb-3">
            <form method="get" action="${pageContext.request.contextPath}/avions" class="form-inline">
                <input type="text" name="codeAvion" value="${param.codeAvion}" placeholder="Code avion" class="form-control mr-2" />
                <select name="modeleId" class="form-control mr-2">
                    <option value="">-- Modèle --</option>
                    <c:forEach var="m" items="${modeles}">
                        <option value="${m.id}" <c:if test="${param.modeleId == m.id}">selected</c:if>>${m.nomModele}</option>
                    </c:forEach>
                </select>
                <input type="number" name="minCap" value="${param.minCap}" placeholder="Cap min" class="form-control mr-2" style="width:110px" />
                <input type="number" name="maxCap" value="${param.maxCap}" placeholder="Cap max" class="form-control mr-2" style="width:110px" />
                <select name="etatId" class="form-control mr-2">
                    <option value="">-- État --</option>
                    <c:forEach var="e" items="${etats}">
                        <option value="${e.id}" <c:if test="${param.etatId == e.id}">selected</c:if>>${e.etat}</option>
                    </c:forEach>
                </select>
                <button class="btn btn-sm btn-primary" type="submit">Rechercher</button>
                <a href="${pageContext.request.contextPath}/avions" class="btn btn-sm btn-secondary ml-2">Réinitialiser</a>
            </form>
        </div>
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                <tr>
                    <th>Code</th>
                    <th>Modèle</th>
                    <th>Capacité P.</th>
                    <th>Capacité Cargo</th>
                    <th>État</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty avions}">
                        <c:forEach var="avion" items="${avions}">
                            <tr>
                                <td>${avion.codeAvion}</td>
                                <td>${avion.modeleAvion != null ? avion.modeleAvion.nomModele : ''}</td>
                                <td>${avion.capacitePassagers}</td>
                                <td>${avion.capaciteCargo}</td>
                                <td>${avion.etatAvion != null ? avion.etatAvion.etat : ''}</td>
                                <td class="table-actions">
                                    <a href="${pageContext.request.contextPath}/avions/edit/${avion.id}" class="btn btn-sm btn-info">Voir</a>
                                    <a href="${pageContext.request.contextPath}/avions/delete/${avion.id}" class="btn btn-sm btn-danger" onclick="return confirm('Êtes-vous sûr ?')">Supprimer</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="6" class="text-muted text-center">Aucun avion trouvé pour ces critères.</td></tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>