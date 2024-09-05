<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:useBean id="match" scope="request" type="com.aleos.model.dto.out.ActiveMatchDto"/>
<c:set var="matchScore" scope="request" value="${match.score}"/>

<c:set var="matchId" value="${matchScore.matchID}" scope="request"/>
<c:set var="p1Name" value="${matchScore.playerOneName}" scope="request"/>
<c:set var="p2Name" value="${matchScore.playerTwoName}" scope="request"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="../common/head.jsp" %>
    <title>Match Control</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/css/live-match-notification.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/css/live-match.css">
    <script src="${pageContext.request.contextPath}/static/js/submitScore.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/liveTime.js"></script>

</head>

<body>
<%@ include file="../common/header.jsp" %>

<div class="info-header">
    <div class="match-id">Match ID: <span>${matchId}</span></div>
    <div class="live-time">LIVE: <span id="live-time"></span></div>
</div>

<div class="content horizontal-center">
    <%@ include file="../fragment/live-match-scoreboard.jspf" %>


    <div class="score-label">Award Point</div>

    <form class="score-control" action="${pageContext.request.contextPath}/match-scores/${matchScore.matchID}"
          method="post">
        <input type="hidden" name="pointWinner" id="pointWinner" value="">
        <div class="button">
            <button type="button" onclick="submitScore('${p1Name}')">${p1Name}</button>
        </div>

        <div class="button">
            <button type="button" onclick="submitScore('${p2Name}')">${p2Name}</button>
        </div>
    </form>
</div>
<c:set var="notifications" scope="request" value="${matchScore.notifications}"/>
<%@ include file="../common/notification.jsp" %>

<%@ include file="../common/footer.jsp" %>

</body>
</html>