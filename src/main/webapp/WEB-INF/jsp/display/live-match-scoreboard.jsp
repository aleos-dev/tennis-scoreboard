<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="p1PointScore" value="${matchScore.scorePoints[0]}" scope="request"/>
<c:set var="p2PointScore" value="${matchScore.scorePoints[1]}" scope="request"/>
<c:set var="p1GameScore" value="${matchScore.scoreGames[0]}" scope="request"/>
<c:set var="p2GameScore" value="${matchScore.scoreGames[1]}" scope="request"/>
<c:set var="p1SetScore" value="${matchScore.scoreSets[0]}" scope="request"/>
<c:set var="p2SetScore" value="${matchScore.scoreSets[1]}" scope="request"/>

<div class="scoreboard">
    <div class="score-entry">
        <div class="title">Point Score:</div>
        <div class="scores">
            <div class="score player1">${p1PointScore}</div>
            <div class="score-divider">:</div>
            <div class="score player2">${p2PointScore}</div>
        </div>
    </div>
    <div class="score-entry">
        <div class="title">Game Score:</div>
        <div class="scores">
            <div class="score player1">${p1GameScore}</div>
            <div class="score-divider">:</div>
            <div class="score player2">${p2GameScore}</div>
        </div>
    </div>
    <div class="score-entry">
        <div class="title">Set Score:</div>
        <div class="scores">
            <div class="score player1">${p1SetScore}</div>
            <div class="score-divider">:</div>
            <div class="score player2">${p2SetScore}</div>
        </div>
    </div>
</div>
