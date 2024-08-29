<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="scoreboard">
    <c:forEach var="set" items="${fn:split(match.info.finalScoreRecord, ',')}" varStatus="status">
        <c:set var="scores" value="${fn:split(set, ':')}" scope="page"/>
        <div class="score-entry">
            <div class="title">Set №${status.count}:</div>
            <div class="scores">
                <div class="score player1">${scores[0]}</div>
                <div class="score-divider">:</div>
                <div class="score player2">${scores[1]}</div>
            </div>
        </div>
    </c:forEach>
</div>
