
document.addEventListener('DOMContentLoaded', () => {
    let dropdownItems = document.querySelectorAll('.dropdown-filter');

    dropdownItems.forEach( (i) => {
        i.addEventListener('click', (e) => {
            e.preventDefault();
            let id = e.target.getAttribute('value');
            if (id) {
                let newURL = `/search?categoryId=${id}`
                history.pushState({path: newURL}, '', newURL)
                fetch(newURL)
                    .then(r => r.text())
                    .then(html => document.getElementById("eventsChart").innerHTML = html)
            }
            // so that when the load more button is used the events loaded are from the categoryFilteredEvents List
            document.getElementById('home-load-more-btn').setAttribute("data-type", "/newFilteredEvents")
        })
    })
})