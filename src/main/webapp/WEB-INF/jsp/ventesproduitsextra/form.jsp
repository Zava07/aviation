<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<style>
    .form-container { padding: 20px; background: #f4f6f9; min-height: 100vh; }
    .page-header { margin-bottom: 25px; }
    .page-header h2 { margin: 0; color: #2c3e50; font-size: 1.6rem; display: flex; align-items: center; gap: 12px; }
    .page-header h2 i { color: #e67e22; }
    
    .form-card { background: white; border-radius: 12px; box-shadow: 0 2px 10px rgba(0,0,0,0.06); overflow: hidden; max-width: 700px; }
    .card-head { padding: 15px 20px; background: linear-gradient(135deg, #e67e22, #d35400); color: white; }
    .card-head h3 { margin: 0; font-size: 1rem; }
    .card-body { padding: 25px; }
    
    .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
    .form-group { margin-bottom: 20px; }
    .form-group.full { grid-column: span 2; }
    .form-group label { display: block; margin-bottom: 8px; font-weight: 600; color: #2c3e50; }
    .form-group input, .form-group select { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 0.95rem; }
    .form-group input:focus, .form-group select:focus { outline: none; border-color: #e67e22; box-shadow: 0 0 0 3px rgba(230,126,34,0.1); }
    
    .prix-info { background: #fef5e7; border: 1px solid #f39c12; border-radius: 8px; padding: 12px; margin-top: 10px; }
    .prix-info .label { font-size: 0.85rem; color: #7f8c8d; }
    .prix-info .value { font-size: 1.2rem; font-weight: 700; color: #e67e22; }
    
    .btn-group { display: flex; gap: 12px; margin-top: 25px; }
    .btn-submit { padding: 12px 24px; background: linear-gradient(135deg, #e67e22, #d35400); color: white; border: none; border-radius: 8px; font-weight: 600; cursor: pointer; }
    .btn-submit:hover { box-shadow: 0 4px 15px rgba(230,126,34,0.4); }
    .btn-cancel { padding: 12px 24px; background: #ecf0f1; color: #7f8c8d; border: none; border-radius: 8px; font-weight: 600; text-decoration: none; }
    .btn-cancel:hover { background: #bdc3c7; color: white; }
</style>

<div class="form-container">
    <div class="page-header">
        <h2><i class="fas fa-shopping-cart"></i> ${pageTitle}</h2>
    </div>
    
    <div class="form-card">
        <div class="card-head"><h3><c:choose><c:when test="${not empty editVente}">Modifier la vente</c:when><c:otherwise>Nouvelle vente</c:otherwise></c:choose></h3></div>
        <div class="card-body">
            <form method="post" action="${pageContext.request.contextPath}/ventes-produits-extra/save" id="venteForm">
                <c:if test="${not empty editVente}">
                    <input type="hidden" name="id" value="${editVente.id}" />
                </c:if>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="produitExtraId">Produit *</label>
                        <select id="produitExtraId" name="produitExtraId" required onchange="updatePrixTotal()">
                            <option value="">-- Sélectionner un produit --</option>
                            <c:forEach var="p" items="${produits}">
                                <option value="${p.id}" data-prix="${p.prix}" 
                                    <c:if test="${(not empty editVente and editVente.produitExtra.id == p.id)}">selected</c:if>>
                                    ${p.nomProduit} - <fmt:formatNumber value="${p.prix}" type="number" groupingUsed="true"/> MGA
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="volInstanceId">Instance de Vol *</label>
                        <select id="volInstanceId" name="volInstanceId" required>
                            <option value="">-- Sélectionner un vol --</option>
                            <c:forEach var="vi" items="${volInstances}">
                                <option value="${vi.id}" 
                                    <c:if test="${(not empty editVente and editVente.volInstance.id == vi.id) or (not empty preselectedVolInstanceId and preselectedVolInstanceId == vi.id)}">selected</c:if>>
                                    ${vi.vol != null ? vi.vol.numeroVol : 'Vol'} - ${vi.dateDepart}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="quantite">Quantité *</label>
                        <input type="number" id="quantite" name="quantite" value="${not empty editVente ? editVente.quantite : 1}" required min="1" onchange="updatePrixTotal()" oninput="updatePrixTotal()" />
                    </div>
                    
                    <div class="form-group">
                        <label for="prixTotal">Prix Total (MGA)</label>
                        <input type="number" id="prixTotal" name="prixTotal" value="${editVente.prixTotal}" step="0.01" min="0" placeholder="Calculé automatiquement" />
                        <div class="prix-info">
                            <span class="label">Prix calculé: </span>
                            <span class="value" id="prixCalcule">0 MGA</span>
                        </div>
                    </div>
                </div>
                
                <div class="btn-group">
                    <button type="submit" class="btn-submit"><i class="fas fa-save"></i> Enregistrer</button>
                    <a href="${pageContext.request.contextPath}/ventes-produits-extra" class="btn-cancel">Annuler</a>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function updatePrixTotal() {
    var produitSelect = document.getElementById('produitExtraId');
    var quantiteInput = document.getElementById('quantite');
    var prixTotalInput = document.getElementById('prixTotal');
    var prixCalculeSpan = document.getElementById('prixCalcule');
    
    var selectedOption = produitSelect.options[produitSelect.selectedIndex];
    var prixUnitaire = selectedOption ? parseFloat(selectedOption.getAttribute('data-prix')) || 0 : 0;
    var quantite = parseInt(quantiteInput.value) || 0;
    
    var total = prixUnitaire * quantite;
    
    prixCalculeSpan.textContent = total.toLocaleString('fr-FR') + ' MGA';
    
    if (!prixTotalInput.value || prixTotalInput.value == '' || prixTotalInput.dataset.autoFill !== 'false') {
        prixTotalInput.value = total.toFixed(2);
    }
}

// Calculer au chargement
document.addEventListener('DOMContentLoaded', function() {
    updatePrixTotal();
});
</script>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
