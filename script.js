document.addEventListener('DOMContentLoaded', function() {
    const dateElement = document.getElementById('date');
    dateElement.textContent = 'Current Date and Time: ' + new Date().toLocaleString();
})