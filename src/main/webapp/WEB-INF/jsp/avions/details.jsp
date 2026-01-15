<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<% request.setAttribute("pageCss", "details.css"); %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Détails Avion</h3>
        <a href="${pageContext.request.contextPath}/avions" class="btn btn-secondary">Retour</a>
    </div>
    <div class="card-body">
        <div style="margin-bottom:12px">
            <h4>${avion.codeAvion} <small class="text-muted">${avion.modeleAvion != null ? avion.modeleAvion.nomModele : ''}</small></h4>
            <div>Capacité Passagers: <strong>${avion.capacitePassagers}</strong></div>
            <div>Capacité Cargo: <strong>${avion.capaciteCargo}</strong></div>
            <div>État: <strong>${avion.etatAvion != null ? avion.etatAvion.etat : ''}</strong></div>
            <div>Point vol: <strong>${avion.pointVol}</strong></div>
        </div>

        <div style="margin-bottom:12px">
            <h5>Distribution sièges par classe</h5>
            <div class="stats-grid">
                <c:forEach var="sc" items="${seatCounts}">
                    <c:set var="classeObj" value="${sc.classe}" />
                    <c:set var="cnt" value="${sc.count}" />
                    <div class="stat-card">
                        <div class="stat-icon primary"><i class="fas fa-user"></i></div>
                        <div class="stat-info">
                            <small>${classeObj.nomClasse}</small>
                            <h3>${cnt}</h3>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <h5>Sièges</h5>
        <c:if test="${not empty sieges}">
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Numéro</th>
                            <th>Classe</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="s" items="${sieges}">
                            <tr>
                                <td>${s.numero}</td>
                                <td>${s.classe != null ? s.classe.nomClasse : ''}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
        <c:if test="${empty sieges}">
            <div class="alert alert-info">Aucun siège enregistré pour cet avion.</div>
        </c:if>

        <h5 style="margin-top:16px">Vols associés</h5>
        <c:if test="${not empty volsWithMax}">
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Vol</th>
                            <th>Prix par classe</th>
                            <th>Valeur max possible</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="m" items="${volsWithMax}">
                            <c:set var="vol" value="${m.vol}" />
                            <tr>
                                <td>${vol != null ? vol.numeroVol : ''}</td>
                                <td>
                                    <c:if test="${not empty m.prixParClasse}">
                                        <ul class="price-list">
                                            <c:forEach var="p" items="${m.prixParClasse}">
                                                <li>${p.classe != null ? p.classe.nomClasse : ''} : <fmt:formatNumber value="${p.prix}" type="number" minFractionDigits="2" maxFractionDigits="2"/> MGA </li>
                                            </c:forEach>
                                        </ul>
                                    </c:if>
                                </td>
                                <td><fmt:formatNumber value="${m.maxRevenue}" type="number" minFractionDigits="2" maxFractionDigits="2"/> MGA</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
        <c:if test="${empty volsWithMax}">
            <div class="alert alert-info">Aucun vol associé à cet avion.</div>
        </c:if>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
