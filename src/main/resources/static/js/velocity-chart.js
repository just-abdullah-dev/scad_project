class VelocityChart {
    constructor() {
      const user = JSON.parse(localStorage.getItem('user'));
      this.token = user.token;
      this.chart = null;
      
      if (document.getElementById('velocityChart')) {
        this.init();
      }
    }
    
    async init() {
      try {
        const response = await fetch('/api/sprints', {
          headers: {
            'Authorization': `Bearer ${this.token}`
          }
        });
        
        const sprints = await response.json();
        this.renderChart(sprints);
      } catch (error) {
        console.error('Error loading sprint data:', error);
      }
    }
    
    renderChart(sprints) {
      const ctx = document.getElementById('velocityChart').getContext('2d');
      
      // Filter and sort sprints
      const completedSprints = sprints
        .filter(s => s.completedPoints)
        .sort((a, b) => new Date(a.endDate) - new Date(b.endDate));
      
      const labels = completedSprints.map(s => s.name);
      const velocities = completedSprints.map(s => s.completedPoints);
      const capacities = completedSprints.map(s => s.plannedCapacity);
      
      if (this.chart) {
        this.chart.destroy();
      }
      
      this.chart = new Chart(ctx, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [
            {
              label: 'Velocity (Completed Points)',
              data: velocities,
              borderColor: '#4361ee',
              backgroundColor: 'rgba(67, 97, 238, 0.1)',
              tension: 0.3,
              fill: true
            },
            {
              label: 'Planned Capacity',
              data: capacities,
              borderColor: '#f72585',
              backgroundColor: 'rgba(247, 37, 133, 0.1)',
              borderDash: [5, 5],
              tension: 0.3
            }
          ]
        },
        options: {
          responsive: true,
          plugins: {
            title: {
              display: true,
              text: 'Sprint Velocity Trend',
              font: {
                size: 16
              }
            },
            legend: {
              position: 'top'
            }
          },
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
    }
  }
  
  // Initialize when DOM is loaded
  document.addEventListener('DOMContentLoaded', () => {
    if (document.getElementById('velocityChart')) {
      new VelocityChart();
    }
  });