document.addEventListener('DOMContentLoaded', async function() {
    // Check authentication
    const user = JSON.parse(localStorage.getItem('user'));
    if (!user.token) {
        window.location.href = 'login.html';
        return;
    }

    // Initialize chart
    const ctx = document.getElementById('predictionChart').getContext('2d');
    const chart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: [],
            datasets: [{
                label: 'Planned',
                data: [],
                backgroundColor: '#4361ee'
            }, {
                label: 'Completed',
                data: [],
                backgroundColor: '#4cc9f0'
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Story Points'
                    }
                }
            }
        }
    });

    // Load historical data
    await loadHistoricalData(chart);

    // Prediction form
    document.getElementById('predictionForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        const plannedPoints = parseInt(document.getElementById('plannedPoints').value);
        const teamCapacity = parseInt(document.getElementById('teamCapacity').value);

        try {
            const response = await fetch(`/api/predictions/completion-probability?plannedPoints=${plannedPoints}&teamCapacity=${teamCapacity}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${user.token}`
                },
            });
                // body: JSON.stringify({
                //     plannedPoints,
                //     teamCapacity
                // })

            const data = await response.json();
            console.log(data)

            if (response.ok) {
                displayPredictionResult(data, plannedPoints);
            } else {
                throw new Error(data.message || 'Prediction failed');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Failed to get prediction');
        }
    });
});

async function loadHistoricalData(chart) {
    try {
        const user = JSON.parse(localStorage.getItem('user'));
        const response = await fetch('/api/sprints', {
            headers: {
                'Authorization': `Bearer ${user.token}`
            }
        });

        const sprints = await response.json();
        const completedSprints = sprints.filter(s => s.completedPoints);

        // Update chart
        chart.data.labels = completedSprints.map(s => s.name);
        chart.data.datasets[0].data = completedSprints.map(s => s.plannedCapacity);
        chart.data.datasets[1].data = completedSprints.map(s => s.completedPoints);
        chart.update();

        // Update metrics
        const totalPoints = completedSprints.reduce((sum, sprint) => sum + sprint.completedPoints, 0);
        const avgVelocity = completedSprints.length > 0 ? Math.round(totalPoints / completedSprints.length) : 0;

        document.getElementById('avgVelocity').textContent = avgVelocity;
        document.getElementById('avgCapacity').textContent = completedSprints.length > 0 ?
            Math.round(completedSprints.reduce((sum, sprint) => sum + sprint.plannedCapacity, 0) / completedSprints.length) : 0;
    } catch (error) {
        console.error('Error loading historical data:', error);
    }
}

function displayPredictionResult(data, plannedPoints) {
    const resultDiv = document.getElementById('predictionResult');
    const probability = Math.round(data.probability * 100);

    document.getElementById('completionRate').textContent = `${Math.round(data.completionRate * 100)}%`;
    document.getElementById('probabilityValue').textContent = `${probability}%`;
    document.getElementById('recommendationText').textContent = data.recommendation;

    document.getElementById('completionBar').style.width = `${probability}%`;
    document.getElementById('completionBar').setAttribute('aria-valuenow', probability);

    resultDiv.classList.remove('d-none');
}