document.getElementById('event-start').addEventListener('change', function() {
    var startDate = new Date(this.value);
    var today = new Date();
    today.setHours(0, 0, 0, 0);

    if (startDate < today) {
        alert('La fecha de inicio debe ser después de hoy.');
        this.value = '';
    }
});

document.getElementById('event-end').addEventListener('change', function() {
    var endDate = new Date(this.value);
    var startDate = new Date(document.getElementById('event-start').value);

    if (endDate <= startDate) {
        alert('La fecha de fin debe ser después de la fecha de inicio.');
        this.value = '';
    }
});

document.getElementById('event-coordinates').addEventListener('input', function() {
    var pattern = new RegExp(/^\-?\d+(\.\d+)?,\s*\-?\d+(\.\d+)?$/);
    if (!pattern.test(this.value)) {
        this.classList.add('is-invalid');
    } else {
        this.classList.remove('is-invalid');
    }
});