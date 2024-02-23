
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

searchBarBtn.addEventListener('click', (e) => {
    e.preventDefault();
    let input = document.getElementById('search-bar-input').value;
    if (input){
        let newURL = `/navbarSearch?input=${input}`
        doFetch(newURL)
    }
})

const doFetch = (url) => {
    fetch(url)
        .then(r => r.text())
        .then(html => document.getElementById("eventsChart").innerHTML = html)
}