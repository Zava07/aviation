<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<style>
    .form-container { padding: 20px; background: #f4f6f9; min-height: 100vh; }
    .page-header { margin-bottom: 25px; }
    .page-header h2 { margin: 0; color: #2c3e50; font-size: 1.6rem; display: flex; align-items: center; gap: 12px; }
    .page-header h2 i { color: #9b59b6; }
    
    .form-card { background: white; border-radius: 12px; box-shadow: 0 2px 10px rgba(0,0,0,0.06); overflow: hidden; max-width: 600px; }
    .card-head { padding: 15px 20px; background: linear-gradient(135deg, #9b59b6, #8e44ad); color: white; }
    .card-head h3 { margin: 0; font-size: 1rem; }
    .card-body { padding: 25px; }
    
    .form-group { margin-bottom: 20px; }
    .form-group label { display: block; margin-bottom: 8px; font-weight: 600; color: #2c3e50; }
    .form-group input, .form-group textarea { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 0.95rem; }
    .form-group input:focus, .form-group textarea:focus { outline: none; border-color: #9b59b6; box-shadow: 0 0 0 3px rgba(155,89,182,0.1); }
    .form-group textarea { resize: vertical; min-height: 100px; }
    
    .btn-group { display: flex; gap: 12px; margin-top: 25px; }
    .btn-submit { padding: 12px 24px; background: linear-gradient(135deg, #9b59b6, #8e44ad); color: white; border: none; border-radius: 8px; font-weight: 600; cursor: pointer; }
    .btn-submit:hover { box-shadow: 0 4px 15px rgba(155,89,182,0.4); }
    .btn-cancel { padding: 12px 24px; background: #ecf0f1; color: #7f8c8d; border: none; border-radius: 8px; font-weight: 600; text-decoration: none; }
    .btn-cancel:hover { background: #bdc3c7; color: white; }
</style>

<div class="form-container">
    <div class="page-header">
        <h2><i class="fas fa-box-open"></i> ${pageTitle}</h2>
    </div>
    
    <div class="form-card">
        <div class="card-head"><h3><c:choose><c:when test="${not empty editProduit}">Modifier le produit</c:when><c:otherwise>Nouveau produit</c:otherwise></c:choose></h3></div>
        <div class="card-body">
            <form method="post" action="${pageContext.request.contextPath}/produits-extra/save">
                <c:if test="${not empty editProduit}">
                    <input type="hidden" name="id" value="${editProduit.id}" />
                </c:if>
                
                <div class="form-group">
                    <label for="nomProduit">Nom du Produit *</label>
                    <input type="text" id="nomProduit" name="nomProduit" value="${editProduit.nomProduit}" required placeholder="Ex: Sandwich, Boisson..." />
                </div>
                
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" name="description" placeholder="Description du produit...">${editProduit.description}</textarea>
                </div>
                
                <div class="form-group">
                    <label for="prix">Prix Unitaire (MGA) *</label>
                    <input type="number" id="prix" name="prix" value="${editProduit.prix}" required step="0.01" min="0" placeholder="0.00" />
                </div>
                
                <div class="btn-group">
                    <button type="submit" class="btn-submit"><i class="fas fa-save"></i> Enregistrer</button>
                    <a href="${pageContext.request.contextPath}/produits-extra" class="btn-cancel">Annuler</a>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
