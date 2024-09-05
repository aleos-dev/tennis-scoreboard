function toggleHistory() {
    var historyDiv = document.getElementById('matchHistory');
    var footer = document.querySelector('.site-footer');
    if (footer.style.position !== 'sticky') {
        footer.style.position = 'sticky';
        footer.style.bottom = '0';
        historyDiv.style.display = 'block';
    } else {
        footer.style.position = ''
        historyDiv.style.display = 'none';
    }
}