let currentpage = 0;

document.getElementById('load-more').addEventListener('click', function() {
    currentpage++;
    cargarProductos(currentpage);
});

function cargarProductos(page) {
    fetch(`/categories?page=${page}`)
        .then(response => response.text())
        .then(html => {
            document.getElementById('categories-container').insertAdjacentHTML('beforeend', html);
        });
}