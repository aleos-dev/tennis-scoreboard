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

<c:if test="${not empty requestScope.errorMessages}">
    <div class="error content">
        <ul>
            <c:forEach items="${requestScope.errorMessages}" var="message">
                <li>${message}</li>
            </c:forEach>
        </ul>
    </div>
</c:if>
<div class="content">
    <c:if test="${not empty requestScope.matchesDto}">

        <jsp:useBean id="matchesDto" scope="request" type="com.aleos.model.dto.out.MatchesDto"/>

        <div class="div-matches-section">
            <%@ include file="../fragment/matchFilterCriteria.jspf" %>

            <c:if test="${empty matchesDto.content}">
                <div class="no-matches"><span>There is no any match</span></div>
            </c:if>

            <section class="matches-section">
                <%@ include file="../fragment/matches-display.jspf" %>
            </section>

            <section class="content horizontal-center pagination-section">

                <c:set var="dto" value="${matchesDto}" scope="request"/>
                <%@ include file="../common/pagination.jsp" %>
            </section>
        </div>
    </c:if>
</div>

<%@ include file="../common/footer.jsp" %>

</body>
</html>
