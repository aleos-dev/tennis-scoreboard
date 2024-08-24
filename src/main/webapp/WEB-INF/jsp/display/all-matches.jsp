<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<c:forEach var="match" items="${matchesDto.content}">
    <c:choose>

        <c:when test="${match.status == 'ONGOING'}">
            <c:set var="liveMatch" value="${match}" scope="request"/>
            <jsp:useBean id="liveMatch" scope="request" type="com.aleos.model.dto.out.ActiveMatchDto"/>

            <a href="${pageContext.request.contextPath}/matches/${liveMatch.id}" class="match-details-link">
                <div class="live-match">
                    <div class="match-item">

                        <h3>
                            <span>LIVE: </span>
                            <span class="player-name">${liveMatch.playerOne}</span>
                            <span class="vs-icon">versus</span>
                            <span class="player-name">${liveMatch.playerTwo}</span>
                        </h3>

                        <div class="match-details">
                            <p>Started at: <span class="match-date">${fn:substring(liveMatch.eventDateTime.toLocalTime().toString(), 0, 5)}</span></p>
                            <p>Current Game Score: <span class="match-scores">${liveMatch.score.scorePoints[0]}<b> : </b>${liveMatch.score.scorePoints[1]}</span></p>
                        </div>

                    </div>
                </div>
            </a>
        </c:when>

        <c:otherwise>
            <c:set var="completedMatch" value="${match}" scope="request"/>
            <jsp:useBean id="completedMatch" scope="request" type="com.aleos.model.dto.out.ConcludedMatchDto"/>

            <a href="${pageContext.request.contextPath}/matches/${completedMatch.id}" class="match-details-link">
                <div class="completed-match">
                    <div class="match-item">

                        <h3>
                            <span class="player-name">${completedMatch.playerOne}</span>
                            <span class="vs-icon">versus</span>
                            <span class="player-name">${completedMatch.playerTwo}</span>
                        </h3>

                        <div class="match-details">

                            <p>Format: <span class="match-format">${completedMatch.info.format}</span></p>
                            <p>Winner: <span class="match-winner">${completedMatch.winner}</span></p>
                            <p>Concluded on: <span
                                    class="match-date">${completedMatch.eventDateTime.toLocalDate()}</span></p>
                        </div>

                    </div>
                </div>
            </a>
        </c:otherwise>

    </c:choose>
</c:forEach>
