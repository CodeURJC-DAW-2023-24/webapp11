function recoverPassword(event) {
    event.preventDefault(); // Prevent the default link behavior

    // Prompt the user for input
    var userInput = window.prompt('Please enter your username');

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
            console.log("Inside the then.")
            if (!response.ok) {
                //username exists
                return true;
            } else {
                //username does not exist
                return false;
            }
        })
}


//editing profile



