// frontend/src/services/api/pagoService.js
import api from './config';

export const pagoService = {
  generarQRYape: (ordenId) => 
    api.post(`/pagos/orden/${ordenId}/generar-qr`),
  
  subirComprobante: (ordenId, comprobante) => {
    const formData = new FormData();
    formData.append('comprobante', comprobante);
    
    return api.post(`/pagos/orden/${ordenId}/subir-comprobante`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  },
  
  obtenerPagoPorOrden: (ordenId) => 
    api.get(`/pagos/orden/${ordenId}`),
  
  verificarPago: (pagoId, verificacion) => 
    api.post(`/pagos/${pagoId}/verificar`, verificacion),
  
  obtenerPagosPendientes: () => 
    api.get('/pagos/pendientes'),
  
  obtenerPagosPorEstado: (estado) => 
    api.get(`/pagos/estado/${estado}`)
};