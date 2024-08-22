<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="p1PointScore" value="${matchScore.scorePoints[0]}" scope="request"/>
<c:set var="p2PointScore" value="${matchScore.scorePoints[1]}" scope="request"/>
<c:set var="p1GameScore" value="${matchScore.scoreGames[0]}" scope="request"/>
<c:set var="p2GameScore" value="${matchScore.scoreGames[1]}" scope="request"/>
<c:set var="p1SetScore" value="${matchScore.scoreSets[0]}" scope="request"/>
<c:set var="p2SetScore" value="${matchScore.scoreSets[1]}" scope="request"/>


<div class="active-scoreboard-display">
    <h3>LIVE: Match between ${p1Name} and ${p2Name}</h3>
    <p>Point Score: ${p1PointScore}:${p2PointScore}</p>
    <p>Game Score: ${p1GameScore}:${p2GameScore}</p>
    <p>Set Score: ${p1SetScore}:${p2SetScore}</p>
</div>
