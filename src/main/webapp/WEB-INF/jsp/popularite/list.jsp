<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="popularity-page">
    <!-- Hero Header -->
    <div class="hero-header">
        <div class="hero-content">
            <div class="hero-icon">
                <i class="fas fa-plane"></i>
            </div>
            <div class="hero-text">
                <h1>Popularité des Vols</h1>
                <p>Découvrez les destinations les plus prisées par nos voyageurs</p>
            </div>
        </div>
        <div class="hero-stats">
            <div class="stat-item">
                <span class="stat-number">${not empty ranking ? ranking.size() : 0}</span>
                <span class="stat-label">Vols classés</span>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
                <span class="stat-number">${filterAnnee}</span>
                <span class="stat-label">Année</span>
            </div>
        </div>
    </div>

    <!-- Filtres -->
    <div class="filter-section">
        <form method="get" action="${pageContext.request.contextPath}/popularite-vols" class="filter-form">
            <div class="filter-group">
                <label><i class="fas fa-calendar"></i> Année</label>
                <select name="annee" class="filter-select">
                    <c:forEach var="y" items="${years}">
                        <option value="${y}" ${filterAnnee == y ? 'selected' : ''}>${y}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="filter-group">
                <label><i class="fas fa-calendar-alt"></i> Mois</label>
                <select name="mois" class="filter-select">
                    <option value="">Toute l'année</option>
                    <c:forEach var="i" begin="0" end="11">
                        <option value="${i + 1}" ${filterMois != null && filterMois == i + 1 ? 'selected' : ''}>${monthNames[i]}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="filter-group">
                <label><i class="fas fa-plane-departure"></i> Vol</label>
                <select name="volId" class="filter-select">
                    <option value="">Tous les vols</option>
                    <c:forEach var="v" items="${vols}">
                        <option value="${v.id}" ${filterVolId != null && filterVolId == v.id ? 'selected' : ''}>${v.numeroVol}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="filter-actions">
                <button type="submit" class="btn-filter">
                    <i class="fas fa-search"></i> Filtrer
                </button>
                <a href="${pageContext.request.contextPath}/popularite-vols" class="btn-reset">
                    <i class="fas fa-redo"></i>
                </a>
            </div>
        </form>
    </div>

    <div class="content-grid">
        <!-- Classement Principal -->
        <div class="ranking-section">
            <div class="section-header">
                <h2>
                    <i class="fas fa-trophy"></i>
                    Classement
                    <c:choose>
                        <c:when test="${filterMois != null}">
                            <span class="period-badge">${monthNames[filterMois - 1]} ${filterAnnee}</span>
                        </c:when>
                        <c:otherwise>
                            <span class="period-badge">Année ${filterAnnee}</span>
                        </c:otherwise>
                    </c:choose>
                </h2>
            </div>
            
            <c:if test="${selectedVol != null}">
                <div class="vol-detail-card">
                    <div class="vol-detail-header">
                        <div class="vol-number">${selectedVol.numeroVol}</div>
                        <div class="vol-route">
                            <span>${selectedVol.aeroportOrigine.nomAeroport}</span>
                            <i class="fas fa-plane"></i>
                            <span>${selectedVol.aeroportDestination.nomAeroport}</span>
                        </div>
                    </div>
                    <div class="vol-points-display">
                        <span class="points-value">${volTotalPoints != null ? volTotalPoints : 0}</span>
                        <span class="points-label">points totaux</span>
                    </div>
                </div>
                
                <h5 class="history-title"><i class="fas fa-history"></i> Historique des points</h5>
                <div class="history-list">
                    <c:forEach var="h" items="${volHistory}">
                        <div class="history-item">
                            <span class="history-date">${h.datePoint}</span>
                            <span class="history-points">+${h.point}</span>
                        </div>
                    </c:forEach>
                </div>
                <c:if test="${empty volHistory}">
                    <div class="empty-state small">
                        <i class="fas fa-inbox"></i>
                        <p>Aucun historique pour cette période</p>
                    </div>
                </c:if>
            </c:if>

            <c:if test="${selectedVol == null}">
                <!-- Top 3 Podium -->
                <c:if test="${not empty ranking && ranking.size() >= 3}">
                    <div class="podium">
                        <div class="podium-item silver">
                            <div class="podium-rank">2</div>
                            <div class="podium-medal">🥈</div>
                            <div class="podium-vol">${ranking[1].vol.numeroVol}</div>
                            <div class="podium-route">${ranking[1].vol.aeroportOrigine.nomAeroport} → ${ranking[1].vol.aeroportDestination.nomAeroport}</div>
                            <div class="podium-points">${ranking[1].pointsPeriode} pts</div>
                        </div>
                        <div class="podium-item gold">
                            <div class="podium-rank">1</div>
                            <div class="podium-medal">🥇</div>
                            <div class="podium-vol">${ranking[0].vol.numeroVol}</div>
                            <div class="podium-route">${ranking[0].vol.aeroportOrigine.nomAeroport} → ${ranking[0].vol.aeroportDestination.nomAeroport}</div>
                            <div class="podium-points">${ranking[0].pointsPeriode} pts</div>
                        </div>
                        <div class="podium-item bronze">
                            <div class="podium-rank">3</div>
                            <div class="podium-medal">🥉</div>
                            <div class="podium-vol">${ranking[2].vol.numeroVol}</div>
                            <div class="podium-route">${ranking[2].vol.aeroportOrigine.nomAeroport} → ${ranking[2].vol.aeroportDestination.nomAeroport}</div>
                            <div class="podium-points">${ranking[2].pointsPeriode} pts</div>
                        </div>
                    </div>
                </c:if>

                <!-- Liste complète -->
                <div class="ranking-list">
                    <c:forEach var="r" items="${ranking}" varStatus="status">
                        <c:if test="${status.index >= 3 || ranking.size() < 3}">
                            <div class="ranking-item">
                                <div class="rank-position">
                                    <c:choose>
                                        <c:when test="${r.position == 1}"><span class="medal">🥇</span></c:when>
                                        <c:when test="${r.position == 2}"><span class="medal">🥈</span></c:when>
                                        <c:when test="${r.position == 3}"><span class="medal">🥉</span></c:when>
                                        <c:otherwise><span class="rank-num">${r.position}</span></c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="rank-info">
                                    <div class="rank-vol">${r.vol.numeroVol}</div>
                                    <div class="rank-route">${r.vol.aeroportOrigine.nomAeroport} → ${r.vol.aeroportDestination.nomAeroport}</div>
                                </div>
                                <div class="rank-points">
                                    <div class="points-period">${r.pointsPeriode} <small>période</small></div>
                                    <div class="points-total">${r.pointsTotal} <small>total</small></div>
                                </div>
                                <a href="${pageContext.request.contextPath}/popularite-vols/detail/${r.vol.id}" class="rank-action">
                                    <i class="fas fa-chevron-right"></i>
                                </a>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
                
                <c:if test="${empty ranking}">
                    <div class="empty-state">
                        <i class="fas fa-chart-line"></i>
                        <h3>Aucune donnée disponible</h3>
                        <p>Les points de popularité apparaîtront ici après les réservations</p>
                    </div>
                </c:if>
            </c:if>
        </div>

        <!-- Sidebar Top 10 Global -->
        <div class="sidebar-section">
            <div class="top10-card">
                <div class="top10-header">
                    <i class="fas fa-crown"></i>
                    <h3>Top 10 Global</h3>
                </div>
                <div class="top10-list">
                    <c:forEach var="g" items="${globalRanking}">
                        <div class="top10-item ${g.position <= 3 ? 'top-three' : ''}">
                            <div class="top10-rank">
                                <c:choose>
                                    <c:when test="${g.position == 1}"><span class="crown">👑</span></c:when>
                                    <c:when test="${g.position == 2}"><span class="medal-sm">🥈</span></c:when>
                                    <c:when test="${g.position == 3}"><span class="medal-sm">🥉</span></c:when>
                                    <c:otherwise><span class="rank-circle">${g.position}</span></c:otherwise>
                                </c:choose>
                            </div>
                            <div class="top10-info">
                                <span class="top10-vol">${g.vol.numeroVol}</span>
                            </div>
                            <div class="top10-points">${g.points}</div>
                        </div>
                    </c:forEach>
                </div>
                <c:if test="${empty globalRanking}">
                    <div class="empty-state small">
                        <i class="fas fa-star"></i>
                        <p>Pas encore de données</p>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<style>
    /* ========== PAGE POPULARITÉ - DESIGN ORIGINAL ========== */
    
    .popularity-page {
        padding: 0;
        background: linear-gradient(135deg, #f5f7fa 0%, #e4e8ec 100%);
        min-height: 100vh;
    }

    /* Hero Header */
    .hero-header {
        background: linear-gradient(135deg, #1e3a5f 0%, #2d5a87 50%, #3d7ab5 100%);
        padding: 40px;
        color: white;
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-radius: 0 0 30px 30px;
        margin-bottom: 30px;
        box-shadow: 0 10px 40px rgba(30, 58, 95, 0.3);
        position: relative;
        overflow: hidden;
    }
    
    .hero-header::before {
        content: '';
        position: absolute;
        top: -50%;
        right: -10%;
        width: 400px;
        height: 400px;
        background: rgba(255,255,255,0.05);
        border-radius: 50%;
    }
    
    .hero-header::after {
        content: '';
        position: absolute;
        bottom: -30%;
        left: 10%;
        width: 200px;
        height: 200px;
        background: rgba(255,255,255,0.03);
        border-radius: 50%;
    }

    .hero-content {
        display: flex;
        align-items: center;
        gap: 25px;
        z-index: 1;
    }

    .hero-icon {
        width: 80px;
        height: 80px;
        background: rgba(255,255,255,0.15);
        border-radius: 20px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 36px;
        backdrop-filter: blur(10px);
    }

    .hero-text h1 {
        font-size: 2rem;
        font-weight: 800;
        margin: 0 0 5px 0;
    }

    .hero-text p {
        margin: 0;
        opacity: 0.85;
        font-size: 1rem;
    }

    .hero-stats {
        display: flex;
        align-items: center;
        gap: 30px;
        z-index: 1;
    }

    .stat-item {
        text-align: center;
    }

    .stat-number {
        display: block;
        font-size: 2.5rem;
        font-weight: 800;
    }

    .stat-label {
        font-size: 0.85rem;
        opacity: 0.8;
        text-transform: uppercase;
        letter-spacing: 1px;
    }

    .stat-divider {
        width: 2px;
        height: 50px;
        background: rgba(255,255,255,0.3);
    }

    /* Filtres */
    .filter-section {
        padding: 0 40px;
        margin-bottom: 30px;
    }

    .filter-form {
        background: white;
        padding: 25px 30px;
        border-radius: 16px;
        display: flex;
        gap: 20px;
        align-items: flex-end;
        box-shadow: 0 4px 20px rgba(0,0,0,0.08);
        flex-wrap: wrap;
    }

    .filter-group {
        flex: 1;
        min-width: 150px;
    }

    .filter-group label {
        display: block;
        font-size: 0.85rem;
        font-weight: 600;
        color: #64748b;
        margin-bottom: 8px;
    }

    .filter-group label i {
        margin-right: 6px;
        color: #3d7ab5;
    }

    .filter-select {
        width: 100%;
        padding: 12px 16px;
        border: 2px solid #e2e8f0;
        border-radius: 10px;
        font-size: 0.95rem;
        background: white;
        transition: all 0.3s;
        cursor: pointer;
    }

    .filter-select:focus {
        outline: none;
        border-color: #3d7ab5;
        box-shadow: 0 0 0 4px rgba(61, 122, 181, 0.15);
    }

    .filter-actions {
        display: flex;
        gap: 10px;
    }

    .btn-filter {
        padding: 12px 28px;
        background: linear-gradient(135deg, #3d7ab5 0%, #1e3a5f 100%);
        color: white;
        border: none;
        border-radius: 10px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s;
    }

    .btn-filter:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(30, 58, 95, 0.4);
    }

    .btn-reset {
        padding: 12px 16px;
        background: #f1f5f9;
        color: #64748b;
        border: none;
        border-radius: 10px;
        cursor: pointer;
        transition: all 0.3s;
        text-decoration: none;
    }

    .btn-reset:hover {
        background: #e2e8f0;
    }

    /* Content Grid */
    .content-grid {
        display: grid;
        grid-template-columns: 1fr 320px;
        gap: 30px;
        padding: 0 40px 40px;
    }

    /* Ranking Section */
    .ranking-section {
        background: white;
        border-radius: 20px;
        padding: 30px;
        box-shadow: 0 4px 20px rgba(0,0,0,0.08);
    }

    .section-header h2 {
        display: flex;
        align-items: center;
        gap: 12px;
        font-size: 1.4rem;
        color: #1e293b;
        margin: 0 0 25px 0;
    }

    .section-header h2 i {
        color: #f59e0b;
    }

    .period-badge {
        background: linear-gradient(135deg, #3d7ab5 0%, #1e3a5f 100%);
        color: white;
        padding: 6px 16px;
        border-radius: 20px;
        font-size: 0.85rem;
        font-weight: 600;
    }

    /* Podium */
    .podium {
        display: flex;
        justify-content: center;
        align-items: flex-end;
        gap: 20px;
        margin-bottom: 40px;
        padding: 20px;
    }

    .podium-item {
        text-align: center;
        padding: 25px 20px;
        border-radius: 16px;
        min-width: 160px;
        position: relative;
        transition: transform 0.3s;
    }

    .podium-item:hover {
        transform: translateY(-5px);
    }

    .podium-item.gold {
        background: linear-gradient(135deg, #fef3c7 0%, #fcd34d 100%);
        order: 2;
        transform: scale(1.1);
        z-index: 2;
        box-shadow: 0 10px 30px rgba(252, 211, 77, 0.4);
    }

    .podium-item.silver {
        background: linear-gradient(135deg, #f1f5f9 0%, #cbd5e1 100%);
        order: 1;
        box-shadow: 0 6px 20px rgba(0,0,0,0.1);
    }

    .podium-item.bronze {
        background: linear-gradient(135deg, #fed7aa 0%, #fb923c 100%);
        order: 3;
        box-shadow: 0 6px 20px rgba(251, 146, 60, 0.3);
    }

    .podium-medal {
        font-size: 3rem;
        margin-bottom: 10px;
    }

    .podium-vol {
        font-size: 1.2rem;
        font-weight: 800;
        color: #1e293b;
        margin-bottom: 5px;
    }

    .podium-route {
        font-size: 0.75rem;
        color: #64748b;
        margin-bottom: 10px;
    }

    .podium-points {
        background: rgba(0,0,0,0.1);
        padding: 6px 14px;
        border-radius: 20px;
        font-weight: 700;
        font-size: 0.9rem;
    }

    /* Ranking List */
    .ranking-list {
        display: flex;
        flex-direction: column;
        gap: 12px;
    }

    .ranking-item {
        display: flex;
        align-items: center;
        padding: 16px 20px;
        background: #f8fafc;
        border-radius: 12px;
        transition: all 0.3s;
        gap: 16px;
    }

    .ranking-item:hover {
        background: #f1f5f9;
        transform: translateX(5px);
    }

    .rank-position {
        width: 45px;
        text-align: center;
    }

    .rank-num {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        width: 32px;
        height: 32px;
        background: #e2e8f0;
        border-radius: 50%;
        font-weight: 700;
        color: #64748b;
    }

    .medal {
        font-size: 1.8rem;
    }

    .rank-info {
        flex: 1;
    }

    .rank-vol {
        font-weight: 700;
        font-size: 1.05rem;
        color: #1e293b;
    }

    .rank-route {
        font-size: 0.85rem;
        color: #64748b;
    }

    .rank-points {
        display: flex;
        gap: 15px;
        text-align: right;
    }

    .points-period {
        font-weight: 700;
        color: #3d7ab5;
    }

    .points-total {
        font-weight: 600;
        color: #10b981;
    }

    .rank-points small {
        display: block;
        font-size: 0.7rem;
        font-weight: 500;
        color: #94a3b8;
        text-transform: uppercase;
    }

    .rank-action {
        width: 40px;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        background: white;
        border-radius: 10px;
        color: #64748b;
        text-decoration: none;
        transition: all 0.3s;
    }

    .rank-action:hover {
        background: #3d7ab5;
        color: white;
    }

    /* Sidebar */
    .sidebar-section {
        position: sticky;
        top: 20px;
    }

    .top10-card {
        background: linear-gradient(135deg, #1e3a5f 0%, #2d5a87 100%);
        border-radius: 20px;
        padding: 25px;
        color: white;
        box-shadow: 0 10px 40px rgba(30, 58, 95, 0.3);
    }

    .top10-header {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-bottom: 20px;
        padding-bottom: 15px;
        border-bottom: 1px solid rgba(255,255,255,0.2);
    }

    .top10-header i {
        font-size: 1.5rem;
        color: #fcd34d;
    }

    .top10-header h3 {
        margin: 0;
        font-size: 1.2rem;
    }

    .top10-list {
        display: flex;
        flex-direction: column;
        gap: 10px;
    }

    .top10-item {
        display: flex;
        align-items: center;
        padding: 12px 14px;
        background: rgba(255,255,255,0.1);
        border-radius: 10px;
        gap: 12px;
        transition: all 0.3s;
    }

    .top10-item:hover {
        background: rgba(255,255,255,0.15);
        transform: translateX(3px);
    }

    .top10-item.top-three {
        background: rgba(255,255,255,0.15);
    }

    .top10-rank {
        width: 30px;
        text-align: center;
    }

    .crown {
        font-size: 1.4rem;
    }

    .medal-sm {
        font-size: 1.2rem;
    }

    .rank-circle {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        width: 24px;
        height: 24px;
        background: rgba(255,255,255,0.2);
        border-radius: 50%;
        font-size: 0.8rem;
        font-weight: 600;
    }

    .top10-info {
        flex: 1;
    }

    .top10-vol {
        font-weight: 600;
    }

    .top10-points {
        background: rgba(255,255,255,0.2);
        padding: 4px 12px;
        border-radius: 15px;
        font-weight: 700;
        font-size: 0.9rem;
    }

    /* Empty State */
    .empty-state {
        text-align: center;
        padding: 60px 30px;
        color: #94a3b8;
    }

    .empty-state i {
        font-size: 4rem;
        margin-bottom: 20px;
        opacity: 0.5;
    }

    .empty-state h3 {
        color: #64748b;
        margin-bottom: 10px;
    }

    .empty-state.small {
        padding: 30px 20px;
    }

    .empty-state.small i {
        font-size: 2rem;
    }

    /* Vol Detail Card */
    .vol-detail-card {
        background: linear-gradient(135deg, #3d7ab5 0%, #1e3a5f 100%);
        border-radius: 16px;
        padding: 25px;
        color: white;
        margin-bottom: 30px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .vol-detail-header .vol-number {
        font-size: 2rem;
        font-weight: 800;
        margin-bottom: 5px;
    }

    .vol-route {
        display: flex;
        align-items: center;
        gap: 10px;
        opacity: 0.9;
    }

    .vol-route i {
        color: #fcd34d;
    }

    .vol-points-display {
        text-align: right;
    }

    .points-value {
        display: block;
        font-size: 3rem;
        font-weight: 800;
        line-height: 1;
    }

    .points-label {
        font-size: 0.9rem;
        opacity: 0.8;
    }

    .history-title {
        color: #1e293b;
        margin-bottom: 15px;
    }

    .history-list {
        display: flex;
        flex-direction: column;
        gap: 8px;
    }

    .history-item {
        display: flex;
        justify-content: space-between;
        padding: 12px 16px;
        background: #f8fafc;
        border-radius: 8px;
    }

    .history-date {
        color: #64748b;
    }

    .history-points {
        font-weight: 700;
        color: #10b981;
    }

    /* Responsive */
    @media (max-width: 1024px) {
        .content-grid {
            grid-template-columns: 1fr;
        }
        
        .sidebar-section {
            position: static;
        }
        
        .podium {
            flex-wrap: wrap;
        }
        
        .podium-item.gold {
            transform: scale(1);
            order: 0;
            width: 100%;
        }
    }

    @media (max-width: 768px) {
        .hero-header {
            flex-direction: column;
            text-align: center;
            gap: 25px;
            padding: 30px 20px;
        }
        
        .hero-content {
            flex-direction: column;
        }
        
        .filter-form {
            flex-direction: column;
        }
        
        .filter-group {
            width: 100%;
        }
        
        .content-grid {
            padding: 0 20px 20px;
        }
    }
</style>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
