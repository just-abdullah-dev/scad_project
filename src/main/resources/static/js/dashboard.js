document.addEventListener('DOMContentLoaded', async function() {
    // Check authentication
    const user = JSON.parse(localStorage.getItem('user'));
    if (!user.token) {
      window.location.href = 'login.html';
      return;
    }
    
    try {
      // Fetch user data
      const userResponse = await fetch('/api/auth/user', {
        headers: {
          'Authorization': `Bearer ${user.token}`
        }
      });
      
      if (!userResponse.ok) {
        throw new Error('Authentication failed');
      }
      
      const user = await userResponse.json();
      document.getElementById('userAvatar').src = user.imageUrl || 'images/default-avatar.png';
      document.getElementById('userName').textContent = user.name || user.email;
      
      // Fetch sprint data
      const sprintsResponse = await fetch('/api/sprints', {
        headers: {
          'Authorization': `Bearer ${user.token}`
        }
      });
      
      const sprints = await sprintsResponse.json();
      
      // Update metrics
      updateMetrics(sprints);
      
      // Initialize charts
      // initVelocityChart(sprints);
      
      // Recent sprints
      renderRecentSprints(sprints.slice(0, 5));
      
    } catch (error) {
      console.error('Error:', error);
      showAlert('Failed to load dashboard data', 'error');
    }
    
    // Event listeners
    document.getElementById('syncGithub').addEventListener('click', syncWithGithub);
  });
  
  async function syncWithGithub() {
    try {
      const user = JSON.parse(localStorage.getItem('user'));
      const response = await fetch('/api/github/sync', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${user.token}`
        }
      });
      
      if (response.ok) {
        showAlert('GitHub data synced successfully!', 'success');
        // Refresh data
        window.location.reload();
      } else {
        throw new Error('Sync failed');
      }
    } catch (error) {
      showAlert('Failed to sync with GitHub', 'error');
    }
  }
  
  function updateMetrics(sprints) {
    const completedSprints = sprints.filter(s => s.completedPoints);
    const totalPoints = completedSprints.reduce((sum, sprint) => sum + sprint.completedPoints, 0);
    const avgVelocity = completedSprints.length > 0 ? Math.round(totalPoints / completedSprints.length) : 0;
    
    document.getElementById('avgVelocity').textContent = avgVelocity;
    document.getElementById('totalSprints').textContent = sprints.length;
    document.getElementById('completedPoints').textContent = totalPoints;
  }
  
  function renderRecentSprints(sprints) {
    const container = document.getElementById('recentSprints');
    container.innerHTML = '';
    
    sprints.forEach(sprint => {
      const progress = sprint.completedPoints ? 
        Math.min(Math.round((sprint.completedPoints / sprint.plannedCapacity) * 100), 100) : 0;
      
      const sprintEl = document.createElement('div');
      sprintEl.className = 'sprint-item';
      sprintEl.innerHTML = `
        <div>
          <h5>${sprint.name}</h5>
          <small class="text-muted">${new Date(sprint.startDate).toDateString()} - ${new Date(sprint.endDate).toDateString()}</small>
          <div class="sprint-progress">
            <div class="progress-bar" style="width: ${progress}%"></div>
          </div>
        </div>
        <div class="text-end">
          <span class="badge bg-primary">${sprint.completedPoints || 0}/${sprint.plannedCapacity} pts</span>
        </div>
      `;
      
      container.appendChild(sprintEl);
    });
  }
  
  function showAlert(message, type) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} fixed-top m-3`;
    alertDiv.textContent = message;
    
    document.body.appendChild(alertDiv);
    
    setTimeout(() => {
      alertDiv.remove();
    }, 3000);
  }