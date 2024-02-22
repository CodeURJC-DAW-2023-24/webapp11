function validateForm(){
    var password = document.getElementById("password").value;
    var confirmPassword = document.getElementById("confirmPassword").value;

    if (password !== confirmPassword) {
        alert("Passwords do not match");
        return false;
    }
    return true;
}

function userNameTaken(){

    var userName = document.getElementById("username").value;

    fetch("/IsUsernameTaken",{
        method : "POST",
        body : userName,
    })
        .then(response => {
            var submitButton = document.getElementById("submit");
            if (!response.ok) {
                alert("UserName is taken. Try a different one");
                console.log("response not ok");
                submitButton.onclick = warnNameTaken;
            } else {
                console.log("response ok");
                submitButton.onclick = validateForm;
            }
        })

}

function warnNameTaken() {
    alert("Can't create an account with that nickname. Try a different one.");
    return false;
}