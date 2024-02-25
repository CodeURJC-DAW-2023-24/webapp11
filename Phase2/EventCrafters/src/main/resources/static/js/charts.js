const newChart = () => {
    const chart = document.getElementById('myChart')
    const labels = JSON.parse(chart.getAttribute('data-labels'));
    const data = JSON.parse(chart.getAttribute('data-data'));

    const ctx = document.getElementById('myChart').getContext('2d');
    const myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Eventos en los que se usan las categorias',
                backgroundColor: 'rgb(255, 99, 132)',
                borderColor: 'rgb(255, 99, 132)',
                data: data,
            }]
        }
    });
};

const chartObserver = () => {
    return new MutationObserver( m => {
        m.forEach(m => {
            m.addedNodes.forEach(n => {
                if (n.nodeType === Node.ELEMENT_NODE && n.classList.contains('myChart')){
                    newChart();
                }
            })
        })
    })
}

// this is so that it check when child nodes are added
let cObserver = chartObserver();
cObserver.observe(document.getElementById('charts-container'), {childList: true});