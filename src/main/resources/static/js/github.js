class GitHubIntegration {
    constructor() {
      this.token = localStorage.getItem('token');
      this.repoSelect = document.getElementById('repoSelect');
      this.repoDataContainer = document.getElementById('repoData');
      
      if (this.repoSelect) {
        this.init();
      }
    }
    
    async init() {
      try {
        const response = await fetch('/api/github/repos', {
          headers: {
            'Authorization': `Bearer ${this.token}`
          }
        });
        
        const repos = await response.json();
        this.populateRepoSelect(repos);
        
        this.repoSelect.addEventListener('change', (e) => {
          this.loadRepoData(e.target.value);
        });
        
        // Load first repo by default
        if (repos.length > 0) {
          this.loadRepoData(repos[0].full_name);
        }
      } catch (error) {
        console.error('Error loading GitHub repos:', error);
      }
    }
    
    populateRepoSelect(repos) {
      this.repoSelect.innerHTML = '';
      
      repos.forEach(repo => {
        const option = document.createElement('option');
        option.value = repo.full_name;
        option.textContent = repo.name;
        this.repoSelect.appendChild(option);
      });
    }
    
    async loadRepoData(repoFullName) {
      try {
        const [owner, repo] = repoFullName.split('/');
        
        const [issuesRes, prsRes, commitsRes] = await Promise.all([
          fetch(`/api/github/repos/${owner}/${repo}/issues`, {
            headers: {
              'Authorization': `Bearer ${this.token}`
            }
          }),
          fetch(`/api/github/repos/${owner}/${repo}/pulls`, {
            headers: {
              'Authorization': `Bearer ${this.token}`
            }
          }),
          fetch(`/api/github/repos/${owner}/${repo}/commits`, {
            headers: {
              'Authorization': `Bearer ${this.token}`
            }
          })
        ]);
        
        const [issues, prs, commits] = await Promise.all([
          issuesRes.json(),
          prsRes.json(),
          commitsRes.json()
        ]);
        
        this.displayRepoData(issues, prs, commits);
      } catch (error) {
        console.error('Error loading repo data:', error);
      }
    }
    
    displayRepoData(issues, prs, commits) {
      this.repoDataContainer.innerHTML = `
        <div class="row">
          <div class="col-md-4">
            <div class="card">
              <div class="card-header">
                <h5>Issues</h5>
              </div>
              <div class="card-body">
                <h2>${issues.length}</h2>
                <p>Total issues</p>
              </div>
            </div>
          </div>
          <div class="col-md-4">
            <div class="card">
              <div class="card-header">
                <h5>Pull Requests</h5>
              </div>
              <div class="card-body">
                <h2>${prs.length}</h2>
                <p>Total PRs</p>
              </div>
            </div>
          </div>
          <div class="col-md-4">
            <div class="card">
              <div class="card-header">
                <h5>Commits</h5>
              </div>
              <div class="card-body">
                <h2>${commits.length}</h2>
                <p>Total commits</p>
              </div>
            </div>
          </div>
        </div>
      `;
    }
  }
  
  // Initialize when DOM is loaded
  document.addEventListener('DOMContentLoaded', () => {
    if (document.getElementById('repoSelect')) {
      new GitHubIntegration();
    }
  });