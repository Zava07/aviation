<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h3 class="card-title">Détails Caisse #${caisse.id} - ${caisse.nomCaisse}</h3>
        <div>
            <a href="${pageContext.request.contextPath}/caisses" class="btn btn-sm btn-outline-secondary">Retour</a>
        </div>
    </div>
    <div class="card-body">
        <div style="display:flex;gap:16px;align-items:center;margin-bottom:12px">
            <div><strong>Solde:</strong> <fmt:formatNumber value="${caisse.solde}" type="number" minFractionDigits="2" maxFractionDigits="2"/></div>
            <div><strong>Balance:</strong> <fmt:formatNumber value="${balance}" type="number" minFractionDigits="2" maxFractionDigits="2"/></div>
            <div><strong>Total Credits (CA):</strong> <fmt:formatNumber value="${totalCredits}" type="number" minFractionDigits="2" maxFractionDigits="2"/></div>
        </div>

        <form method="get" action="${pageContext.request.contextPath}/caisses/${caisse.id}" class="mb-3" style="display:flex;gap:8px;align-items:center;flex-wrap:wrap">
            <label>Date from: <input type="date" name="dateFrom" class="form-control" value="${dateFrom}" /></label>
            <label>Date to: <input type="date" name="dateTo" class="form-control" value="${dateTo}" /></label>
            <label>Type:
                <select name="type" class="form-control">
                    <option value="">-- Any --</option>
                    <c:forEach var="t" items="${{'credit','debit'}}">
                        <option value="${t}" ${typeFilter == t ? 'selected' : ''}>${t}</option>
                    </c:forEach>
                </select>
            </label>
            <button class="btn btn-primary" type="submit">Filtrer</button>
        </form>

        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr><th>ID</th><th>Date</th><th>Montant</th><th>Type</th><th>Paiement</th><th>Billet</th><th>Passager</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="m" items="${mouvements}">
                        <tr>
                            <td><c:out value="${m.id}"/></td>
                            <td><c:out value="${m.date}"/></td>
                            <td><fmt:formatNumber value="${m.montant}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                            <td><c:out value="${m.type}"/></td>
                            <td><c:out value="${m.paiementId != null ? m.paiementId : '-'}"/></td>
                            <td><c:out value="${m.billetNumero != null ? m.billetNumero : '-'}"/></td>
                            <td><c:out value="${m.passager != null ? m.passager : '-'}"/></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>