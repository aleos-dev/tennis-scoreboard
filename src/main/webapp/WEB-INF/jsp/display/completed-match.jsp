<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:useBean id="concludedMatch" scope="request" type="com.aleos.model.dto.out.ConcludedMatchDto"/>

<c:set var="p1Name" value="${concludedMatch.playerOne}" scope="request"/>
<c:set var="p2Name" value="${concludedMatch.playerTwo}" scope="request"/>
<c:set var="winner" value="${concludedMatch.winner}" scope="request"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="../common/head.jsp" %>
    <title>Match info</title>
</head>
<body>
<%@ include file="../common/header.jsp"%>

<h5>Match ID: ${concludedMatch.id}</h5>
<h3>Match between ${concludedMatch.playerOne} and ${concludedMatch.playerTwo} was concluded:
    <b>Winner is </b> ${winner}</h3>

<%@ include file="completed-match-scoreboard.jsp" %>

<%@ include file="../common/footer.jsp"%>
</body>
</html>