<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

        <h5>Sièges</h5>
        <c:if test="${not empty sieges}">
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr><th>Numéro</th>
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

        <h5 style="margin-top:16px">Instances de Vol (affectées à cet avion)</h5>
        <c:if test="${not empty volsWithMax}">
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Vol</th>
                            <th>Valeur max possible</th>
                            
                            
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="m" items="${volsWithMax}">
                            <c:set var="vol" value="${m.vol}" />
                            <tr>
                                <td>${vol != null ? vol.numeroVol : ''}</td>
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
