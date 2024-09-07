function toggleHistory() {
    let historyDiv = document.getElementById('matchHistory');
    if (historyDiv.style.display === 'none' || historyDiv.style.display === '') {
        historyDiv.style.display = 'block';
    } else {
        historyDiv.style.display = 'none';
    }
}
