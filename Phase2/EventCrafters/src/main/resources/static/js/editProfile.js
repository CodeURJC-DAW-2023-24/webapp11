document.addEventListener("DOMContentLoaded", function() {
    document.getElementById('edit-profile-btn').addEventListener('click', function() {
        var editProfilePopup = document.getElementById('edit-profile-popup');
        if (editProfilePopup.style.display === 'none') {
            editProfilePopup.style.display = 'block';
        } else {
            editProfilePopup.style.display = 'none';
        }
    });
});

function warn() {
    return window.confirm('Si guarda los datos, se le obligará a iniciar sesión con las nuevas credenciales');
}

function cancel() {
    window.location.href = 'profile';
    return false;
}
