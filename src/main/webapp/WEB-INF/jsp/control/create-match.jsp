<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>

<html lang="en">

<head>
    <%@ include file="../common/head.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/css/create-match.css">
    <title>New Match</title>
</head>

<body>
<%@ include file="../common/header.jsp" %>
<%@ include file="../fragment/error-display.jspf" %>

<div class="content horizontal-center">

    <h1>Enter the match information</h1>

    <form action="matches" class="content" method="post">

        <fieldset class="horizontal-center">
            <legend>Player Information</legend>

            <label for="playerOneNameId">Player One Name:
                <input type="text" name="playerOneName" id="playerOneNameId" required minlength="5"
                       value="${requestScope.matchPayload != null ? requestScope.matchPayload.playerOneName() : ""}">
            </label><br>

            <label for="playerTwoNameId">Player Two Name:
                <input type="text" name="playerTwoName" id="playerTwoNameId" required minlength="5"
                       value="${requestScope.matchPayload != null ? requestScope.matchPayload.playerTwoName() : ""}">
            </label><br>
        </fieldset>

        <fieldset>
            <legend>Match Information</legend>

            <label for="format-id" class="format-label">Format:
                <select name="matchFormat" id="format-id" required>
                    <option value="bo3" ${requestScope.matchPayload != null && 'bo3'.equalsIgnoreCase(requestScope.matchPayload.format()) ? 'selected' : ''}>
                        BO3
                    </option>
                    <option value="bo5" ${requestScope.matchPayload != null && 'bo5'.equalsIgnoreCase(requestScope.matchPayload.format()) ? 'selected' : ''}>
                        BO5
                    </option>
                </select>
            </label><br>
        </fieldset>

        <button type="submit" class="cta-button"> Start a New Match</button>
    </form>

</div>

<%@ include file="../common/footer.jsp" %>
</body>
</html>
