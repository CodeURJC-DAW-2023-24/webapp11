
async function loadMore() {

    const response = await fetch(`/newEvents`);
    const newEvent = await response.text(); //Pick new events template structure
    const eventContainerStructure = document.getElementById("eventsChart");  //Events structure from index.mustache
    eventContainerStructure.innerHTML += newEvent; //Add the new events after last one
}

function mensajeBorrado() {
    cambiarVisibilidad(document.getElementById("noElementos"));
}