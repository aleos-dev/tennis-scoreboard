<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<c:forEach var="match" items="${matchesDto.content}">

    <c:choose>

        <c:when test="${match.status == 'ONGOING'}">
            <c:set var="liveMatch" value="${match}" scope="request" />
            <div class="match-item ongoing">
                <%@ include file="live-matches.jsp" %>
            </div>
        </c:when>

        <c:otherwise>
            <c:set var="completedMatch" value="${match}" scope="request" />
            <div class="match-item completed">
                <%@ include file="completed-matches.jsp" %>
            </div>
        </c:otherwise>

    </c:choose>

</c:forEach>
