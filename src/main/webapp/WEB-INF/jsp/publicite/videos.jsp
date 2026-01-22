<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title"><i class="fas fa-video"></i> Vidéos publicitaires</h3>
        <a href="${pageContext.request.contextPath}/publicites/societes" class="btn btn-secondary">
            <i class="fas fa-building"></i> Gérer les sociétés
        </a>
    </div>
    <div class="card-body">
        <div class="form-section mb-4">
            <h4><i class="fas fa-plus-circle"></i>
                <c:choose>
                    <c:when test="${isEdit}">Modifier la vidéo</c:when>
                    <c:otherwise>Ajouter une vidéo</c:otherwise>
                </c:choose>
            </h4>
            <form method="post" action="${pageContext.request.contextPath}/publicites/videos/save" class="grid-form">
                <input type="hidden" name="id" value="${video.id != null ? video.id : ''}" />
                <div class="form-group">
                    <label for="titre">Titre</label>
                    <input type="text" id="titre" name="titre" class="form-control" required placeholder="Titre de la vidéo" value="${video.titre != null ? video.titre : ''}">
                </div>
                <div class="form-group">
                    <label for="dureeSeconde">Durée (secondes)</label>
                    <input type="number" id="dureeSeconde" name="dureeSeconde" class="form-control" min="0" placeholder="Durée" value="${video.dureeSeconde != null ? video.dureeSeconde : ''}">
                </div>
                <div class="form-group">
                    <label for="societeId">Société</label>
                    <select id="societeId" name="societe.id" class="form-control" required>
                        <option value="">-- Sélectionner --</option>
                        <c:forEach var="societe" items="${societes}">
                            <option value="${societe.id}" <c:if test="${video.societe != null && video.societe.id == societe.id}">selected</c:if>>${societe.nomSociete}</option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i>
                    <c:choose>
                        <c:when test="${isEdit}">Mettre à jour</c:when>
                        <c:otherwise>Enregistrer</c:otherwise>
                    </c:choose>
                </button>
                <c:if test="${isEdit}">
                    <a href="${pageContext.request.contextPath}/publicites/videos" class="btn btn-secondary">
                        <i class="fas fa-undo"></i> Annuler
                    </a>
                </c:if>
            </form>
        </div>

        <h4 class="mb-3"><i class="fas fa-list"></i> Liste des vidéos</h4>
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Titre</th>
                        <th>Durée (s)</th>
                        <th>Société</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="vid" items="${videos}">
                        <tr>
                            <td>#${vid.id}</td>
                            <td>${vid.titre}</td>
                            <td>${vid.dureeSeconde != null ? vid.dureeSeconde : '-'}</td>
                            <td>${vid.societe.nomSociete}</td>
                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/publicites/videos?id=${vid.id}" class="btn btn-sm btn-info"><i class="fas fa-edit"></i></a>
                                <form method="post" action="${pageContext.request.contextPath}/publicites/videos/delete/${vid.id}" onsubmit="return confirm('Supprimer cette vidéo ?');">
                                    <button type="submit" class="btn btn-sm btn-danger"><i class="fas fa-trash"></i></button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
                <c:if test="${empty videos}">
                    <tfoot>
                        <tr>
                            <td colspan="5" class="text-muted text-center">Aucune vidéo enregistrée.</td>
                        </tr>
                    </tfoot>
                </c:if>
            </table>
        </div>
    </div>
</div>

<style>
    .grid-form {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
        gap: 12px;
        align-items: end;
    }
    .grid-form .form-group {
        display: flex;
        flex-direction: column;
        gap: 6px;
    }
    .grid-form .btn {
        height: fit-content;
    }
    .actions {
        display: flex;
        gap: 6px;
        align-items: center;
    }
    .actions form {
        display: inline;
    }
</style>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
