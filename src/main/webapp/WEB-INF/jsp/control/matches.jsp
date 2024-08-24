<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<jsp:useBean id="matchesDto" scope="request" type="com.aleos.model.dto.out.MatchesDto"/>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>Matches</title>
    <%@ include file="../common/head.jsp" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/matches.css">
</head>

<body>
<%@ include file="../common/header.jsp" %>

<div class="div-matches-section">

    <c:if test="${empty matchesDto.content}">
        <div class="no-matches"><span>There is no any matches</span></div>
    </c:if>

    <section class="matches-section">
        <div class="matches-list">`
            <%@ include file="../display/all-matches.jsp" %>
        </div>
    </section>

    <section class="pagination-section">
        <div class="pagination-controls">
            <%@ include file="../common/pagination.jsp" %>
        </div>
    </section>
</div>

<%@ include file="../common/footer.jsp" %>

</body>
</html>
