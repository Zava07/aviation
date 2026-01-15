<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - Aviation</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>
    <%@ include file="../includes/header.jsp" %>

    <div class="container-fluid py-4">
        <div class="row">
            <div class="col-12">
                <div class="card shadow-sm">
                    <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                        <h5 class="mb-0"><i class="bi bi-people-fill me-2"></i>Liste des Passagers avec Points de Fidélité</h5>
                    </div>
                    <div class="card-body">
                        
                        <c:if test="${not empty successMessage}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                ${successMessage}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>

                        <!-- Filtre Multi-critères -->
                        <form method="get" action="${pageContext.request.contextPath}/passagers" class="mb-4">
                            <div class="card bg-light">
                                <div class="card-header">
                                    <h6 class="mb-0"><i class="bi bi-funnel me-2"></i>Filtres</h6>
                                </div>
                                <div class="card-body">
                                    <div class="row g-3">
                                        <!-- Filtres passager -->
                                        <div class="col-md-3">
                                            <label class="form-label">Nom</label>
                                            <input type="text" name="nom" class="form-control" value="${param.nom}" placeholder="Rechercher par nom">
                                        </div>
                                        <div class="col-md-3">
                                            <label class="form-label">Prénom</label>
                                            <input type="text" name="prenom" class="form-control" value="${param.prenom}" placeholder="Rechercher par prénom">
                                        </div>
                                        <div class="col-md-3">
                                            <label class="form-label">Email</label>
                                            <input type="text" name="email" class="form-control" value="${param.email}" placeholder="Rechercher par email">
                                        </div>
                                        <div class="col-md-3">
                                            <label class="form-label">Type Passager</label>
                                            <select name="typeId" class="form-select">
                                                <option value="">-- Tous --</option>
                                                <c:forEach var="t" items="${types}">
                                                    <option value="${t.id}" ${param.typeId == t.id ? 'selected' : ''}>${t.type}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    
                                    <hr>
                                    
                                    <!-- Filtres période fidélité -->
                                    <div class="row g-3">
                                        <div class="col-md-3">
                                            <label class="form-label"><i class="bi bi-calendar-month me-1"></i>Mois</label>
                                            <select name="mois" class="form-select">
                                                <c:forEach var="m" items="${listeMois}">
                                                    <option value="${m.value}" ${selectedMois == m.value ? 'selected' : ''}>${m.label}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="col-md-3">
                                            <label class="form-label"><i class="bi bi-calendar-event me-1"></i>Année</label>
                                            <select name="annee" class="form-select">
                                                <c:forEach var="a" items="${listeAnnees}">
                                                    <option value="${a}" ${selectedAnnee == a ? 'selected' : ''}>${a}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="col-md-3">
                                            <label class="form-label">Points Min (mois)</label>
                                            <input type="number" name="pointsMin" class="form-control" value="${param.pointsMin}" placeholder="Min">
                                        </div>
                                        <div class="col-md-3">
                                            <label class="form-label">Points Max (mois)</label>
                                            <input type="number" name="pointsMax" class="form-control" value="${param.pointsMax}" placeholder="Max">
                                        </div>
                                    </div>
                                    
                                    <div class="row mt-3">
                                        <div class="col-12">
                                            <button type="submit" class="btn btn-primary">
                                                <i class="bi bi-search me-1"></i>Filtrer
                                            </button>
                                            <a href="${pageContext.request.contextPath}/passagers" class="btn btn-secondary">
                                                <i class="bi bi-x-circle me-1"></i>Réinitialiser
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>

                        <!-- Info période sélectionnée -->
                        <div class="alert alert-info mb-3">
                            <i class="bi bi-info-circle me-2"></i>
                            <strong>Période sélectionnée :</strong> 
                            <c:forEach var="m" items="${listeMois}">
                                <c:if test="${selectedMois == m.value}">${m.label}</c:if>
                            </c:forEach> ${selectedAnnee}
                        </div>

                        <!-- Tableau des passagers -->
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>ID</th>
                                        <th>Nom</th>
                                        <th>Prénom</th>
                                        <th>Email</th>
                                        <th>Type</th>
                                        <th>Points Totaux</th>
                                        <th class="text-center bg-info text-white">Points du Mois</th>
                                        <th class="text-center bg-success text-white">Points de l'Année</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${not empty passagers}">
                                            <c:forEach var="passager" items="${passagers}">
                                                <tr>
                                                    <td>${passager.id}</td>
                                                    <td>${passager.nom}</td>
                                                    <td>${passager.prenom}</td>
                                                    <td>${passager.email}</td>
                                                    <td>
                                                        <c:if test="${not empty passager.typePassager}">
                                                            <span class="badge bg-secondary">${passager.typePassager.type}</span>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-dark">
                                                            <i class="bi bi-star-fill text-warning me-1"></i>
                                                            ${passager.fidelite != null ? passager.fidelite : 0}
                                                        </span>
                                                    </td>
                                                    <td class="text-center">
                                                        <span class="badge bg-info fs-6">
                                                            <i class="bi bi-calendar-month me-1"></i>
                                                            ${pointsParMois[passager.id] != null ? pointsParMois[passager.id] : 0}
                                                        </span>
                                                    </td>
                                                    <td class="text-center">
                                                        <span class="badge bg-success fs-6">
                                                            <i class="bi bi-calendar-event me-1"></i>
                                                            ${pointsParAnnee[passager.id] != null ? pointsParAnnee[passager.id] : 0}
                                                        </span>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="8" class="text-center text-muted py-4">
                                                    <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                                                    Aucun passager trouvé
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>

                        <!-- Résumé -->
                        <div class="row mt-3">
                            <div class="col-md-4">
                                <div class="card bg-primary text-white">
                                    <div class="card-body text-center">
                                        <h3>${passagers.size()}</h3>
                                        <small>Passagers affichés</small>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="card bg-info text-white">
                                    <div class="card-body text-center">
                                        <c:set var="totalPointsMois" value="0"/>
                                        <c:forEach var="passager" items="${passagers}">
                                            <c:set var="totalPointsMois" value="${totalPointsMois + (pointsParMois[passager.id] != null ? pointsParMois[passager.id] : 0)}"/>
                                        </c:forEach>
                                        <h3>${totalPointsMois}</h3>
                                        <small>Total Points du Mois</small>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="card bg-success text-white">
                                    <div class="card-body text-center">
                                        <c:set var="totalPointsAnnee" value="0"/>
                                        <c:forEach var="passager" items="${passagers}">
                                            <c:set var="totalPointsAnnee" value="${totalPointsAnnee + (pointsParAnnee[passager.id] != null ? pointsParAnnee[passager.id] : 0)}"/>
                                        </c:forEach>
                                        <h3>${totalPointsAnnee}</h3>
                                        <small>Total Points de l'Année</small>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
