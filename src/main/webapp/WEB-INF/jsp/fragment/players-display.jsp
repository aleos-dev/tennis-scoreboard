<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<c:forEach var="player" items="${requestScope.playersDto.content}">
    <a href="${pageContext.request.contextPath}/players/${player.name()}" class="player-details-ref">

        <div class="player-item">
            <h3>
                <span class="player-name">${player.name()}</span>
                <span class="country">${player.country()}</span>
                <span class="image">${player.playerImageUrl()}</span>
            </h3>

            <div class="player-details">
                <span>Go to: </span>
                <c:if test="${not empty player.ongoingMatchUuid()}">
                    <a class="ongoing-match-ref"
                       href="${pageContext.request.contextPath}/matches/${player.ongoingMatchUuid()}">
                        ongoing match
                    </a>
                </c:if>
                <a class="matches-ref" href="${player.matchesEndpoint()}&status=any">
                    <span>match list</span>
                </a>
            </div>
        </div>
    </a>
</c:forEach>
