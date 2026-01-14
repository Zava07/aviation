<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h3 class="card-title">Mouvements des Caisses</h3>
    </div>
    <div class="card-body">
        <form method="get" class="mb-3">
            <label>De: <input type="date" name="dateFrom" value="${dateFrom}"/></label>
            <label>À: <input type="date" name="dateTo" value="${dateTo}"/></label>
            <label>Type:
                <select name="type">
                    <option value="">Tous</option>
                    <option value="credit" ${typeFilter == 'credit' ? 'selected' : ''}>Credit</option>
                    <option value="debit" ${typeFilter == 'debit' ? 'selected' : ''}>Debit</option>
                </select>
            </label>
            <button class="btn btn-primary">Filtrer</button>
        </form>

        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Montant</th>
                        <th>Type</th>
                        <th>Date</th>
                        <th>Caisse</th>
                        <th>Paiement / Billet</th>
                        <th>Passager</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="m" items="${mouvements}">
                        <tr>
                            <td><c:out value="${m.id}"/></td>
                            <td><fmt:formatNumber value="${m.montant}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                            <td><c:out value="${m.type}"/></td>
                            <td><c:out value="${m.date}"/></td>
                            <td><a href="${pageContext.request.contextPath}/caisses/${m.caisseId}"><c:out value="${m.caisseNom}"/></a></td>
                            <td>
                                <c:if test="${not empty m.billetNumero}">
                                    <a href="${pageContext.request.contextPath}/billets">${m.billetNumero}</a>
                                </c:if>
                            </td>
                            <td><c:out value="${m.passager}"/></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
