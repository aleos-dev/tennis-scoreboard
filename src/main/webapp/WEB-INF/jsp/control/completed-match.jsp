<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:useBean id="match" scope="request" type="com.aleos.model.dto.out.ConcludedMatchDto"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="../common/head.jsp" %>
    <title>Match Info</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/completed-match.css">
    <script src="${pageContext.request.contextPath}/static/js/toggleHistory.js"></script>
</head>

<body>
<%@ include file="../common/header.jsp" %>

<div class="completed-header">
    <div>
        <div class="completed-date">
            <span id="completed-date">Completed on: <span class="date-value">${match.eventDateTime.toLocalDate()}</span></span>
            <span class="match-id">Match ID: <span class="match-id-value">${match.id}</span></span>
        </div>
    </div>
    <span class="match-announce">
        <span class="player1 ${match.winner != null && match.winner.equalsIgnoreCase(match.playerOne) ? 'winner-glow' : 'loser-fade'}">
            ${match.playerOne}
        </span>
        vs
        <span class="player2 ${match.winner != null && match.winner.equalsIgnoreCase(match.playerTwo) ? 'winner-glow' : 'loser-fade'}">
            ${match.playerTwo}
        </span>
    </span>
</div>

<div class="completed-match-container">
    <div class="player ${match.winner != null && match.winner.equalsIgnoreCase(match.playerOne) ? 'player-winner' : 'player-loser'}">
        <img src="${pageContext.request.contextPath}/avatars/${match.playerOne}" alt="Player" class="player-image">
    </div>

    <div class="scoreboard-container">
        <div class="vs-icon">
            <img src="${pageContext.request.contextPath}/static/images/vs-element.webp" alt="VS">
        </div>
        <%@ include file="../fragment/completed-match-scoreboard.jspf" %>
    </div>

    <div class="player ${match.winner != null && match.winner.equalsIgnoreCase(match.playerTwo) ? 'player-winner' : 'player-loser'}">
        <img src="${pageContext.request.contextPath}/avatars/${match.playerTwo}" alt="Player" class="player-image">
    </div>
</div>

<div class="button-container">
    <button onclick="toggleHistory()" class="cta-button">Toggle History</button>
</div>

<div id="matchHistory" class="match-history">
    <span>Match History:</span>
    <c:choose>
        <c:when test="${not empty match.info.historyEntries}">
            <ul>
                <c:forEach var="entry" items="${match.info.historyEntries}">
                    <li>${entry}</li>
                </c:forEach>
            </ul>
        </c:when>
        <c:otherwise>
            <p>No match history available.</p>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="../common/footer.jsp" %>
</body>
</html>
