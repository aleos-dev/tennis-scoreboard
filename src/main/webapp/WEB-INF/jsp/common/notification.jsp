<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<c:forEach var="notification" items="${requestScope.notifications}">
    <div class="notification">${notification}</div>
</c:forEach>

<script>
    window.onload = function () {
        const notifications = document.querySelectorAll('.notification');
        notifications.forEach((notification, index) => {
            setTimeout(() => {
                notification.style.display = 'block';
                setTimeout(() => {
                    notification.style.display = 'none';
                }, 1200);
            }, index * 1500); // Delay subsequent notifications
        });
    };
</script>