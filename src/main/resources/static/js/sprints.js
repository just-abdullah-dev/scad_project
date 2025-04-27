document.addEventListener('DOMContentLoaded', async function() {
    // Check authentication
    const user = JSON.parse(localStorage.getItem('user'));
    if (!user.token) {
        window.location.href = 'login.html';
        return;
    }

    // Initialize modal
    const sprintModal = new bootstrap.Modal(document.getElementById('sprintModal'));

    // Load sprints
    await loadSprints();

    // New sprint button
    document.getElementById('newSprintBtn').addEventListener('click', function() {
        document.getElementById('modalTitle').textContent = 'Create New Sprint';
        document.getElementById('sprintForm').reset();
        document.getElementById('sprintId').value = '';
        sprintModal.show();
    });

    // Save sprint
    document.getElementById('saveSprintBtn').addEventListener('click', async function() {
        const sprintData = {
            name: document.getElementById('sprintName').value,
            description: document.getElementById('sprintDescription').value,
            startDate: document.getElementById('startDate').value,
            endDate: document.getElementById('endDate').value,
            plannedCapacity: parseInt(document.getElementById('plannedCapacity').value),
            goal: document.getElementById('sprintGoal').value
        };

        const sprintId = document.getElementById('sprintId').value;
        const method = sprintId ? 'PUT' : 'POST';
        const url = sprintId ? `/api/sprints/${sprintId}` : '/api/sprints';

        try {
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${user.token}`
                },
                body: JSON.stringify(sprintData)
            });

            if (response.ok) {
                sprintModal.hide();
                await loadSprints();
            } else {
                throw new Error('Failed to save sprint');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Failed to save sprint');
        }
    });

    // Search functionality
    document.getElementById('searchSprints').addEventListener('input', function(e) {
        const searchTerm = e.target.value.toLowerCase();
        const rows = document.querySelectorAll('#sprintsTable tr');

        rows.forEach(row => {
            const name = row.cells[0].textContent.toLowerCase();
            row.style.display = name.includes(searchTerm) ? '' : 'none';
        });
    });
});

async function loadSprints() {
    try {
        const user = JSON.parse(localStorage.getItem('user'));
        const response = await fetch('/api/sprints', {
            headers: {
                'Authorization': `Bearer ${user.token}`
            }
        });

        const sprints = await response.json();
        renderSprintsTable(sprints);
    } catch (error) {
        console.error('Error loading sprints:', error);
    }
}

function renderSprintsTable(sprints) {
    const tableBody = document.getElementById('sprintsTable');
    tableBody.innerHTML = '';
    console.log(sprints);
    sprints.forEach(sprint => {
        const row = document.createElement('tr');

        const today = new Date();
        const endDate = new Date(sprint.endDate);
        const startDate = new Date(sprint.startDate);

        let status = 'Planned';
        let statusClass = 'bg-info';

        if (today > endDate) {
            status = 'Completed';
            statusClass = 'bg-success';
        } else if (today >= startDate && today <= endDate) {
            status = 'Active';
            statusClass = 'bg-primary';
        }

        row.innerHTML = `
      <td>${sprint.name}</td>
      <td>${formatDate(sprint.startDate)} - ${formatDate(sprint.endDate)}</td>
      <td><span class="badge ${statusClass}">${status}</span></td>
      <td>
        <div class="progress" style="height: 8px;">
          <div class="progress-bar" style="width: ${sprint.completedPoints ?
            Math.min((sprint.completedPoints / sprint.plannedCapacity) * 100, 100) : 0}%"></div>
        </div>
      </td>
      <td>
        <button class="btn btn-sm btn-outline-primary edit-sprint" data-id="${sprint.id}">Edit</button>
        <button class="btn btn-sm btn-outline-danger delete-sprint" data-id="${sprint.id}">Delete</button>
      </td>
    `;

        tableBody.appendChild(row);
    });

    // Add event listeners to edit buttons
    document.querySelectorAll('.edit-sprint').forEach(btn => {
        btn.addEventListener('click', async function() {
            const sprintId = this.getAttribute('data-id');
            await loadSprintForEdit(sprintId);
        });
    });

    // Add event listeners to delete buttons
    document.querySelectorAll('.delete-sprint').forEach(btn => {
        btn.addEventListener('click', async function() {
            const sprintId = this.getAttribute('data-id');
            if (confirm('Are you sure you want to delete this sprint?')) {
                await deleteSprint(sprintId);
            }
        });
    });
}

async function loadSprintForEdit(sprintId) {
    try {
        const user = JSON.parse(localStorage.getItem('user'));
        const response = await fetch(`/api/sprints/${sprintId}`, {
            headers: {
                'Authorization': `Bearer ${user.token}`
            }
        });

        const sprint = await response.json();

        document.getElementById('modalTitle').textContent = 'Edit Sprint';
        document.getElementById('sprintId').value = sprint.id;
        document.getElementById('sprintName').value = sprint.name;
        document.getElementById('sprintDescription').value = sprint.description;
        document.getElementById('startDate').value = sprint.startDate.split('T')[0];
        document.getElementById('endDate').value = sprint.endDate.split('T')[0];
        document.getElementById('plannedCapacity').value = sprint.plannedCapacity;
        document.getElementById('sprintGoal').value = sprint.goal || '';

        const sprintModal = new bootstrap.Modal(document.getElementById('sprintModal'));
        sprintModal.show();
    } catch (error) {
        console.error('Error loading sprint:', error);
    }
}

async function deleteSprint(sprintId) {
    try {
        const user = JSON.parse(localStorage.getItem('user'));
        const response = await fetch(`/api/sprints/${sprintId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${user.token}`
            }
        });

        if (response.ok) {
            await loadSprints();
        } else {
            throw new Error('Failed to delete sprint');
        }
    } catch (error) {
        console.error('Error deleting sprint:', error);
        alert('Failed to delete sprint');
    }
}

function formatDate(dateString) {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
}