
document.addEventListener('DOMContentLoaded', () => {
    let dropdownItems = document.querySelectorAll('.dropdown-filter');

    dropdownItems.forEach( (i) => {
        i.addEventListener('click', (e) => {
            e.preventDefault();
            let id = e.target.getAttribute('value');
            if (id) {
                let newURL = `/search?categoryId=${id}`
                //history.pushState({path: newURL}, '', newURL)
                doFetch(newURL)
            }

        })
    })
})

const searchBarBtn = document.getElementById('search-bar-btn');
const homeLoadMoreBtnDiv = document.getElementById('home-load-more-btn-div');
const eventChart = document.getElementById('eventsChart');
const spinner = document.getElementById('spinner');

searchBarBtn.addEventListener('click', (e) => {
    e.preventDefault();
    let input = document.getElementById('search-bar-input').value;
    if (input){
        let newURL = `/navbarSearch?input=${input}`
        doFetch(newURL)
    }
})

const doFetch = (url) => {
    homeLoadMoreBtnDiv.style.display = "block";
    spinner.style.display = "block";
    fetch(url)
        .then(r => r.text())
        .then(html => document.getElementById("eventsChart").innerHTML = html)
    spinner.style.display = "none";
}

// it checks each time we add events to eventChart
// if we had added an element with the no-events-message
// in which case the loadmore button is hidden
const observer = new MutationObserver( m => {
    m.forEach(m => {
        m.addedNodes.forEach(n => {
            if (n.nodeType === Node.ELEMENT_NODE && n.classList.contains('no-events-message')){
                homeLoadMoreBtnDiv.style.display = "none";
            }
        })
    })
})

// this is so that it check when child nodes are added
observer.observe(eventChart, {childList: true});