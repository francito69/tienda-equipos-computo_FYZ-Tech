// frontend/src/services/api/authService.js
import api from './config';

export const authService = {
  login: (credentials) => api.post('/auth/login', credentials),
  register: (userData) => api.post('/auth/registro', userData),
  logout: () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
  }
};