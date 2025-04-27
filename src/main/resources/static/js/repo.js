const reposContainer = document.getElementById('repos');
const issuesContainer = document.getElementById('issues');
const repoNameInput = document.getElementById('repoNameInput');

const user = JSON.parse(localStorage.getItem('user'));

// Fetch and display repositories
async function fetchRepos() {
    try {
        const response = await fetch('/api/github/repos', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${user.token}`
            }
        });
        const repos = await response.json();
        console.log(repos)

        repos.forEach(repo => {
            const repoCard = document.createElement('div');
            repoCard.className = 'bg-white p-6 rounded shadow hover:shadow-lg transition';

            repoCard.innerHTML = `
        <div class="flex items-center mb-4">
          <img src="${repo.owner.avatar_url}" alt="${repo.owner.login}" class="w-10 h-10 rounded-full mr-3">
          <div>
            <h3 class="text-lg font-semibold">${repo.name}</h3>
            <a href="${repo.html_url}" target="_blank" class="text-blue-500 text-sm">${repo.full_name}</a>
          </div>
        </div>
        <p class="text-gray-700 text-sm mb-3">${repo.description || 'No description provided.'}</p>
        <div class="flex flex-wrap text-xs gap-4 text-gray-500">
          <span>⭐ ${repo.stargazers_count}</span>
          <span>🍴 ${repo.forks_count}</span>
          <span>🛠 ${repo.language || 'N/A'}</span>
          <span>🐞 ${repo.open_issues_count} open issues</span>
        </div>
      `;

            reposContainer.appendChild(repoCard);
        });
    } catch (error) {
        console.error('Error fetching repos:', error);
        reposContainer.innerHTML = '<p class="text-red-500">Failed to load repositories.</p>';
    }
}

// Fetch and display issues of a specific repo
async function fetchIssues() {
    const repoName = repoNameInput.value.trim();
    if (!repoName) {
        alert('Please enter a repository name.');
        return;
    }

    issuesContainer.innerHTML = '<p>Loading issues...</p>';

    try {
        const response = await fetch(`/api/github/repos/${user.githubUsername}/${repoName}/issues`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${user.token}`
            }
        });
        const issues = await response.json();
        console.log(issues)

        issuesContainer.innerHTML = '';

        if (issues.length === 0) {
            issuesContainer.innerHTML = '<p class="text-gray-500">No issues found for this repository.</p>';
            return;
        }

        issues.forEach(issue => {
            const issueCard = document.createElement('div');
            issueCard.className = 'bg-white p-6 rounded shadow hover:shadow-lg transition relative';

            issueCard.innerHTML = `
        <div class="flex items-center mb-4">
          <img src="${issue.user.avatar_url}" alt="${issue.user.login}" class="w-8 h-8 rounded-full mr-3">
          <div>
            <h4 class="text-md font-semibold">${issue.title}</h4>
            <a href="${issue.html_url}" target="_blank" class="text-blue-500 text-xs">${issue.user.login}</a>
          </div>
        </div>
        <p class="text-gray-700 text-sm mb-3">State: <strong>${issue.state}</strong></p>
        <button 
          onclick="copyId(${issue.id})" 
          class="absolute top-4 right-4 bg-gray-200 hover:bg-gray-300 text-xs px-2 py-1 rounded"
        >
          Copy ID
        </button>
      `;

            issuesContainer.appendChild(issueCard);
        });
    } catch (error) {
        console.error('Error fetching issues:', error);
        issuesContainer.innerHTML = '<p class="text-red-500">Failed to load issues.</p>';
    }
}

// Copy issue ID to clipboard
function copyId(id) {
    navigator.clipboard.writeText(id.toString())
        .then(() => {
            alert(`Issue ID ${id} copied to clipboard!`);
        })
        .catch(err => {
            console.error('Failed to copy ID:', err);
        });
}

// Initial load
fetchRepos();
