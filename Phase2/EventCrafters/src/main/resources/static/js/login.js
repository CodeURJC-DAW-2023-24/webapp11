function recoverPassword(event) {
    event.preventDefault(); // Prevent the default link behavior

    // Prompt the user for input
    var userInput = window.prompt('Please enter your username');
    if (userInput===null){
        return;
    }

    // Check if the user exists
    userExists(userInput).then(exists => {
        if (exists) {
            window.location.href = "/recoverPassword/" + userInput;
        } else {
            // Invalid input
            alert('Invalid input. Please try again.');
        }
    })
}

function userExists(input){
    return fetch("/IsUsernameTaken", {
        method : "POST",
        body : input,
    })
        .then(response => {
            if (!response.ok) {
                //username exists
                return true;
            } else {
                //username does not exist
                return false;
            }
        })
}

function checkUserBanned(){
    var username = document.getElementById("username").value;
    if (username===""){
        document.getElementById("submit").onclick = null;
        return;
    }
    fetch("/IsUserBanned", {
        method : "POST",
        body : username,
    })
        .then(response => response.json())
        .then(isBanned => {
            if (isBanned) {
                alert("Este usuario está baneado")
                document.getElementById("submit").onclick = function() {return false};
            } else {
                document.getElementById("submit").onclick = null;
            }
        })
}


//editing profile



