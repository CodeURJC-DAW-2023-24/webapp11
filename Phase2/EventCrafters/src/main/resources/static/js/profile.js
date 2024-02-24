const deleteAccountBtn = document.getElementById('delete-account-btn'),
    categoriesContainer = document.getElementById('categories-container'),
    moreTagsBtnDiv = document.getElementById('more-tags-btn-div');



const askForConfirmation = () => {
    let result = confirm("¿Estás seguro de que deseas continuar?");
    if (result == true) {
        // El usuario hizo clic en "Aceptar"
        alert("Has aceptado.");
    } else {
        // El usuario hizo clic en "Cancelar"
        alert("Has cancelado la acción.");
    }
}
//deleteAccountBtn.addEventListener('click', () => askForConfirmation());


function sendProfileImage(){
    let pfp = document.getElementById("pfp");
    let formData = new FormData();

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
        let fileName = pfp.files[0].name;
        let validExtensions = ["jpg", "jpeg", "png"];

        let extension = getFileExtension(fileName).toLowerCase();

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
    let index = filename.lastIndexOf(".")+1;
    return filename.slice(index);
}


// it checks each time we add events to eventChart
// if we had added an element with the no-events-message
// in which case the loadmore button is hidden
const repeatableObserver = (btn) => {
     return new MutationObserver( m => {
        m.forEach(m => {
            m.addedNodes.forEach(n => {
                if (n.nodeType === Node.ELEMENT_NODE && n.classList.contains('no-events-message')){
                    btn.style.display = "none";
                }
            })
        })
    })
}

// this is so that it check when child nodes are added
let tagsObserver = repeatableObserver(moreTagsBtnDiv);
tagsObserver.observe(categoriesContainer, {childList: true});


