<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <%@ include file="../common/head.jsp" %>
    <title>Player profile</title>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/css/player.css">
    <script src="${basePath}/static/js/imageInput.js"></script>
</head>

<body>
<%@ include file="../common/header.jsp" %>

<c:set var="editMode" value="${not empty param.editMode ? param.editMode : requestScope.editMode}"/>

<div class="content">

    <c:choose>
        <c:when test="${empty requestScope.errorMessages}">

            <div class="card horizontal-center vertical-center">
                <c:choose>

                    <c:when test="${editMode}">

                        <form action="${basePath}/players/${requestScope.playerDto.name()}"
                              method="post"
                              enctype="multipart/form-data">

                            <input type="hidden" name="_method" value="PATCH">

                            <div class="form-group">
                                <label for="player-name">Name:</label>
                                <input type="text" id="player-name" name="name" value="${requestScope.playerDto.name()}"
                                       required minlength="5">
                            </div>

                            <%@ include file="../fragment/player-info.jspf" %>

                            <button type="submit" class="cta-button">Save Changes</button>
                            <a href="${basePath}/players/${requestScope.playerDto.name()}">Cancel</a>
                        </form>
                    </c:when>

                    <c:otherwise>
                        <c:if test="${not empty requestScope.playerDto}">

                            <h1>${requestScope.playerDto.name()}</h1>
                            <%@ include file="../fragment/player-info.jspf" %>

                            <div class="player-info-links content">
                                <c:if test="${not empty requestScope.playerDto.ongoingMatchUuid()}">
                                    <div class="ongoing-match">
                                        <p>
                                            <a href="${basePath}/matches/${requestScope.playerDto.ongoingMatchUuid()}"
                                               class="matches-link">Check ongoing match</a>
                                        </p>
                                    </div>
                                </c:if>


                                <div class="refs">
                                    <p><a href="${requestScope.playerDto.matchesEndpoint()}">View Player's Matches</a>
                                    </p>

                                    <p><a href="${basePath}/players">Back to Player Search</a></p>
                                </div>
                            </div>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:when>
        <c:otherwise>
            <%@ include file="../fragment/error-display.jspf" %>
        </c:otherwise>

    </c:choose>

</div>

<%@ include file="../common/footer.jsp" %>
</body>
</html>
