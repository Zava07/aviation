<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title"><i class="fas fa-building"></i> Sociétés Publicitaires</h3>
    </div>
    <div class="card-body">
        <div class="form-section mb-4">
            <h4><i class="fas fa-plus-circle"></i>
                <c:choose>
                    <c:when test="${isEdit}">Modifier la société</c:when>
                    <c:otherwise>Ajouter une société</c:otherwise>
                </c:choose>
            </h4>
            <form method="post" action="${pageContext.request.contextPath}/publicites/societes/save" class="inline-form">
                <input type="hidden" name="id" value="${societe.id != null ? societe.id : ''}" />
                <div class="form-group">
                    <label for="nomSociete">Nom de la société</label>
                    <input type="text" id="nomSociete" name="nomSociete" class="form-control" required placeholder="Nom de la société" value="${societe.nomSociete != null ? societe.nomSociete : ''}">
                </div>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i>
                    <c:choose>
                        <c:when test="${isEdit}">Mettre à jour</c:when>
                        <c:otherwise>Enregistrer</c:otherwise>
                    </c:choose>
                </button>
                <c:if test="${isEdit}">
                    <a href="${pageContext.request.contextPath}/publicites/societes" class="btn btn-secondary">
                        <i class="fas fa-undo"></i> Annuler
                    </a>
                </c:if>
            </form>
        </div>

        <h4 class="mb-3"><i class="fas fa-list"></i> Liste des sociétés</h4>
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nom</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${societes}">
                        <tr>
                            <td>#${item.id}</td>
                            <td>${item.nomSociete}</td>
                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/publicites/societes?id=${item.id}" class="btn btn-sm btn-info"><i class="fas fa-edit"></i></a>
                                <form method="post" action="${pageContext.request.contextPath}/publicites/societes/delete/${item.id}" onsubmit="return confirm('Supprimer cette société ?');">
                                    <button type="submit" class="btn btn-sm btn-danger"><i class="fas fa-trash"></i></button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
                <c:if test="${empty societes}">
                    <tfoot>
                        <tr>
                            <td colspan="3" class="text-muted text-center">Aucune société enregistrée.</td>
                        </tr>
                    </tfoot>
                </c:if>
            </table>
        </div>
    </div>
</div>

<style>
    .inline-form {
        display: flex;
        gap: 10px;
        align-items: center;
    }
    .inline-form .form-control {
        max-width: 280px;
    }
    .form-section {
        background: var(--bg-light, #f8f9fa);
        padding: 20px;
        border-radius: 8px;
    }
    .form-section h4 {
        margin-bottom: 15px;
        color: var(--primary-color, #1e3a5f);
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
