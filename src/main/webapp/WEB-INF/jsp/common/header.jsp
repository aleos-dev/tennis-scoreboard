<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<header class="site-header">
    <nav class="main-nav">
        <ul>
            <li><a href="${pageContext.request.contextPath}/">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/matches">Matches</a></li>
            <li><a href="${pageContext.request.contextPath}/matches?status=ongoing">Ongoing</a></li>
            <li><a href="${pageContext.request.contextPath}/players">Players</a></li>
        </ul>
    </nav>
</header>