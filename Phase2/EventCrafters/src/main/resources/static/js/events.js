
async function loadMore(index, refreshSize) {

    const from = index;
    const to = index + (refreshSize-1);


    const response = await fetch(`/newEvents`); //se "recogen" los nuevos eventos
    const newEvent = await response.text();
    console.log(newEvent)
    const eventContainerStructure = document.getElementById("eventsChart");  //se recoge del index su estructura
    console.log(eventContainerStructure)
    eventContainerStructure.innerHTML += newEvent; //los platos nuevos se meten ahí
}

function mensajeBorrado() {
    cambiarVisibilidad(document.getElementById("noElementos"));
}