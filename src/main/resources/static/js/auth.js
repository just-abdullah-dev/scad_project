document.addEventListener('DOMContentLoaded', () => {
    const user = JSON.parse(localStorage.getItem('user')) || null;
    const currentPath = window.location.pathname.split('/').pop(); // example: 'index.html'

    initAuthForms();
    initNavbarUser();
    initPageAccessControl(user, currentPath);
    initGithubLogin();
    initLogout();

    // ===== Functions =====

    function initAuthForms() {
        const loginForm = document.getElementById('loginForm');
        const registerForm = document.getElementById('registerForm');

        if (loginForm) {
            loginForm.addEventListener('submit', async (e) => {
                e.preventDefault();
                const email = document.getElementById('loginEmail').value.trim();
                const password = document.getElementById('loginPassword').value.trim();

                try {
                    const res = await fetch('/api/auth/signin', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ email, password })
                    });

                    const data = await res.json();

                    if (res.ok) {
                        localStorage.setItem('user', JSON.stringify(data));
                        window.location.href = 'dashboard.html';
                    } else {
                        showAlert(data.message || 'Login failed', 'error');
                    }
                } catch (error) {
                    showAlert('An error occurred. Please try again.', 'error');
                }
            });
        }

        if (registerForm) {
            registerForm.addEventListener('submit', async (e) => {
                e.preventDefault();
                const name = document.getElementById('registerName').value.trim();
                const githubUsername = document.getElementById('githubUsername').value.trim();
                const email = document.getElementById('registerEmail').value.trim();
                const password = document.getElementById('registerPassword').value.trim();

                try {
                    const res = await fetch('/api/auth/signup', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ name, email, password, githubUsername })
                    });

                    const data = await res.json();

                    if (res.ok) {
                        showAlert(data.message, 'success');
                        setTimeout(() => {
                            window.location.href = 'login.html';
                        }, 1500);
                    } else {
                        showAlert(data.message || 'Registration failed', 'error');
                    }
                } catch (error) {
                    showAlert('An error occurred. Please try again.', 'error');
                }
            });
        }
    }

    function initNavbarUser() {
        const userNameSpan = document.getElementById('userNameSpan');
        if (userNameSpan && user && user.username) {
            userNameSpan.textContent = user.username;
        }

        const navLinks = document.getElementById('nav-links');
        if (navLinks) {
            if (user) {
                // If user exists, replace login/register with dashboard link
                navLinks.innerHTML = `<a href="dashboard.html">Dashboard</a>`;
            }
        }
    }

    function initPageAccessControl(user, currentPath) {
        const authPages = ['login.html', 'register.html'];
        const allowedUserPages = ['index.html', 'repo.html', 'dashboard.html', 'sprints.html', 'predictions.html', 'profile.html'];
        if(window.location.pathname.split('/').pop() === "index.html") {

        } else if (user) {
            // If user is logged in and visiting login or register → redirect to dashboard
            if (authPages.includes(currentPath)) {
                window.location.href = 'dashboard.html';
            }
            // If user visits an invalid page, redirect to dashboard
            if (!allowedUserPages.includes(currentPath)) {
                window.location.href = 'dashboard.html';
            }
        } else {
            // If no user and trying to access any page except login/register → redirect to login
            if (!authPages.includes(currentPath)) {
                window.location.href = 'login.html';
            }
        }
    }

    function initGithubLogin() {
        const githubLoginBtn = document.getElementById('githubLogin');
        if (githubLoginBtn) {
            githubLoginBtn.addEventListener('click', () => {
                window.location.href = '/oauth2/authorization/github';
            });
        }
    }

    function initLogout() {
        const logoutBtn = document.getElementById('logout');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', (e) => {
                e.preventDefault();
                localStorage.removeItem('user');
                window.location.href = 'index.html';
            });
        }
    }

    function showAlert(message, type = 'error') {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type}`;
        alertDiv.textContent = message;

        const container = document.querySelector('.auth-card') || document.body;
        container.prepend(alertDiv);

        setTimeout(() => {
            alertDiv.remove();
        }, 3000);
    }
});


// document.addEventListener('DOMContentLoaded', function () {
//     const loginForm = document.getElementById('loginForm');
//     const registerForm = document.getElementById('registerForm');
//
//     if (loginForm) {
//         loginForm.addEventListener('submit', async function (e) {
//             e.preventDefault();
//             const email = document.getElementById('loginEmail').value;
//             const password = document.getElementById('loginPassword').value;
//
//             try {
//                 const response = await fetch('/api/auth/signin', {
//                     method: 'POST',
//                     headers: {
//                         'Content-Type': 'application/json'
//                     },
//                     body: JSON.stringify({
//                         email,
//                         password
//                     })
//                 });
//
//                 const data = await response.json();
//
//                 if (response.ok) {
//                     localStorage.setItem('user', JSON.stringify(data));
//                     window.location.href = 'dashboard.html';
//                 } else {
//                     showAlert(data.message || 'Login failed', 'error');
//                 }
//             } catch (error) {
//                 showAlert('An error occurred. Please try again.', 'error');
//             }
//         });
//     }
//
//     if (registerForm) {
//         registerForm.addEventListener('submit', async function (e) {
//             e.preventDefault();
//             const name = document.getElementById('registerName').value;
//             const githubUsername = document.getElementById('githubUsername').value;
//             const email = document.getElementById('registerEmail').value;
//             const password = document.getElementById('registerPassword').value;
//
//             try {
//                 const response = await fetch('/api/auth/signup', {
//                     method: 'POST',
//                     headers: {
//                         'Content-Type': 'application/json'
//                     },
//                     body: JSON.stringify({
//                         name,
//                         email,
//                         password,
//                         githubUsername
//                     })
//                 });
//
//                 const data = await response.json();
//
//                 if (response.ok) {
//                     showAlert(data.message, 'success');
//                     setTimeout(() => {
//                         window.location.href = 'login.html';
//                     }, 1500);
//                 } else {
//                     showAlert(data.message || 'Registration failed', 'error');
//                 }
//             } catch (error) {
//                 showAlert('An error occurred. Please try again.', 'error');
//             }
//         });
//     }
//
//     // GitHub OAuth login
//     const githubLoginBtn = document.getElementById('githubLogin');
//     if (githubLoginBtn) {
//         githubLoginBtn.addEventListener('click', function () {
//             window.location.href = '/oauth2/authorization/github';
//         });
//     }
//
//     // Logout functionality
//     const logoutBtn = document.getElementById('logout');
//     if (logoutBtn) {
//         logoutBtn.addEventListener('click', function () {
//             localStorage.removeItem('user');
//             window.location.href = 'index.html';
//         });
//     }
//
//     // Check if user is logged in
//     const user = JSON.parse(localStorage.getItem('user'));
//     // username in navbar
//     if (user) {
//         const userNameElement = document.getElementById('userNameSpan');
//         if (userNameElement) {
//             userNameElement.textContent = user.username;
//         }
//     }
//     // index.html header menu
//     const navLinksDiv = document.getElementById('nav-links');
//
//     if (user && navLinksDiv) {
//         // Replace nav-links div with the dropdown
//         navLinksDiv.innerHTML = `  <a href="dashboard.html">Dashboard</a>`;
//
//         const currentPath = window.location.pathname.split('/').pop(); // get filename
//         console.log(currentPath)
//         const allowedPagesForUser = ['profile.html', 'index.html', 'dashboard.html', 'sprints.html', 'predictions.html'];
//         const authPages = ['login.html', 'register.html'];
//
//         if (user) {
//             // If user exists
//             if (authPages.includes(currentPath)) {
//                 // If user tries to go to login or register, redirect to dashboard.html
//                 window.location.href = 'dashboard.html';
//             } else if (!allowedPagesForUser.includes(currentPath)) {
//                 // If user tries to go to any unauthorized page, redirect to dashboard.html
//                 window.location.href = 'dashboard.html';
//             }
//         } else {
//             // If user does not exist
//             if (!authPages.includes(currentPath)) {
//                 // If not on login or register, force redirect to login.html
//                 window.location.href = 'login.html';
//             }
//         }
//
//     }
//
//
// });
//
// function showAlert(message, type) {
//     const alertDiv = document.createElement('div');
//     alertDiv.className = `alert alert-${type}`;
//     alertDiv.textContent = message;
//
//     const container = document.querySelector('.auth-card') || document.body;
//     container.prepend(alertDiv);
//
//     setTimeout(() => {
//         alertDiv.remove();
//     }, 3000);
// }