function submitScore(playerName) {
    document.getElementById('pointWinner').value = playerName;
    document.forms[0].submit();
}