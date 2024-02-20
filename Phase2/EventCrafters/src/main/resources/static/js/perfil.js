const deleteAccountBtn = document.getElementById('delete-account-btn');

const askForConfirmation = () => {
    var result = confirm("¿Estás seguro de que deseas continuar?");
    if (result == true) {
        // El usuario hizo clic en "Aceptar"
        alert("Has aceptado.");
    } else {
        // El usuario hizo clic en "Cancelar"
        alert("Has cancelado la acción.");
    }
}
deleteAccountBtn.addEventListener('click', () => askForConfirmation())

