<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>Matches</title>
    <%@ include file="../common/head.jsp" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/matches.css">
</head>

<body>
<%@ include file="../common/header.jsp" %>
<%@ include file="../fragment/error-display.jspf" %>

<div class="content">
    <c:if test="${not empty requestScope.matchesDto}">

        <div class="div-matches-section">
            <%@ include file="../fragment/matchFilterCriteria.jspf" %>

            <c:if test="${empty requestScope.matchesDto.content}">
                <div class="no-matches"><span>There are no matches</span></div>
            </c:if>

            <section class="matches-section">
                <%@ include file="../fragment/matches-display.jspf" %>
            </section>

            <section class="content horizontal-center pagination-section">

                <c:set var="dto" value="${requestScope.matchesDto}" scope="request"/>
                <%@ include file="../common/pagination.jsp" %>
            </section>
        </div>
    </c:if>
</div>

<%@ include file="../common/footer.jsp" %>
</body>
</html>
