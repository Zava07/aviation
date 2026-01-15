<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h3 class="card-title">Gestion des Caisses</h3>
    </div>
    <div class="card-body">
        <div class="mb-3">
            <div class="stats-grid">
                <!-- big total card -->
                <div class="stat-card big-stat-card" style="background: linear-gradient(90deg, rgba(30,58,95,0.95), rgba(44,82,130,0.95)); color: #fff;">
                    <div>
                        <div style="font-size:0.95rem;opacity:0.9">Dashboard</div>
                        <div style="font-size:0.95rem;margin-top:6px;">Total chiffre d'affaire global</div>
                        <div style="font-size:2rem;font-weight:800;margin-top:6px"> <fmt:formatNumber value="${totalCA}" type="number" minFractionDigits="2" maxFractionDigits="2"/></div>
                    </div>
                    <div style="text-align:right">
                        <a href="${pageContext.request.contextPath}/caisses" class="btn" style="background:rgba(255,255,255,0.12);color:#fff;border:0;">Voir Caisses</a>
                    </div>
                </div>

                <!-- small supporting cards: count caisses + total balance -->
                <div class="stat-card">
                    <div class="stat-icon info"><i class="fas fa-boxes"></i></div>
                    <div class="stat-info">
                        <div style="font-size:0.85rem;color:var(--gray)">Nombre de caisses</div>
                        <div style="font-size:1.25rem;font-weight:700">${empty caisses ? 0 : caisses.size()}</div>
                    </div>
                </div>

                <c:set var="totalBalance" value="0" />
                <c:forEach var="c" items="${caisses}">
                    <c:set var="val" value="${balances[c.id] != null ? balances[c.id] : 0}" />
                    <c:set var="totalBalance" value="${totalBalance + val}" />
                </c:forEach>

                <div class="stat-card">
                    <div class="stat-icon danger"><i class="fas fa-balance-scale-left"></i></div>
                    <div class="stat-info">
                        <div style="font-size:0.85rem;color:var(--gray)">Total balance</div>
                        <div style="font-size:1.25rem;font-weight:700;color:var(--primary)"><fmt:formatNumber value="${totalBalance}" type="number" minFractionDigits="2" maxFractionDigits="2"/></div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Small card dashboard -->
        <div class="stats-grid">
            <c:forEach var="c" items="${caisses}">
                <div class="stat-card">
                    <div class="stat-icon primary"><i class="fas fa-wallet"></i></div>
                    <div class="stat-info" style="flex:1;">
                        <div style="display:flex;justify-content:space-between;align-items:flex-start">
                            <div>
                                <div style="font-size:0.95rem;font-weight:600">Caisse #<c:out value="${c.id}"/> <small style="color:var(--gray);font-weight:400">- <c:out value="${c.nomCaisse}"/></small></div>
                                <div style="margin-top:6px;color:var(--gray);font-size:0.85rem">Solde</div>
                                <div style="font-size:1.15rem;font-weight:700;color:var(--dark)"><fmt:formatNumber value="${c.solde}" type="number" minFractionDigits="2" maxFractionDigits="2"/></div>
                            </div>
                            <div style="text-align:right">
                                <div style="color:var(--gray);font-size:0.85rem">Credits</div>
                                <div style="font-weight:700;color:var(--success);"><fmt:formatNumber value="${totalCredits[c.id]}" type="number" minFractionDigits="2" maxFractionDigits="2"/></div>
                                <div style="margin-top:6px;color:var(--gray);font-size:0.85rem">Balance</div>
                                <div style="font-weight:700;color:var(--primary);"><fmt:formatNumber value="${balances[c.id]}" type="number" minFractionDigits="2" maxFractionDigits="2"/></div>
                            </div>
                        </div>
                        <div style="margin-top:12px;text-align:right">
                            <a href="${pageContext.request.contextPath}/caisses/${c.id}" class="btn btn-sm btn-outline-primary">Détails</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Existing detailed table retained below -->
        <div class="table-responsive mt-3">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nom</th>
                        <th>Solde</th>
                        <th>Chiffre d'affaire (credits)</th>
                        <th>Balance (credits - debits)</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="c" items="${caisses}">
                        <tr>
                            <td><c:out value="${c.id}"/></td>
                            <td><c:out value="${c.nomCaisse}"/></td>
                            <td><fmt:formatNumber value="${c.solde}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                            <td><fmt:formatNumber value="${totalCredits[c.id]}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                            <td><fmt:formatNumber value="${balances[c.id]}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/caisses/${c.id}" class="btn btn-sm btn-outline-primary view-caisse" data-caisse-id="${c.id}">Détails</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <c:if test="${empty caisses}">
            <div class="alert alert-info">Aucune caisse trouvée pour ces critères.</div>
        </c:if>
    </div>
</div>

<!-- modal -->
<div id="caisseDetailsModal" class="modal" style="display:none;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Détails Caisse</h5>
                <button type="button" class="close" onclick="hideCaisseModal();">&times;</button>
            </div>
            <div class="modal-body">
                <div id="caisseInfo">
                    <p>Nom: <span id="caisseNom"></span></p>
                    <p>Solde: <span id="caisseSolde"></span></p>
                    <p>Balance calculée: <span id="caisseBalance"></span></p>
                    <p>Total credits: <span id="caisseCredits"></span></p>
                </div>
                <h6>Mouvements</h6>
                <table class="data-table" id="mouvementsTable">
                    <thead>
                        <tr><th>ID</th><th>Montant</th><th>Type</th><th>Date</th><th>Paiement</th></tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" onclick="hideCaisseModal();">Fermer</button>
            </div>
        </div>
    </div>
</div>


<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>