function showError(errorText) {
    document.getElementById("errorText").innerHTML = errorText;
    document.getElementById("error").style.visibility = "visible";
}

function closeError() {
    document.getElementById("error").style.visibility = "hidden";
}
