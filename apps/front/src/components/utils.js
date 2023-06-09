export function toQueryString(params) {
    return Object.keys(params)
      .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
      .join('&');
  }

export function isAuthenticated() {
  const username = sessionStorage.getItem('username');
  const password = sessionStorage.getItem('password');

  const credentialsList = [
    { username: 'aaa', password: '123' },
    { username: 'bbb', password: '123' },
    { username: 'ccc', password: '123' },
    { username: 'ddd', password: '123' },
    { username: 'eee', password: '123' },
    { username: 'fff', password: '123' },
  ];

  const match = credentialsList.find(
    cred => cred.username === username && cred.password === password
  );

  if (match) {
    return true;
  }
  return false;
}

export function getBackendUrl() {
  return process.env.VUE_APP_BACKEND_URL;
}
