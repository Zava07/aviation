<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">
            <i class="fas fa-cog"></i> 
            <c:choose>
                <c:when test="${isEdit}">Modifier Configuration</c:when>
                <c:otherwise>Nouvelle Configuration</c:otherwise>
            </c:choose>
        </h3>
        <a href="${pageContext.request.contextPath}/configurations" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Retour
        </a>
    </div>
    <div class="card-body">
        <form method="post" action="${pageContext.request.contextPath}/configurations/save" class="config-form">
            <c:if test="${config.id != null}">
                <input type="hidden" name="id" value="${config.id}" />
            </c:if>

            <div class="form-section">
                <h4><i class="fas fa-star"></i> Paramètres de Fidélité</h4>
                
                <div class="form-group">
                    <label for="pointFidelite">Points de Fidélité <span class="required">*</span></label>
                    <input type="number" id="pointFidelite" name="pointFidelite" 
                           class="form-control" required min="0"
                           value="${config.pointFidelite != null ? config.pointFidelite : ''}"
                           placeholder="Nombre de points attribués par vol">
                    <small class="form-text text-muted">
                        Nombre de points de fidélité attribués aux passagers pour chaque vol effectué.
                    </small>
                </div>
            </div>

            <c:if test="${isEdit && config.dateModification != null}">
                <div class="form-section info-section">
                    <h4><i class="fas fa-info-circle"></i> Informations</h4>
                    <div class="info-item">
                        <span class="info-label">Dernière modification :</span>
                        <span class="info-value">
                            ${config.dateModification.toLocalDate()} à ${config.dateModification.toLocalTime()}
                        </span>
                    </div>
                </div>
            </c:if>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary btn-lg">
                    <i class="fas fa-save"></i> 
                    <c:choose>
                        <c:when test="${isEdit}">Mettre à jour</c:when>
                        <c:otherwise>Créer</c:otherwise>
                    </c:choose>
                </button>
                <a href="${pageContext.request.contextPath}/configurations" class="btn btn-secondary btn-lg">
                    <i class="fas fa-times"></i> Annuler
                </a>
            </div>
        </form>
    </div>
</div>

<style>
    .config-form {
        max-width: 600px;
    }
    .form-section {
        background: var(--bg-light, #f8f9fa);
        padding: 20px;
        border-radius: 8px;
        margin-bottom: 20px;
    }
    .form-section h4 {
        margin-bottom: 15px;
        color: var(--primary-color, #1e3a5f);
        border-bottom: 2px solid var(--primary-color, #1e3a5f);
        padding-bottom: 10px;
    }
    .form-section h4 i {
        margin-right: 10px;
    }
    .info-section {
        background: #e3f2fd;
    }
    .info-section h4 {
        color: #1565c0;
        border-bottom-color: #1565c0;
    }
    .info-item {
        display: flex;
        gap: 10px;
    }
    .info-label {
        font-weight: 500;
        color: #555;
    }
    .info-value {
        color: #333;
    }
    .form-group {
        margin-bottom: 15px;
    }
    .form-group label {
        display: block;
        margin-bottom: 5px;
        font-weight: 500;
        color: var(--text-primary, #333);
    }
    .form-group .required {
        color: #dc3545;
    }
    .form-control {
        width: 100%;
        padding: 10px 12px;
        border: 1px solid var(--border-color, #dee2e6);
        border-radius: 6px;
        font-size: 14px;
    }
    .form-control:focus {
        border-color: var(--primary-color, #1e3a5f);
        outline: none;
        box-shadow: 0 0 0 3px rgba(30, 58, 95, 0.1);
    }
    .form-text {
        font-size: 12px;
        color: var(--text-secondary, #6c757d);
        margin-top: 5px;
    }
    .form-actions {
        display: flex;
        gap: 15px;
        margin-top: 25px;
    }
    .btn-lg {
        padding: 12px 30px;
        font-size: 16px;
    }
    .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
</style>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
