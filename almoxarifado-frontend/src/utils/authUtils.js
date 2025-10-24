export function isAdmin() {
  return localStorage.getItem('userRole') === 'ROLE_ADMIN';
}

export function isAuthenticated() {
  return !!localStorage.getItem('authToken');
}