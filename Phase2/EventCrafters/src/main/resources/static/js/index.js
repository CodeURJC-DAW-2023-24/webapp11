

document.addEventListener('DOMContentLoaded', () => {
    let dropdownItems = document.querySelectorAll('.dropdown-filter');

    dropdownItems.forEach( (i) => {
        i.addEventListener('click', (e) => {
            e.preventDefault();
            let id = e.target.getAttribute('value');
            if (id) {
                let newURL = `/search?id=${id}`
                //history.pushState({path: newURL}, '', newURL)
                fetch(newURL)
                    .then(r => r.text())
                    .then(html => document.getElementById("eventsChart").innerHTML = html)
            }
        })
    })
})