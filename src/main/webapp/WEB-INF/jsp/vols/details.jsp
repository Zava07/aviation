<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Détails Vol</h3>
        <a href="${pageContext.request.contextPath}/vols" class="btn btn-secondary">Retour</a>
    </div>
    <div class="card-body">
        <div style="margin-bottom:12px">
            <h4>${vol.numeroVol} <small class="text-muted">${vol.aeroportOrigine != null ? vol.aeroportOrigine.nomAeroport : ''} → ${vol.aeroportDestination != null ? vol.aeroportDestination.nomAeroport : ''}</small></h4>
        </div>

        <div style="display:flex;gap:20px;align-items:center;margin-bottom:16px">
            <div><strong>CA billets:</strong> <fmt:formatNumber value="${totalTickets}" type="number" minFractionDigits="2" maxFractionDigits="2"/> MGA</div>
            <div><strong>CA publicité:</strong> <fmt:formatNumber value="${totalAds}" type="number" minFractionDigits="2" maxFractionDigits="2"/> MGA</div>
            <div><strong>Total CA:</strong> <fmt:formatNumber value="${totalCA}" type="number" minFractionDigits="2" maxFractionDigits="2"/> MGA</div>
            <form method="get" action="${pageContext.request.contextPath}/vols/details/${vol.id}" style="margin-left:auto;display:flex;gap:8px;align-items:center">
                <label style="margin:0">Filtrer date:</label>
                <input type="date" name="date" value="${filterDate}" class="form-control" />
                <button class="btn btn-sm btn-primary">Appliquer</button>
            </form>
        </div>

        <h5>Instances de Vol</h5>
        <c:if test="${not empty volInstances}">
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Avion</th>
                            <th>Date Départ</th>
                            <th>Heure Départ</th>
                            <th>Montant tickets</th>
                            <th>Montant diffusion pub</th>
                            <th>Total CA</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="vi" items="${volInstances}">
                            <tr>
                                <td>${vi.id}</td>
                                <td>${vi.avion != null ? vi.avion.codeAvion : ''}</td>
                                <td><fmt:formatDate value="${vi.dateDepartDate}" pattern="dd/MM/yyyy"/></td>
                                <td><fmt:formatDate value="${vi.dateDepartDate}" pattern="HH:mm"/></td>
                                <td><fmt:formatNumber value="${vi.tickets}" type="number" minFractionDigits="2" maxFractionDigits="2"/> MGA</td>
                                <td><fmt:formatNumber value="${vi.ads}" type="number" minFractionDigits="2" maxFractionDigits="2"/> MGA</td>
                                <td><fmt:formatNumber value="${vi.total}" type="number" minFractionDigits="2" maxFractionDigits="2"/> MGA</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/publicites/diffusions?volInstanceId=${vi.id}&amp;year=${vi.dateDepartYear != null ? vi.dateDepartYear : ''}&amp;month=${vi.dateDepartMonth != null ? vi.dateDepartMonth : ''}" class="btn btn-sm btn-outline-primary">Voir diffusions</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
        <c:if test="${empty volInstances}">
            <div class="alert alert-info">Aucune instance de vol pour ce vol (ou pas de données pour la période sélectionnée).</div>
        </c:if>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>