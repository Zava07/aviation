<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<style>
    .produits-container { padding: 20px; background: #f4f6f9; min-height: 100vh; }
    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; }
    .page-header h2 { margin: 0; color: #2c3e50; font-size: 1.6rem; display: flex; align-items: center; gap: 12px; }
    .page-header h2 i { color: #9b59b6; }
    .badge-count { background: linear-gradient(135deg, #9b59b6, #8e44ad); color: white; padding: 5px 12px; border-radius: 15px; font-size: 0.85rem; }
    .btn-primary-custom { padding: 10px 20px; background: linear-gradient(135deg, #9b59b6, #8e44ad); color: white; border: none; border-radius: 8px; font-weight: 600; text-decoration: none; display: flex; align-items: center; gap: 6px; }
    .btn-primary-custom:hover { color: white; box-shadow: 0 4px 15px rgba(155,89,182,0.4); }
    
    .filter-card { background: white; border-radius: 12px; padding: 15px 20px; margin-bottom: 20px; box-shadow: 0 2px 10px rgba(0,0,0,0.06); }
    .filter-form { display: flex; gap: 12px; align-items: center; flex-wrap: wrap; }
    .filter-form input { padding: 8px 12px; border: 1px solid #ddd; border-radius: 6px; font-size: 0.9rem; min-width: 250px; }
    .filter-form input:focus { outline: none; border-color: #9b59b6; }
    .btn-filter { padding: 8px 16px; border-radius: 6px; font-weight: 500; border: none; cursor: pointer; font-size: 0.9rem; }
    .btn-filter.primary { background: #9b59b6; color: white; }
    .btn-filter.secondary { background: #ecf0f1; color: #7f8c8d; text-decoration: none; }
    
    .content-card { background: white; border-radius: 12px; box-shadow: 0 2px 10px rgba(0,0,0,0.06); overflow: hidden; }
    .card-head { padding: 15px 20px; background: linear-gradient(135deg, #2c3e50, #34495e); color: white; }
    .card-head h3 { margin: 0; font-size: 1rem; display: flex; align-items: center; gap: 8px; }
    
    .data-table { width: 100%; border-collapse: collapse; }
    .data-table thead th { background: #f8f9fa; padding: 12px 15px; text-align: left; font-weight: 600; color: #495057; font-size: 0.8rem; text-transform: uppercase; border-bottom: 2px solid #dee2e6; }
    .data-table tbody td { padding: 14px 15px; border-bottom: 1px solid #eee; vertical-align: middle; }
    .data-table tbody tr:hover { background: #f8f9fa; }
    
    .produit-nom { font-weight: 700; color: #2c3e50; }
    .produit-prix { color: #27ae60; font-weight: 600; }
    .produit-desc { color: #7f8c8d; font-size: 0.85rem; max-width: 300px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
    
    .actions-cell { display: flex; gap: 6px; }
    .btn-action { padding: 6px 10px; border-radius: 6px; font-size: 0.75rem; font-weight: 600; text-decoration: none; border: none; cursor: pointer; }
    .btn-action.info { background: #ebf5fb; color: #3498db; }
    .btn-action.info:hover { background: #3498db; color: white; }
    .btn-action.danger { background: #fdedec; color: #e74c3c; }
    .btn-action.danger:hover { background: #e74c3c; color: white; }
    
    .empty-state { text-align: center; padding: 40px 20px; color: #95a5a6; }
    .empty-state i { font-size: 2.5rem; margin-bottom: 10px; opacity: 0.5; }
</style>

<div class="produits-container">
    <div class="page-header">
        <h2><i class="fas fa-box-open"></i> Produits Extra <span class="badge-count">${empty produits ? 0 : produits.size()} produit(s)</span></h2>
        <a href="${pageContext.request.contextPath}/produits-extra/create" class="btn-primary-custom"><i class="fas fa-plus"></i> Nouveau Produit</a>
    </div>
    
    <div class="filter-card">
        <form method="get" action="${pageContext.request.contextPath}/produits-extra" class="filter-form">
            <input type="text" name="q" value="${filterQ}" placeholder="Rechercher un produit..." />
            <button type="submit" class="btn-filter primary"><i class="fas fa-search"></i> Rechercher</button>
            <a href="${pageContext.request.contextPath}/produits-extra" class="btn-filter secondary"><i class="fas fa-undo"></i> Réinitialiser</a>
        </form>
    </div>
    
    <div class="content-card">
        <div class="card-head"><h3><i class="fas fa-list"></i> Liste des Produits</h3></div>
        <table class="data-table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Nom du Produit</th>
                    <th>Description</th>
                    <th>Prix Unitaire</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty produits}">
                        <c:forEach var="p" items="${produits}">
                            <tr>
                                <td>${p.id}</td>
                                <td><span class="produit-nom">${p.nomProduit}</span></td>
                                <td><span class="produit-desc" title="${p.description}">${p.description}</span></td>
                                <td><span class="produit-prix"><fmt:formatNumber value="${p.prix}" type="number" groupingUsed="true"/> MGA</span></td>
                                <td>
                                    <div class="actions-cell">
                                        <a href="${pageContext.request.contextPath}/produits-extra/edit/${p.id}" class="btn-action info"><i class="fas fa-edit"></i> Modifier</a>
                                        <form method="post" action="${pageContext.request.contextPath}/produits-extra/delete/${p.id}" style="display:inline">
                                            <button type="submit" class="btn-action danger" onclick="return confirm('Confirmer la suppression ?')"><i class="fas fa-trash"></i> Supprimer</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="5"><div class="empty-state"><i class="fas fa-box-open"></i><p>Aucun produit trouvé</p></div></td></tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
