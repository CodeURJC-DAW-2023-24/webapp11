
const homeLoadMoreBtn = document.getElementById('home-load-more-btn');
//const spinner = document.getElementById('spinner'); // this const is in index.js
async function loadMore(e) {
    spinner.style.display = "block"
    let url = e.target.getAttribute('data-type')
    console.log(url);
    const response = await fetch(`${url}`);
    const newEvent = await response.text(); //Pick new events template structure
    const eventContainerStructure = document.getElementById("eventsChart");  //Events structure from index.mustache
    spinner.style.display = "none"
    eventContainerStructure.innerHTML += newEvent; //Add the new events after last one

}

homeLoadMoreBtn.addEventListener('click', (e) => loadMore(e));
