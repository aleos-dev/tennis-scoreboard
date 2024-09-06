<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<script src="${pageContext.request.contextPath}/static/js/notifications.js"></script>

<c:forEach var="notification" items="${requestScope.notifications}">
    <div class="notification">${notification}</div>
</c:forEach>

<script>
    window.onload = function () {
        displayNotifications();
    };
</script>