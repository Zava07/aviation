<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <c:choose>
        <c:when test="${not empty pageTitle}">
            <title>${pageTitle} - Aviation Manager</title>
        </c:when>
        <c:otherwise>
            <title>Aviation Manager</title>
        </c:otherwise>
    </c:choose>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <c:if test="${not empty pageCss}">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/${pageCss}">
    </c:if>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body>
<div class="app-container">
    <aside class="sidebar">
        <div class="sidebar-header">
            <div class="logo">
                <i class="fas fa-plane"></i>
                <h2>AeroManager</h2>
            </div>
            <p class="subtitle">Systeme de Gestion Aerienne</p>
        </div>
        <nav class="nav-section">
            <p class="nav-section-title">Menu Principal</p>
            <ul class="nav-menu">
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/" class="nav-link">
                        <i class="fas fa-home"></i>
                        <span>Tableau de Bord</span>
                    </a>
                </li>
            </ul>
        </nav>
        <nav class="nav-section">
            <p class="nav-section-title">Gestion des Vols</p>
            <ul class="nav-menu">
                <li class="nav-item has-submenu ${currentPage == 'vols' || currentPage == 'avions' || currentPage == 'vol-instances' || currentPage == 'aeroports' ? 'open' : ''}">
                    <a href="#" class="nav-link">
                        <i class="fas fa-plane-departure"></i>
                        <span>Vols & Avions</span>
                    </a>
                    <ul class="submenu">
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/vols" class="nav-link ${currentPage == 'vols' ? 'active' : ''}">
                                <i class="fas fa-route"></i>
                                <span>Vols</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/avions" class="nav-link ${currentPage == 'avions' ? 'active' : ''}">
                                <i class="fas fa-plane"></i>
                                <span>Avions</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/vol-instances" class="nav-link ${currentPage == 'vol-instances' ? 'active' : ''}">
                                <i class="fas fa-calendar-day"></i>
                                <span>Instances de Vol</span>
                            </a>
                        </li>
                        
                    </ul>
                </li>
            </ul>
        </nav>
        <nav class="nav-section">
            <p class="nav-section-title">Gestion des Reservations</p>
            <ul class="nav-menu">
                <li class="nav-item has-submenu ${currentPage == 'reservations' || currentPage == 'billets' || currentPage == 'paiements' || currentPage == 'passagers' ? 'open' : ''}">
                    <a href="#" class="nav-link">
                        <i class="fas fa-book"></i>
                        <span>Reservations</span>
                    </a>
                    <ul class="submenu">
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/reservations" class="nav-link ${currentPage == 'reservations' ? 'active' : ''}">
                                <i class="fas fa-book-open"></i>
                                <span>Reservations</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/billets" class="nav-link ${currentPage == 'billets' ? 'active' : ''}">
                                <i class="fas fa-ticket-alt"></i>
                                <span>Billets</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/paiements" class="nav-link ${currentPage == 'paiements' ? 'active' : ''}">
                                <i class="fas fa-credit-card"></i>
                                <span>Paiements</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/passagers" class="nav-link ${currentPage == 'passagers' ? 'active' : ''}">
                                <i class="fas fa-user"></i>
                                <span>Passagers</span>
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </nav>
        <nav class="nav-section">
            <p class="nav-section-title">Gestion des Caisses</p>
            <ul class="nav-menu">
                <li class="nav-item has-submenu ${currentPage == 'reservations' || currentPage == 'billets' || currentPage == 'paiements' || currentPage == 'passagers' ? 'open' : ''}">
                    <a href="#" class="nav-link">
                        <i class="fas fa-wallet"></i>
                        <span>Caisses</span>
                    </a>
                    <ul class="submenu">
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/caisses" class="nav-link ${currentPage == 'caisses' ? 'active' : ''}">
                                <i class="fas fa-cash-register"></i>
                                <span>Caisses</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/mouvement-caisses" class="nav-link ${currentPage == 'mouvement-caisses' ? 'active' : ''}">
                                <i class="fas fa-exchange-alt"></i>
                                <span>Mouvement Caisses</span>
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </nav>
        <nav class="nav-section">
            <p class="nav-section-title">Gestion des Entites</p>
            <ul class="nav-menu">
                <li class="nav-item has-submenu ${currentPage == 'passagers' || currentPage == 'reservations' || currentPage == 'billets' || currentPage == 'paiements' || currentPage == 'personnel' || currentPage == 'classes' || currentPage == 'modeles' || currentPage == 'sieges' || currentPage == 'prix' || currentPage == 'type-passagers' ? 'open' : ''}">
                    <a href="#" class="nav-link">
                        <i class="fas fa-cogs"></i>
                        <span>Entites</span>
                    </a>
                    <ul class="submenu">
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/personnel" class="nav-link ${currentPage == 'personnel' ? 'active' : ''}">
                                <i class="fas fa-user-tie"></i>
                                <span>Personnel</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/classes" class="nav-link ${currentPage == 'classes' ? 'active' : ''}">
                                <i class="fas fa-layer-group"></i>
                                <span>Classes</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/modeles" class="nav-link ${currentPage == 'modeles' ? 'active' : ''}">
                                <i class="fas fa-cog"></i>
                                <span>Modeles Avion</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/sieges" class="nav-link ${currentPage == 'sieges' ? 'active' : ''}">
                                <i class="fas fa-chair"></i>
                                <span>Sieges</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/prix" class="nav-link ${currentPage == 'prix' ? 'active' : ''}">
                                <i class="fas fa-euro-sign"></i>
                                <span>Prix Vol / Classe</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/type-passagers" class="nav-link ${currentPage == 'type-passagers' || currentPage == 'typepassagers' ? 'active' : ''}">
                                <i class="fas fa-users"></i>
                                <span>Types Passagers</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/aeroports" class="nav-link ${currentPage == 'aeroports' ? 'active' : ''}">
                                <i class="fas fa-tower-broadcast"></i>
                                <span>Aeroports</span>
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </nav>
    </aside>
    <main class="main-content">
        <header class="main-header">
            <div class="header-left">
                <nav class="breadcrumb">
                    <a href="${pageContext.request.contextPath}/"><i class="fas fa-home"></i></a>
                    <span class="separator">/</span>
                    <span class="current">${pageTitle}</span>
                </nav>
            </div>
            <div class="header-right">
                <div class="user-menu">
                    <img src="https://ui-avatars.com/api/?name=Admin&background=1e3a5f&color=fff" alt="User">
                </div>
            </div>
        </header>
        <div class="page-content">
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">${successMessage}</div>
            </c:if>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>
