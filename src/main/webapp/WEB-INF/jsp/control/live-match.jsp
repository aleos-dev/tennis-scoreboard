<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:useBean id="match" scope="request" type="com.aleos.model.dto.out.ActiveMatchDto"/>
<c:set var="matchScore" scope="request" value="${match.score}"/>

<!DOCTYPE html>
<html lang="en">

<head>
    <%@ include file="../common/head.jsp" %>
    <title>Match Control</title>
    <link rel="stylesheet" type="text/css"
          href="${basePath}/static/css/live-match-notification.css">
    <link rel="stylesheet" type="text/css"
          href="${basePath}/static/css/live-match.css">
    <script src="${basePath}/static/js/submitScore.js"></script>
    <script src="${basePath}/static/js/liveTime.js"></script>
</head>

<body style="background: url('${basePath}/static/images/active-match-background.webp') no-repeat center/cover">

<%@ include file="../common/header.jsp" %>

<div class="info-header">
    <div class="match-id">Match ID: <span>${requestScope.matchId}</span></div>
    <div class="live-time">LIVE: <span id="live-time"></span></div>
</div>

<div class="content horizontal-center">
    <%@ include file="../fragment/live-match-scoreboard.jspf" %>

    <div class="score-label">Award Point</div>

    <form class="score-control"
          action="${basePath}/match-scores/${matchScore.matchID}"
          method="post">

        <input type="hidden" name="pointWinner" id="pointWinner" value="">

        <c:forEach var="playerName" items="${[matchScore.playerOneName, matchScore.playerTwoName]}">
            <button type="button" onclick="submitScore('${playerName}')">${playerName}</button>
        </c:forEach>
    </form>
</div>

<c:set var="notifications" scope="request" value="${matchScore.notifications}"/>
<%@ include file="../common/notification.jsp" %>

<%@ include file="../common/footer.jsp" %>
</body>
</html>