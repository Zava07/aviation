<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title"><i class="fas fa-broadcast-tower"></i> Gestion des Diffusions</h3>
        <div class="header-actions">
            <a href="${pageContext.request.contextPath}/publicites/societes" class="btn btn-secondary">
                <i class="fas fa-building"></i> Sociétés
            </a>
            <a href="${pageContext.request.contextPath}/publicites/videos" class="btn btn-secondary">
                <i class="fas fa-video"></i> Vidéos
            </a>
        </div>
    </div>
    <div class="card-body">
        <!-- Statistiques CA -->
        <div class="stats-cards mb-4">
            <div class="stat-card stat-card-success">
                <div class="stat-icon"><i class="fas fa-coins"></i></div>
                <div class="stat-content">
                    <span class="stat-label">CA Réel</span>
                    <span class="stat-value"><fmt:formatNumber value="${caReel}" type="number" maxFractionDigits="2"/> MGA</span>
                </div>
            </div>
            <div class="stat-card stat-card-info">
                <div class="stat-icon"><i class="fas fa-calculator"></i></div>
                <div class="stat-content">
                    <span class="stat-label">CA Théorique</span>
                    <span class="stat-value"><fmt:formatNumber value="${caTheorique}" type="number" maxFractionDigits="2"/> MGA</span>
                </div>
            </div>
            <div class="stat-card stat-card-warning">
                <div class="stat-icon"><i class="fas fa-cog"></i></div>
                <div class="stat-content">
                    <span class="stat-label">Coût Config</span>
                    <span class="stat-value"><fmt:formatNumber value="${coutConfig}" type="number" maxFractionDigits="2"/> MGA</span>
                </div>
            </div>
        </div>

        <!-- Filtres -->
        <div class="filter-section mb-4">
            <form method="get" action="${pageContext.request.contextPath}/publicites/diffusions" class="filter-form">
                <div class="form-group">
                    <label for="year">Année</label>
                    <select id="year" name="year" class="form-control">
                        <c:forEach var="y" begin="2020" end="2030">
                            <option value="${y}" ${y == selectedYear ? 'selected' : ''}>${y}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="month">Mois</label>
                    <select id="month" name="month" class="form-control">
                        <option value="0" ${selectedMonth == 0 ? 'selected' : ''}>Tous</option>
                        <option value="1" ${selectedMonth == 1 ? 'selected' : ''}>Janvier</option>
                        <option value="2" ${selectedMonth == 2 ? 'selected' : ''}>Février</option>
                        <option value="3" ${selectedMonth == 3 ? 'selected' : ''}>Mars</option>
                        <option value="4" ${selectedMonth == 4 ? 'selected' : ''}>Avril</option>
                        <option value="5" ${selectedMonth == 5 ? 'selected' : ''}>Mai</option>
                        <option value="6" ${selectedMonth == 6 ? 'selected' : ''}>Juin</option>
                        <option value="7" ${selectedMonth == 7 ? 'selected' : ''}>Juillet</option>
                        <option value="8" ${selectedMonth == 8 ? 'selected' : ''}>Août</option>
                        <option value="9" ${selectedMonth == 9 ? 'selected' : ''}>Septembre</option>
                        <option value="10" ${selectedMonth == 10 ? 'selected' : ''}>Octobre</option>
                        <option value="11" ${selectedMonth == 11 ? 'selected' : ''}>Novembre</option>
                        <option value="12" ${selectedMonth == 12 ? 'selected' : ''}>Décembre</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary"><i class="fas fa-filter"></i> Filtrer</button>
            </form>
        </div>

        <!-- Formulaire création multiple -->
        <div class="form-section mb-4">
            <h4><i class="fas fa-plus-circle"></i> Créer des diffusions</h4>
            <form method="post" action="${pageContext.request.contextPath}/publicites/diffusions/save" class="grid-form">
                <div class="form-group">
                    <label for="societeId">Société *</label>
                    <select id="societeId" name="societeId" class="form-control" required>
                        <option value="">-- Sélectionner --</option>
                        <c:forEach var="societe" items="${societes}">
                            <option value="${societe.id}">${societe.nomSociete}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="publiciteVideoId">Vidéo (optionnel)</label>
                    <select id="publiciteVideoId" name="publiciteVideoId" class="form-control">
                        <option value="">-- Aucune --</option>
                        <c:forEach var="video" items="${videos}">
                            <option value="${video.id}">${video.titre} (${video.dureeSeconde}s)</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="volInstanceId">Vol Instance *</label>
                    <select id="volInstanceId" name="volInstanceId" class="form-control" required>
                        <option value="">-- Sélectionner --</option>
                        <c:forEach var="vi" items="${volInstances}">
                            <option value="${vi.id}" ${param.volInstanceId != null && param.volInstanceId == vi.id ? 'selected' : ''}>
                                ${vi.vol.numeroVol} - ${vi.avion.codeAvion} 
                                (${vi.dateDepart.toLocalDate()} ${vi.dateDepart.toLocalTime()})
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="nombreFois">Nombre de fois *</label>
                    <input type="number" id="nombreFois" name="nombreFois" class="form-control" 
                           min="1" value="1" required placeholder="Nombre de diffusions">
                </div>
                <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Créer</button>
            </form>
            <small class="form-text text-muted mt-2">
                Le montant appliqué sera: <strong><fmt:formatNumber value="${coutConfig}" type="number" maxFractionDigits="2"/> MGA</strong> (depuis Config).
                La date de diffusion sera la date de départ du vol sélectionné.
            </small>
        </div>

        <!-- Liste des diffusions -->
        <h4 class="mb-3"><i class="fas fa-list"></i> Liste des diffusions</h4>
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Société</th>
                        <th>Vidéo</th>
                        <th>Vol</th>
                        <th>Avion</th>
                        <th>Date Diffusion</th>
                        <th>Montant</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="d" items="${diffusions}">
                        <tr>
                            <td>#${d.id}</td>
                            <td>${d.societe.nomSociete}</td>
                            <td>${d.publiciteVideo != null ? d.publiciteVideo.titre : '-'}</td>
                            <td>${d.volInstance.vol.numeroVol}</td>
                            <td>${d.volInstance.avion.codeAvion}</td>
                            <td>
                                <c:if test="${d.datePublicite != null}">
                                    ${d.datePublicite.toLocalDate()} ${d.datePublicite.toLocalTime()}
                                </c:if>
                            </td>
                            <td><span class="badge badge-success"><fmt:formatNumber value="${d.montant}" type="number" maxFractionDigits="2"/> MGA</span></td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/publicites/diffusions/delete/${d.id}" 
                                      onsubmit="return confirm('Supprimer cette diffusion ?');" style="display:inline">
                                    <button type="submit" class="btn btn-sm btn-danger"><i class="fas fa-trash"></i></button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
                <c:if test="${empty diffusions}">
                    <tfoot>
                        <tr>
                            <td colspan="8" class="text-muted text-center">Aucune diffusion trouvée pour cette période.</td>
                        </tr>
                    </tfoot>
                </c:if>
            </table>
        </div>

        <div class="stats-section mt-3">
            <span class="stats-item">
                <i class="fas fa-database"></i> Total: <strong>${diffusions.size()}</strong> diffusion(s)
            </span>
        </div>
    </div>
