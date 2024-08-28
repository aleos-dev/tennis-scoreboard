<jsp:useBean id="filterCriteria" scope="request" type="com.aleos.model.dto.in.MatchFilterCriteria"/>

<div class="filter-form">
    <form action="${requestScope.baseUrl}" method="get">
        <label for="player-name-id">Name: </label>
        <input type="text" name="playerName" id="player-name-id" placeholder="Player Name"
               value="${filterCriteria.playerName() != null ? filterCriteria.playerName() : ''}"/>

        <label for="status-id">Status: </label>
        <select name="status" id="status-id" required>
            <option value="finished" ${filterCriteria.status() == 'finished' ? 'selected' : ''}>Finished</option>
            <option value="any" ${filterCriteria.status() == 'any' ? 'selected' : ''}>Any</option>
            <option value="ongoing" ${filterCriteria.status() == 'ongoing' ? 'selected' : ''}>Ongoing</option>
        </select>

        <label for="before-id">Before Date: </label>
        <input type="datetime-local" id="before-id" name="before" placeholder="Before Date"
               value="${requestScope.formattedBefore}"/>

        <button class="cta-button" type="submit">Filter</button>
    </form>
</div>
