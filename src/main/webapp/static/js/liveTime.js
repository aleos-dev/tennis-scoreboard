function updateTime() {
    const now = new Date();
    const timeString = now.toLocaleTimeString();
    document.getElementById('live-time').textContent = timeString;
}

setInterval(updateTime, 1000);