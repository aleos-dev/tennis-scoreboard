<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>Players</title>
    <%@ include file="../common/head.jsp" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/players.css">
</head>

<body>
<%@ include file="../common/header.jsp" %>
<%@ include file="../fragment/error-display.jspf" %>

<div class="content">
    <c:if test="${not empty requestScope.playersDto}">

        <div class="div-players-section">
            <%@ include file="../fragment/playerFilterCriteria.jspf" %>

            <c:if test="${empty requestScope.playersDto.content}">
                <div class="no-players"><span>No players found</span></div>
            </c:if>

            <section class="players-section">
                <%@ include file="../fragment/players-display.jspf" %>
            </section>

            <section class="content horizontal-center pagination-section">
                <c:set var="dto" value="${requestScope.playersDto}" scope="request"/>
                <%@ include file="../common/pagination.jsp" %>
            </section>
        </div>
    </c:if>
</div>

<%@ include file="../common/footer.jsp" %>
</body>
</html>
