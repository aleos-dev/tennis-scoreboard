<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:useBean id="liveMatch" scope="request" type="com.aleos.model.dto.out.ActiveMatchDto"/>

<div class="ongoing-match-display">
    <h3>LIVE: Match between ${liveMatch.playerOne} and ${liveMatch.playerTwo}</h3>
    <p>SCORE: ${liveMatch.score.matchID}</p>
</div>