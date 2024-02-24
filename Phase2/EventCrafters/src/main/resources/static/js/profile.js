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
deleteAccountBtn.addEventListener('click', () => askForConfirmation());

function sendProfileImage(){
    var pfp = document.getElementById("pfp");
    var formData = new FormData();

    if (isValidInput(pfp)){
        formData.append("profilePicture", pfp.files[0]);
        fetch("/setProfilePicture", {
            method:"POST",
            body: formData,
        }).then(response => {
            if (!response.ok) {
                alert("Error subiendo la foto de perfil");
            } else {
                window.href = "/profile";
            }

        }).catch(error => {
            // Handle errors
            alert("Error: " + error.message);
        });
    }
}
function isValidInput(pfp){
    if (pfp.files.length > 0) {
        var fileName = pfp.files[0].name;
        var validExtensions = ["jpg", "jpeg", "png"];

        var extension = getFileExtension(fileName).toLowerCase();

        if (validExtensions.includes(extension)) {
            return true;
        } else {
            alert("Invalid file type. Please upload a valid image file.");
            return false;
        }
    }
    alert("No file selected.");
    return false;
}

function getFileExtension(filename) {
    var index = filename.lastIndexOf(".")+1;
    return filename.slice(index);
}


