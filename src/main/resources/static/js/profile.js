document.addEventListener('DOMContentLoaded', async function() {
    // Check authentication

    const userData = JSON.parse(localStorage.getItem('user'));
    if (!userData.token) {
        window.location.href = 'login.html';
        return;
    }

    // Load user profile
    await loadUserProfile();

    // Connect GitHub button
    document.getElementById('connectGithubBtn').addEventListener('click', function() {
        window.location.href = '/oauth2/authorization/github';
    });

    // Profile form submission
    document.getElementById('profileForm').addEventListener('submit', async function(e) {
        e.preventDefault();

        const profileData = {
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            timezone: document.getElementById('timezone').value
        };

        try {
            const response = await fetch('/api/profile', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${userData.token}`
                },
                body: JSON.stringify(profileData)
            });

            if (response.ok) {
                alert('Profile updated successfully');
                await loadUserProfile();
            } else {
                throw new Error('Profile update failed');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Failed to update profile');
        }
    });
});

async function loadUserProfile() {
    try {
        const userData = JSON.parse(localStorage.getItem('user'));
        const response = await fetch('/api/profile', {
            headers: {
                'Authorization': `Bearer ${userData.token}`
            }
        });

        const user = await response.json();

        // Update profile info
        document.getElementById('profileName').textContent = user.name || 'User';
        document.getElementById('profileEmail').textContent = user.email;
        document.getElementById('userName').textContent = user.name || user.email.split('@')[0];

        // Update form fields
        if (user.name) {
            const names = user.name.split(' ');
            document.getElementById('firstName').value = names[0] || '';
            document.getElementById('lastName').value = names.slice(1).join(' ') || '';
        }
        document.getElementById('email').value = user.email;
        document.getElementById('timezone').value = user.timezone || 'UTC';

        // Update GitHub info
        if (user.githubUsername) {
            document.getElementById('githubUsername').textContent = user.githubUsername;
            document.getElementById('connectGithubBtn').textContent = 'Reconnect GitHub';
        }
    } catch (error) {
        console.error('Error loading user profile:', error);
    }
}