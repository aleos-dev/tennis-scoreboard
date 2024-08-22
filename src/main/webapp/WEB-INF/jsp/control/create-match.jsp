<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>

<html lang="en">

<head>
    <title>Register new match</title>
</head>

<body>

<%@ include file="../common/header.jsp" %>


<div class="new-match">

    <h1>Enter new match information</h1>
    <form action="${pageContext.request.contextPath}/matches" method="post">

        <fieldset>
            <legend>Player Information</legend>
            <label for="playerOneNameId">Player one name:
                <input type="text" name="playerOneName" id="playerOneNameId">
            </label><br>

            <label for="playerTwoNameId">Player two name:
                <input type="text" name="playerTwoName" id="playerTwoNameId">
            </label><br>
        </fieldset>
        <fieldset>
            <legend>Match Information</legend>
            <label for="formatId">Format:
                <select name="matchFormat" id="formatId" required>
                    <option value="bo3" selected>BO3</option>
                    <option value="bo5">BO5</option>
                </select>
            </label><br>
        </fieldset>

        <button type="submit">Start a New Match</button>
    </form>

</div>

<%@ include file="../common/footer.jsp" %>

</body>
</html>
