
const homeLoadMoreBtn = document.getElementById('home-load-more-btn');
async function loadMore(e) {
    let url = e.target.getAttribute('data-type')
    console.log(url);
    const response = await fetch(`${url}`);
    const newEvent = await response.text(); //Pick new events template structure
    const eventContainerStructure = document.getElementById("eventsChart");  //Events structure from index.mustache
    eventContainerStructure.innerHTML += newEvent; //Add the new events after last one
}

homeLoadMoreBtn.addEventListener('click', (e) => loadMore(e));
