<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<c:forEach var="notification" items="${requestScope.notifications}">
    <div class="notification" style="display:none;">${notification}</div>
</c:forEach>

<script>
    window.onload = function () {
        const notifications = document.querySelectorAll('.notification');
        notifications.forEach((notification, index) => {
            setTimeout(() => {
                notification.style.display = 'block'; // Show the message
                setTimeout(() => {
                    notification.style.display = 'none'; // Hide the message
                }, 1200);
            }, index * 1500); // Delay subsequent notifications
        });
    };
</script>