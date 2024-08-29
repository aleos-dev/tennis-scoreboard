<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:useBean id="match" scope="request" type="com.aleos.model.dto.out.ConcludedMatchDto"/>

<c:set var="matchId" value="${match.id}" scope="request"/>
<c:set var="p1Name" value="${match.playerOne}" scope="request"/>
<c:set var="p2Name" value="${match.playerTwo}" scope="request"/>
<c:set var="winner" value="${match.winner}" scope="request"/>
<c:set var="completedAt" value="${match.eventDateTime}" scope="request"/>


<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="../common/head.jsp" %>
    <title>Match info</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/css/completed-match.css">
</head>

<body>
<%@ include file="../common/header.jsp" %>

<div class="completed-header">
    <div>
        <div class="completed-date">
            <span id="completed-date"> Completed on: <span class="date-value">${completedAt.toLocalDate()}</span></span>
            <span class="match-id"> Match ID: <span class="match-id-value">${matchId}</span></span>
        </div>
    </div>
    <span class="match-announce"><span class="player1">${p1Name} </span><span class="player2">${p2Name}</span></span>
</div>

<div class="completed-match-container">
    <div class="player player-container-1">
        <jsp:include page="../fragment/playerRepresentation.jsp">
            <jsp:param name="name" value="${p1Name}"/>
        </jsp:include>
    </div>

    <div class="scoreboard-container">
        <div class="vs-icon">
            <img src="${pageContext.request.contextPath}/static/images/vs-element.webp" alt="VS">
        </div>

        <%@ include file="../fragment/completed-match-scoreboard.jsp" %>
    </div>

    <div class="player player-container-2">
        <jsp:include page="../fragment/playerRepresentation.jsp">
            <jsp:param name="name" value="${p2Name}"/>
        </jsp:include>
    </div>
</div>


<%@ include file="../common/footer.jsp" %>
</body>
</html>