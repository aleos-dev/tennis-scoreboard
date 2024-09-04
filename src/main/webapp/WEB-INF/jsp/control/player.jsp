<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<%--<c:set var="editMode" value="${param.editMode == 'false'}"/>--%>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="../common/head.jsp" %>
    <title>Player profile</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/css/player.css">
</head>

<body>

<c:set var="editMode" value="${param.editMode == null ? requestScope.editMode : param.editMode}"/>

<div class="content horizontal-center vertical-center">
    <c:choose>
        <c:when test="${editMode}">

        <form action="${pageContext.request.contextPath}/players/${requestScope.playerDto.name()}" method="post" enctype="multipart/form-data">

                <input type="hidden" name="_method" value="PATCH">

                <div class="form-group">
                    <label for="player-name">Name:</label>
                    <input type="text" id="player-name" name="name" value="${requestScope.playerDto.name()}" required>
                </div>

                <%@ include file="../fragment/player-info.jspf" %>

                <button type="submit">Save Changes</button>
            <a href="${pageContext.request.contextPath}/players/${requestScope.playerDto.name()}">Cancel</a>
            </form>
        </c:when>
        <c:otherwise>
            <c:if test="${not empty requestScope.playerDto}">

                <h1>${requestScope.playerDto.name()}</h1>

                <%@ include file="../fragment/player-info.jspf" %>

                <c:if test="${not empty requestScope.playerDto.ongoingMatchUuid()}">
                    <div class="ongoing-match">
                        <p>
                            <a href="${pageContext.request.contextPath}/matches/${requestScope.playerDto.ongoingMatchUuid()}"
                               class="matches-link">Currently in match.
                                UUID: ${requestScope.playerDto.ongoingMatchUuid()}</a>
                        </p>
                    </div>
                </c:if>

                <p><a href="${requestScope.playerDto.matchesEndpoint()}">View Player's Matches</a></p>

                <p><a href="${pageContext.request.contextPath}/players">Back to Player Search</a></p>
            </c:if>
        </c:otherwise>
    </c:choose>

</div>

<c:if test="${not empty requestScope.errorMessages}">
    <div class="error content">
        <ul>
            <c:forEach items="${requestScope.errorMessages}" var="message">
                <li>${message}</li>
            </c:forEach>
        </ul>
    </div>
</c:if>


<%@ include file="../common/footer.jsp" %>
</body>
</html>
