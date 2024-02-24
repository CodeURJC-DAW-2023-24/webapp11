const deleteAccountBtn = document.getElementById('delete-account-btn'),
    categoriesContainer = document.getElementById('categories-container'),
    chartsContainer = document.getElementById('charts-container'),
    createdEventsContainer = document.getElementById('created-events-container'),
    moreTagsBtnDiv = document.getElementById('more-tags-btn-div'),
    moreCreatedEventsBtnDiv = document.getElementById('more-created-events-btn-div'),
    spinnerPT = document.getElementById('spinner-profile-tags'),
    spinnerC = document.getElementById('spinner-charts'),
    spinnerPCE = document.getElementById('spinner-profile-created-events');



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
    showReloadWarning();
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

let createdEventsObserver = repeatableObserver(moreCreatedEventsBtnDiv);
createdEventsObserver.observe(createdEventsContainer, {childList: true});



// AJAX (fetch)
const Ajax = (spinner, container, url) =>{
    spinner.style.display = "block"
    fetch(`${url}`)
        .then(response => response.text())
        .then(html => {
            spinner.style.display = "none"
            container.insertAdjacentHTML('beforeend', html);
        });
}

document.getElementById('load-more-tags').addEventListener('click', () => {
    Ajax(spinnerPT,categoriesContainer, "/categories")
})

document.getElementById('load-more-created-events').addEventListener('click', () => {
    Ajax(spinnerPCE, createdEventsContainer, "/moreEventsProfile")
})

document.addEventListener('DOMContentLoaded', () => {
    Ajax(spinnerC,chartsContainer,"/chart-page")
});

function showReloadWarning() {
    alert("Por favor, recarga la página para ver tu nueva foto de perfil.");
}




function warnBan() {
    alert("Procede a banear usuario.");
}

// Obtener referencia al botón de banear
const banUserBtn = document.getElementById('ban-user-btn');

// Obtener referencia al formulario de banear
const banUserPopup = document.getElementById('ban-user-popup');


banUserBtn.addEventListener('click', () => {
    if (banUserPopup.style.display === 'block') {
        banUserPopup.style.display = 'none';
    } else {
        banUserPopup.style.display = 'block';
    }
});





