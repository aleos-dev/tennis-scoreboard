<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="concluded-scoreboard-display">
    <c:forEach var="set" items="${concludedMatch.info.finalScoreRecord.split(',')}">
        <c:set var="scores" value="${fn:split(set, ':')}" scope="page"/>

        <p>Set Score: ${scores[0]}:${scores[1]}</p>
    </c:forEach>
</div>

