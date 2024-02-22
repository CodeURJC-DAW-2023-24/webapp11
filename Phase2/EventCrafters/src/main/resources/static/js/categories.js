const windowBackground = document.getElementById('window-background'),
    windowContainer = document.getElementById('window-container'),
    closeButton = document.getElementById('close-button'),
    tagColorin = document.getElementById('tag-color-in'),
    tagNamein = document.getElementById('tag-name-in'),
    tagPopup = document.getElementById('tag-pop-up')
    deleteTagBtn = document.getElementById("delete-tag-button");

let currentpage = 0;

// to trasition to rgb from hex
function rgbToHex(r,g,b){
    return "#" + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);
}

const newButton = (e) => {
    windowBackground.style.display = 'flex'
    let parent = e.target.parentElement;
    if (parent.getAttribute('name') == 'tag'){
        let id = parent.getAttribute('id')
        let nameholder = parent.getElementsByTagName('span')
        tagPopup.action = `/editCategory/${id}` // so that if you click in the save button the changes are saved
        deleteTagBtn.href = `/deleteCategory/${id}` // so that if you click in the trash can icon the category is deleted
        deleteTagBtn.style.display = 'flex' // it needs to be flex so that the trash can symbol id displayed in the center
        tagNamein.value = nameholder[0].textContent
        let color = window.getComputedStyle(nameholder[0]).getPropertyValue('background-color')
        let rgb = color.split('(')[1].split(')')[0].split(',').map(function(num) {
            return parseInt(num.trim());// from String to integer
        });
        tagColorin.value = rgbToHex(rgb[0],rgb[1],rgb[2]);
    } else {
        deleteTagBtn.style.display = 'none'
    }
}

// makes it so that all elements with id=open-button do the same function when clicked
document.getElementById('categories-container').addEventListener('click', (e) => {
    if (e.target && e.target.matches('#open-button')) {
        newButton(e)
    }
})

//to close the tag popup
const closeWindow = () => {
    windowContainer.classList.add('close-window')// closing animation

    // so that it doesn´t set the values to null until the transition has finished
    //setTimeout(() => {}, 500)

    setTimeout(() => {

        windowContainer.classList.remove('close-window')
        windowBackground.style.display = 'none'
        tagColorin.value = null;
        tagNamein.value = null;
    }, 450);
}

deleteTagBtn.addEventListener("click", (e) => {
    e.preventDefault();
    // Show the confirmation dialog
    let confirmation = confirm("Are you sure you want to delete the category");

    // If the user clicks "OK", proceed with the category removal from the database
    if (confirmation) {
        window.location.href = deleteTagBtn.href;
    }
})

// if you click the x simbol the popup closes
closeButton.addEventListener('click', () => closeWindow())

// if you click outside the popup it closes
window.addEventListener('click', e => e.target === windowBackground && closeWindow())

// AJAX (fetch)
document.getElementById('load-more').addEventListener('click', function() {
    currentpage++;
    loadCategories(currentpage);
});

function loadCategories(page) {
    fetch(`/categories?page=${page}`)
        .then(response => response.text())
        .then(html => {
            document.getElementById('categories-container').insertAdjacentHTML('beforeend', html);
        });

}