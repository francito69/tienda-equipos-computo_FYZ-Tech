// frontend/src/services/api/productoService.js
import api from './config';

export const productoService = {
  obtenerTodos: () => api.get('/productos'),
  obtenerPorId: (id) => api.get(`/productos/${id}`),
  buscarProductos: (query) => api.get(`/productos/buscar?q=${query}`),
  obtenerPorCategoria: (categoriaId) => api.get(`/productos/categoria/${categoriaId}`),
  obtenerConStock: () => api.get('/productos/con-stock')
};