// frontend/src/services/api/carritoService.js
import api from './config';

export const carritoService = {
  obtenerCarrito: () => api.get('/carrito'),
  agregarAlCarrito: (productoId, cantidad) => 
    api.post(`/carrito/agregar?productoId=${productoId}&cantidad=${cantidad}`),
  actualizarCantidad: (productoId, cantidad) => 
    api.put(`/carrito/actualizar?productoId=${productoId}&cantidad=${cantidad}`),
  removerDelCarrito: (productoId) => 
    api.delete(`/carrito/remover?productoId=${productoId}`)
};