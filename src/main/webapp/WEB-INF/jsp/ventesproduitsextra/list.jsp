<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<style>
    .ventes-container { padding: 20px; background: #f4f6f9; min-height: 100vh; }
    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; }
    .page-header h2 { margin: 0; color: #2c3e50; font-size: 1.6rem; display: flex; align-items: center; gap: 12px; }
    .page-header h2 i { color: #e67e22; }
    .badge-count { background: linear-gradient(135deg, #e67e22, #d35400); color: white; padding: 5px 12px; border-radius: 15px; font-size: 0.85rem; }
    .btn-primary-custom { padding: 10px 20px; background: linear-gradient(135deg, #e67e22, #d35400); color: white; border: none; border-radius: 8px; font-weight: 600; text-decoration: none; display: flex; align-items: center; gap: 6px; }
    .btn-primary-custom:hover { color: white; box-shadow: 0 4px 15px rgba(230,126,34,0.4); }
    
    .filter-card { background: white; border-radius: 12px; padding: 15px 20px; margin-bottom: 20px; box-shadow: 0 2px 10px rgba(0,0,0,0.06); }
    .filter-form { display: flex; gap: 12px; align-items: center; flex-wrap: wrap; }
    .filter-form input, .filter-form select { padding: 8px 12px; border: 1px solid #ddd; border-radius: 6px; font-size: 0.9rem; }
    .filter-form input:focus, .filter-form select:focus { outline: none; border-color: #e67e22; }
    .btn-filter { padding: 8px 16px; border-radius: 6px; font-weight: 500; border: none; cursor: pointer; font-size: 0.9rem; }
    .btn-filter.primary { background: #e67e22; color: white; }
    .btn-filter.secondary { background: #ecf0f1; color: #7f8c8d; text-decoration: none; }
    
    .stats-row { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px; margin-bottom: 20px; }
    .stat-card { background: white; border-radius: 12px; padding: 20px; box-shadow: 0 2px 10px rgba(0,0,0,0.06); }
    .stat-card.orange { background: linear-gradient(135deg, #e67e22, #d35400); color: white; }
    .stat-card.green { background: linear-gradient(135deg, #27ae60, #2ecc71); color: white; }
    .stat-card h4 { margin: 0 0 8px 0; font-size: 0.8rem; text-transform: uppercase; opacity: 0.9; }
    .stat-card .value { font-size: 1.5rem; font-weight: 700; }
    
    .content-card { background: white; border-radius: 12px; box-shadow: 0 2px 10px rgba(0,0,0,0.06); overflow: hidden; }
    .card-head { padding: 15px 20px; background: linear-gradient(135deg, #2c3e50, #34495e); color: white; }
    .card-head h3 { margin: 0; font-size: 1rem; display: flex; align-items: center; gap: 8px; }
    
    .data-table { width: 100%; border-collapse: collapse; }
    .data-table thead th { background: #f8f9fa; padding: 12px 15px; text-align: left; font-weight: 600; color: #495057; font-size: 0.8rem; text-transform: uppercase; border-bottom: 2px solid #dee2e6; }
    .data-table tbody td { padding: 14px 15px; border-bottom: 1px solid #eee; vertical-align: middle; }
    .data-table tbody tr:hover { background: #f8f9fa; }
    
    .produit-badge { display: inline-flex; align-items: center; gap: 6px; padding: 4px 10px; background: #f5eef8; color: #9b59b6; border-radius: 6px; font-size: 0.85rem; font-weight: 600; }
    .vol-badge { display: inline-flex; align-items: center; gap: 6px; padding: 4px 10px; background: #ebf5fb; color: #3498db; border-radius: 6px; font-size: 0.85rem; }
    .quantite { font-weight: 700; color: #2c3e50; }
    .prix { color: #27ae60; font-weight: 600; }
    .date { color: #7f8c8d; font-size: 0.85rem; }
    
    .actions-cell { display: flex; gap: 6px; }
    .btn-action { padding: 6px 10px; border-radius: 6px; font-size: 0.75rem; font-weight: 600; text-decoration: none; border: none; cursor: pointer; }
    .btn-action.info { background: #ebf5fb; color: #3498db; }
    .btn-action.info:hover { background: #3498db; color: white; }
    .btn-action.danger { background: #fdedec; color: #e74c3c; }
    .btn-action.danger:hover { background: #e74c3c; color: white; }
    
    .empty-state { text-align: center; padding: 40px 20px; color: #95a5a6; }
    .empty-state i { font-size: 2.5rem; margin-bottom: 10px; opacity: 0.5; }
    
    .total-row { background: #f8f9fa !important; font-weight: 700; }
    .total-row td { border-top: 2px solid #dee2e6; }
</style>

<div class="ventes-container">
    <div class="page-header">
        <h2><i class="fas fa-shopping-cart"></i> Ventes Produits Extra <span class="badge-count">${empty ventes ? 0 : ventes.size()} vente(s)</span></h2>
        <a href="${pageContext.request.contextPath}/ventes-produits-extra/create" class="btn-primary-custom"><i class="fas fa-plus"></i> Nouvelle Vente</a>
    </div>
    
    <div class="stats-row">
        <div class="stat-card orange">
            <h4><i class="fas fa-chart-line"></i> Total Ventes</h4>
            <div class="value"><fmt:formatNumber value="${totalVentes}" type="number" groupingUsed="true"/> MGA</div>
        </div>
        <div class="stat-card green">
            <h4><i class="fas fa-cubes"></i> Quantité Totale</h4>
            <div class="value">${totalQuantite}</div>
        </div>
    </div>
    
    <div class="filter-card">
        <form method="get" action="${pageContext.request.contextPath}/ventes-produits-extra" class="filter-form">
            <select name="produitId">
                <option value="">Tous les produits</option>
                <c:forEach var="p" items="${produits}">
                    <option value="${p.id}" <c:if test="${filterProduitId == p.id}">selected</c:if>>${p.nomProduit}</option>
                </c:forEach>
            </select>
            <select name="volInstanceId">
                <option value="">Tous les vols</option>
                <c:forEach var="vi" items="${volInstances}">
                    <option value="${vi.id}" <c:if test="${filterVolInstanceId == vi.id}">selected</c:if>>
                        ${vi.vol != null ? vi.vol.numeroVol : 'Vol'} - ${vi.dateDepart}
                    </option>
                </c:forEach>
            </select>
            <input type="date" name="date" value="${filterDate}" />
            <button type="submit" class="btn-filter primary"><i class="fas fa-search"></i> Filtrer</button>
            <a href="${pageContext.request.contextPath}/ventes-produits-extra" class="btn-filter secondary"><i class="fas fa-undo"></i> Réinitialiser</a>
        </form>
    </div>
    
    <div class="content-card">
        <div class="card-head"><h3><i class="fas fa-list"></i> Liste des Ventes</h3></div>
        <table class="data-table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Produit</th>
                    <th>Vol</th>
                    <th>Quantité</th>
                    <th>Prix Total</th>
                    <th>Date d'Achat</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty ventes}">
                        <c:forEach var="v" items="${ventes}">
                            <tr>
                                <td>${v.id}</td>
                                <td><span class="produit-badge"><i class="fas fa-box"></i> ${v.produitExtra != null ? v.produitExtra.nomProduit : '—'}</span></td>
                                <td>
                                    <c:if test="${v.volInstance != null}">
                                        <span class="vol-badge">
                                            <i class="fas fa-plane"></i>
                                            ${v.volInstance.vol != null ? v.volInstance.vol.numeroVol : ''} - 
                                            <fmt:parseDate value="${v.volInstance.dateDepart}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                                            <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm" />
                                        </span>
                                    </c:if>
                                </td>
                                <td><span class="quantite">${v.quantite}</span></td>
                                <td><span class="prix"><fmt:formatNumber value="${v.prixTotal}" type="number" groupingUsed="true"/> MGA</span></td>
                                <td>
                                    <span class="date">
                                        <c:if test="${v.dateAchat != null}">
                                            <fmt:parseDate value="${v.dateAchat}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedAchat" type="both" />
                                            <fmt:formatDate value="${parsedAchat}" pattern="dd/MM/yyyy HH:mm" />
                                        </c:if>
                                    </span>
                                </td>
                                <td>
                                    <div class="actions-cell">
                                        <a href="${pageContext.request.contextPath}/ventes-produits-extra/edit/${v.id}" class="btn-action info"><i class="fas fa-edit"></i> Modifier</a>
                                        <form method="post" action="${pageContext.request.contextPath}/ventes-produits-extra/delete/${v.id}" style="display:inline">
                                            <button type="submit" class="btn-action danger" onclick="return confirm('Confirmer la suppression ?')"><i class="fas fa-trash"></i> Supprimer</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr class="total-row">
                            <td colspan="3" style="text-align:right"><strong>Total</strong></td>
                            <td><strong>${totalQuantite}</strong></td>
                            <td><strong class="prix"><fmt:formatNumber value="${totalVentes}" type="number" groupingUsed="true"/> MGA</strong></td>
                            <td colspan="2"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="7"><div class="empty-state"><i class="fas fa-shopping-cart"></i><p>Aucune vente trouvée</p></div></td></tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
