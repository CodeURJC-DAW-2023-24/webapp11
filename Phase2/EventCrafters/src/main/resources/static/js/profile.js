const categoriesContainer = document.getElementById('categories-container'),
    chartsContainer = document.getElementById('charts-container'),
    createdEventsContainer = document.getElementById('created-events-container'),
    pastCreatedEventsContainer = document.getElementById('past-created-events-container'),
    registeredEventsContainer = document.getElementById('registered-events-container'),
    registeredPastEventsContainer = document.getElementById('past-registered-events-container'),
    moreTagsBtnDiv = document.getElementById('more-tags-btn-div'),
    moreCreatedEventsBtnDiv = document.getElementById('more-events-btn-div-1'),
    morePastCreatedEventsBtnDiv = document.getElementById('more-events-btn-div-2'),
    moreRegisteredEventsBtnDiv = document.getElementById('more-events-btn-div-3'),
    moreRegisteredPastEventsBtnDiv = document.getElementById('more-events-btn-div-4'),
    spinnerPT = document.getElementById('spinner-profile-tags'),
    spinnerC = document.getElementById('spinner-charts'),
    spinnerPCE = document.getElementById('spinner-profile-created-events'),
    spinnerPPCE = document.getElementById('spinner-profile-past-created-events'),
    spinnerPRE = document.getElementById('spinner-profile-registered-events'),
    spinnerPRPE = document.getElementById('spinner-profile-registered-past-events'),
    loadMoreTags = document.getElementById('load-more-tags'),
    loadMoreCreatedEventsBtn = document.getElementById('load-more-created-events'),
    loadMorePastCreatedEventsBtn = document.getElementById('load-more-past-created-events'),
    loadMoreRegisteredEventsBtn = document.getElementById('load-more-registered-events'),
    loadMoreRegisteredPastEventsBtn = document.getElementById('load-more-registered-past-events');


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
                    if (btn.classList.contains("d-block")){
                        btn.classList.remove("d-block");
                    }
                    btn.style.display = "none";
                }
            })
        })
    })
}

// this is so that it check when child nodes are added
if (categoriesContainer != null) {
    let tagsObserver = repeatableObserver(moreTagsBtnDiv);
    tagsObserver.observe(categoriesContainer, {childList: true});
}

let createdEventsObserver = repeatableObserver(moreCreatedEventsBtnDiv);
createdEventsObserver.observe(createdEventsContainer, {childList: true});

let pastCreatedEventsObserver = repeatableObserver(morePastCreatedEventsBtnDiv);
pastCreatedEventsObserver.observe(pastCreatedEventsContainer, {childList: true});

let registeredEventsObserver = repeatableObserver(moreRegisteredEventsBtnDiv);
registeredEventsObserver.observe(registeredEventsContainer, {childList: true});

let registeredPastEventsObserver = repeatableObserver(moreRegisteredPastEventsBtnDiv);
registeredPastEventsObserver.observe(registeredPastEventsContainer, {childList: true});



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

loadMoreTags.addEventListener('click', () => {
    let page = loadMoreTags.getAttribute("data-nextPage");
    let nextPage = parseInt(page,10) + 1;
    Ajax(spinnerPT,categoriesContainer, `/categories?page=${page}`)
    loadMoreTags.setAttribute("data-nextPage", nextPage);
})

loadMoreCreatedEventsBtn.addEventListener('click', () => {
    let i = loadMoreCreatedEventsBtn.getAttribute("data-i")
    let page = loadMoreCreatedEventsBtn.getAttribute("data-nextPage");
    let nextPage = parseInt(page,10) + 1;
    Ajax(spinnerPCE, createdEventsContainer, `/moreEventsProfile/${i}?page=${page}`)
    loadMoreCreatedEventsBtn.setAttribute("data-nextPage", nextPage);
})

loadMorePastCreatedEventsBtn.addEventListener('click', () => {
    let i = loadMorePastCreatedEventsBtn.getAttribute("data-i")
    let page = loadMorePastCreatedEventsBtn.getAttribute("data-nextPage");
    let nextPage = parseInt(page,10) + 1;
    Ajax(spinnerPPCE, pastCreatedEventsContainer, `/moreEventsProfile/${i}?page=${page}`)
    loadMorePastCreatedEventsBtn.setAttribute("data-nextPage", nextPage);
})

loadMoreRegisteredEventsBtn.addEventListener('click', () => {
    let i = loadMoreRegisteredEventsBtn.getAttribute("data-i")
    let page = loadMoreRegisteredEventsBtn.getAttribute("data-nextPage");
    let nextPage = parseInt(page,10) + 1;
    Ajax(spinnerPRE, registeredEventsContainer, `moreEventsProfile/${i}?page=${page}`)
    loadMoreRegisteredEventsBtn.setAttribute("data-nextPage", nextPage);

})

loadMoreRegisteredPastEventsBtn.addEventListener('click', () => {
    let i = loadMoreRegisteredPastEventsBtn.getAttribute("data-i")
    let page = loadMoreRegisteredPastEventsBtn.getAttribute("data-nextPage");
    let nextPage = parseInt(page,10) + 1;
    Ajax(spinnerPRPE, registeredPastEventsContainer, `moreEventsProfile/${i}?page=${page}`)
    loadMoreRegisteredPastEventsBtn.setAttribute("data-nextPage", nextPage);
})

document.addEventListener('DOMContentLoaded', () => {
    Ajax(spinnerC,chartsContainer,"/chart-page")
});

function showReloadWarning() {
    alert("Por favor, recarga la página para ver tu nueva foto de perfil.");
}




function warnBan() {
    var userName = document.getElementById("usernameBan").value;
    if (userName==="admin") {
        alert("No se puede banear al administrador del sistema.")
        return false;
    }
    fetch("/IsUsernameTaken?username="+userName)
        .then(response => {
            if (!response.ok) {
                alert("Ha baneado al usuario.");
                return true;
            } else {
                alert("El usuario no existe.");
                return false;
            }
        })
}
function warnUnBan() {
    var userName = document.getElementById("usernameUnBan").value;

    fetch("/IsUserBanned?username="+userName)
        .then(response => response.json())
        .then(isBanned => {
            if (isBanned) {
                alert("Usuario desbaneado")
                return true;
            } else {
                alert("El usuario no estaba baneado");
                return false;
            }
        })
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

const unbanUserBtn = document.getElementById('unban-user-btn');
const unbanUserPopup = document.getElementById('unban-user-popup');

unbanUserBtn.addEventListener('click', () => {
    if (unbanUserPopup.style.display === 'block') {
        unbanUserPopup.style.display = 'none';
    } else {
        unbanUserPopup.style.display = 'block';
    }
});


function warnDelete(){
    var result = confirm("Si continua, se eliminará la cuenta. ¿Está seguro?");
    if (result) {
        alert("La cuenta ha sido eliminada");
        return true;
    } else {
        alert("La cuenta no ha sido eliminada");
        return false;
    }
}




