<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Liste des Prix Vol/Classe</h3>
        <a href="${pageContext.request.contextPath}/prix/create" class="btn btn-primary">Nouveau</a>
    </div>
    <div class="card-body">
        <div class="mb-3">
            <form method="get" action="${pageContext.request.contextPath}/prix" class="form-inline">
                <select name="volId" class="form-control mr-2">
                    <option value="">-- Vol --</option>
                    <c:forEach var="v" items="${vols}">
                        <option value="${v.id}" <c:if test="${param.volId == v.id}">selected</c:if>>${v.numeroVol != null ? v.numeroVol : v.id}</option>
                    </c:forEach>
                </select>
                <select name="classeId" class="form-control mr-2">
                    <option value="">-- Classe --</option>
                    <c:forEach var="c" items="${classes}">
                        <option value="${c.id}" <c:if test="${param.classeId == c.id}">selected</c:if>>${c.nomClasse}</option>
                    </c:forEach>
                </select>
                <input type="number" step="0.01" name="minPrice" value="${param.minPrice}" placeholder="Prix min" class="form-control mr-2" />
                <input type="number" step="0.01" name="maxPrice" value="${param.maxPrice}" placeholder="Prix max" class="form-control mr-2" />
                <button class="btn btn-sm btn-primary" type="submit">Rechercher</button>
                <a href="${pageContext.request.contextPath}/prix" class="btn btn-sm btn-secondary ml-2">Réinitialiser</a>
            </form>
        </div>
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                <tr><th>Vol</th><th>Classe</th><th>Prix</th><th>Actions</th></tr>
                </thead>
                <tbody>
                <c:forEach var="p" items="${prixList}">
                    <tr>
                        <td>${p.vol != null ? p.vol.numeroVol : ''}</td>
                        <td>${p.classe != null ? p.classe.nomClasse : ''}</td>
                        <td>${p.prix}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/prix/edit/${p.id}" class="btn btn-sm btn-info">Modifier</a>
                            <form method="post" action="${pageContext.request.contextPath}/prix/delete/${p.id}" style="display:inline">
                                <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Supprimer ?')">Supprimer</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
