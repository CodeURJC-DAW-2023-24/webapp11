const windowBackground = document.getElementById('window-background'),
    windowContainer = document.getElementById('window-container'),
    openButtons = document.getElementsByName('open-button'),
    closeButton = document.getElementById('close-button'),
    colorTrial = document.getElementById('color-trial'),
    tagColorin = document.getElementById('tag-color-in'),
    tagNamein = document.getElementById('tag-name-in');


// para pasar de rgb a hex
function rgbToHex(r,g,b){ 
    return "#" + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);
}


// openButtons es un array con todos los elementos con el nombre de open-button
for (var i = 0; i < openButtons.length; i++) {
    openButtons[i].addEventListener('click', (e) => {
        windowBackground.style.display = 'flex'
        let parent = e.target.parentElement;
        if (parent.getAttribute('name') == 'tag'){
            let nameholder = parent.getElementsByTagName('span')
            tagNamein.value = nameholder[0].textContent
            let color = window.getComputedStyle(nameholder[0]).getPropertyValue('background-color')
            let rgb = color.split('(')[1].split(')')[0].split(',').map(function(num) {
                return parseInt(num.trim());// para pasar los string a integer
              });
            tagColorin.value = rgbToHex(rgb[0],rgb[1],rgb[2]);
        } 
    })
}

//para cerrar el popup de tags
const closeWindow = () => {
    windowContainer.classList.add('close') // animación de cerrado

    setTimeout(() => {
        windowContainer.classList.remove('close')
        windowBackground.style.display = 'none'
    }, 1000);
}

// so pulsas en la x se cierra la ventana
closeButton.addEventListener('click', () => closeWindow())

// para qui si pulsas fuera de la ventana se salga de esta
window.addEventListener('click', e => e.target == windowBackground && closeWindow())

tagColorin.addEventListener('input', () => colorTrial.style.background = tagColorin.value)

