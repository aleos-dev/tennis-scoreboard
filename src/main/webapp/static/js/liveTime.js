function updateTime() {
    const now = new Date();
    document.getElementById('live-time').textContent = now.toLocaleTimeString();
}

setInterval(updateTime, 1000);