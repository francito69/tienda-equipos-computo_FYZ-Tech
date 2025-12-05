// frontend/src/services/api/ordenService.js
import api from './config';

export const ordenService = {
  crearOrden: (ordenData) => api.post('/ordenes', ordenData),
  obtenerOrdenes: () => api.get('/ordenes'),
  obtenerOrdenPorId: (id) => api.get(`/ordenes/${id}`)
};