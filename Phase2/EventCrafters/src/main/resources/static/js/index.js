
document.addEventListener('DOMContentLoaded', () => {
    let dropdownItems = document.querySelectorAll('.dropdown-filter');

    dropdownItems.forEach( (i) => {
        i.addEventListener('click', (e) => {
            e.preventDefault();
            let i = document.getElementById('home-load-more-btn').getAttribute("data-type")
            document.getElementById('home-load-more-btn').setAttribute("data-nextPage", 1)
            document.getElementById('home-load-more-btn').setAttribute("data-type", 7)
            let id = e.target.getAttribute('value');
            if (id) {
                let newURL = `/search${i}?categoryId=${id}`
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
    let i = document.getElementById('home-load-more-btn').getAttribute("data-type")
    document.getElementById('home-load-more-btn').setAttribute("data-nextPage", 1)
    document.getElementById('home-load-more-btn').setAttribute("data-type", 8)
    let input = document.getElementById('search-bar-input').value;
    if (input){
        let newURL = `/navbarSearch${i}?input=${input}`
        console.log(newURL);
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