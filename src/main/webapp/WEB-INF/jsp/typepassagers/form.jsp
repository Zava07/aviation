<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Type Passager</h3>
    </div>
    <div class="card-body">
        <form method="post" action="${action}">
            <div class="form-group">
                <label>Type</label>
                <input type="text" name="type" class="form-control" value="${item.type}" required/>
            </div>
            <button class="btn btn-success">Enregistrer</button>
            <a href="${pageContext.request.contextPath}/type-passagers" class="btn btn-secondary">Annuler</a>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
