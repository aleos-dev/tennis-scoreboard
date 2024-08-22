<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <%@ include file="WEB-INF/jsp/common/head.jsp" %>
    <title>Welcome to Tennis Scoreboard</title>
</head>

<body>

<%@ include file="WEB-INF/jsp/common/header.jsp" %>

<div class="hero-section">
    <h1>Welcome to Tennis Scoreboard</h1>
    <p>Track your tennis matches in real-time, manage player profiles, and review completed games.</p>
    <a href="${pageContext.request.contextPath}/new-match" class="cta-button">Start a New Match</a>
</div>


<%@ include file="WEB-INF/jsp/common/footer.jsp" %>

</body>
</html>