</div>

<style>
    .stats-cards {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
        gap: 20px;
    }
    .stat-card {
        display: flex;
        align-items: center;
        padding: 20px;
        border-radius: 12px;
        color: white;
    }
    .stat-card-success { background: linear-gradient(135deg, #28a745, #20c997); }
    .stat-card-info { background: linear-gradient(135deg, #17a2b8, #6f42c1); }
    .stat-card-warning { background: linear-gradient(135deg, #ffc107, #fd7e14); color: #333; }
    .stat-icon {
        font-size: 2em;
        margin-right: 15px;
        opacity: 0.8;
    }
    .stat-content {
        display: flex;
        flex-direction: column;
    }
    .stat-label {
        font-size: 0.85em;
        opacity: 0.9;
    }
    .stat-value {
        font-size: 1.4em;
        font-weight: 600;
    }
    .filter-section {
        background: var(--bg-light, #f8f9fa);
        padding: 15px;
        border-radius: 8px;
    }
    .filter-form {
        display: flex;
        gap: 15px;
        align-items: end;
        flex-wrap: wrap;
    }
    .filter-form .form-group {
        display: flex;
        flex-direction: column;
        gap: 5px;
    }
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
    .form-section {
        background: var(--bg-light, #f8f9fa);
        padding: 20px;
        border-radius: 8px;
    }
    .form-section h4 {
        margin-bottom: 15px;
        color: var(--primary-color, #1e3a5f);
    }
    .header-actions {
        display: flex;
        gap: 10px;
    }
    .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-wrap: wrap;
        gap: 10px;
    }
</style>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
