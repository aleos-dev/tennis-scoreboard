<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:useBean id="completedMatch" scope="request" type="com.aleos.model.dto.out.ConcludedMatchDto"/>

<div class="completed-match-display">
    <h3>Completed: Match between ${completedMatch.playerOne} and ${completedMatch.playerTwo}</h3>
    <p>Info: Format: ${completedMatch.info.format}, Winner: ${completedMatch.winner}</p>
    <p>Concluded at: ${completedMatch.eventDateTime}</p>
</div>