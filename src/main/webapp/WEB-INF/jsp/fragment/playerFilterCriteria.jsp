<jsp:useBean id="filterCriteria" scope="request" type="com.aleos.model.dto.in.PlayerFilterCriteria"/>

<div class="filter-form">
    <form action="${requestScope.baseUrl}" method="get">
        <label for="player-name-id">Name: </label>
        <input type="text" name="playerName" id="player-name-id" placeholder="Player Name"
               value="${filterCriteria.name() != null ? filterCriteria.name() : ''}"/>

        <label for="country-code-id">Country Code: </label>
        <select name="country" id="country-code-id" required onchange="toggleCustomCountryInput(this)">
            <c:forEach var="code" items="${requestScope.countryCodes}">
                <option value="" selected></option>
                <option value="${code}" ${filterCriteria.country() == code ? 'selected' : ''}>${code}</option>
            </c:forEach>
        </select>

        <button class="cta-button" type="submit">Filter</button>
    </form>
</div>
