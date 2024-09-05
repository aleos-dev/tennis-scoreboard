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
    <c:if test="${not empty requestScope.playersDto}">

        <jsp:useBean id="playersDto" scope="request" type="com.aleos.model.dto.out.PlayersDto"/>

        <div class="div-players-section">
            <%@ include file="../fragment/playerFilterCriteria.jspf" %>

            <c:if test="${empty playersDto.content}">
                <div class="no-players"><span>There is no any player</span></div>
            </c:if>

            <section class="players-section">
                <%@ include file="../fragment/players-display.jspf" %>
            </section>

            <section class="content horizontal-center pagination-section">
                <c:set var="dto" value="${playersDto}" scope="request"/>
                <%@ include file="../common/pagination.jsp" %>
            </section>
        </div>
    </c:if>
</div>

<%@ include file="../common/footer.jsp" %>

</body>
</html>
