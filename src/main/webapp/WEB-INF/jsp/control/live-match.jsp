<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="matchScore" scope="request" type="com.aleos.model.MatchScore"/>

<c:set var="p1Name" value="${matchScore.playerOneName}" scope="request"/>
<c:set var="p2Name" value="${matchScore.playerTwoName}" scope="request"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="../common/head.jsp" %>
    <title>Match Control</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/css/active-match-notification.css">
    <script src="${pageContext.request.contextPath}/static/js/submitScore.js"></script>
</head>
<body>

<h5>Match ID: ${matchScore.matchID}</h5>


<%@ include file="../display/live-match-scoreboard.jsp" %>

<form action="${pageContext.request.contextPath}/match-scores/${matchScore.matchID}" method="post">
    <input type="hidden" name="pointWinner" id="pointWinner" value="">
    <button type="button" onclick="submitScore('${p1Name}')">A point to ${p1Name}</button>
    <button type="button" onclick="submitScore('${p2Name}')">A point to ${p2Name}</button>
</form>

<%@ include file="../common/footer.jsp"%>

<c:if test="${not empty requestScope.notifications}">
    <%@ include file="../common/notification.jsp" %>
</c:if>

</body>
</html>