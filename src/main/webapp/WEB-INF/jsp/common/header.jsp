<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<header class="site-header">
    <c:set var="basePath" value="" />
    <nav class="main-nav">
        <ul>
            <li><a href="${basePath}/">Home</a></li>
            <li><a href="${basePath}/matches">Matches</a></li>
            <li><a href="${basePath}/matches?status=ongoing">Ongoing</a></li>
            <li><a href="${basePath}/players">Players</a></li>
        </ul>
    </nav>
</header>