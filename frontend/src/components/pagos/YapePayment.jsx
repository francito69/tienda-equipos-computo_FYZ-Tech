// frontend/src/components/pagos/YapePayment.jsx
import React, { useState, useEffect } from 'react';
import { 
  Box, 
  Card, 
  CardContent, 
  Typography, 
  Button, 
  Stepper, 
  Step, 
  StepLabel,
  CircularProgress,
  Alert,
  Paper,
  Divider
} from '@mui/material';
import { 
  QrCode2, 
  Upload, 
  Verified, 
  Payment,
  CheckCircle
} from '@mui/icons-material';
import { pagoService } from '../../services/api';

const YapePayment = ({ ordenId, montoTotal, onPaymentSuccess }) => {
  const [activeStep, setActiveStep] = useState(0);
  const [qrData, setQrData] = useState(null);
  const [pagoInfo, setPagoInfo] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [file, setFile] = useState(null);

  const steps = [
    'Generar QR Yape',
    'Realizar Pago',
    'Subir Comprobante',
    'Verificación'
  ];

  // Verificar estado actual del pago
  useEffect(() => {
    verificarEstadoPago();
  }, [ordenId]);

  const verificarEstadoPago = async () => {
    try {
      const response = await pagoService.obtenerPagoPorOrden(ordenId);
      if (response.data) {
        setPagoInfo(response.data);
        actualizarPasos(response.data.estado);
      }
    } catch (error) {
      console.log('No hay pago existente, comenzando nuevo proceso...');
    }
  };

  const actualizarPasos = (estadoPago) => {
    switch (estadoPago) {
      case 'PENDIENTE':
        setActiveStep(1);
        break;
      case 'PENDIENTE_VERIFICACION':
        setActiveStep(3);
        break;
      case 'VERIFICADO':
        setActiveStep(4);
        onPaymentSuccess?.();
        break;
      case 'RECHAZADO':
        setActiveStep(1);
        setError('Pago rechazado. Por favor, sube un comprobante válido.');
        break;
      default:
        setActiveStep(0);
    }
  };

  const generarQR = async () => {
    setLoading(true);
    setError('');
    
    try {
      // Usar tu QR personalizado - cambia "mi_yape_qr.png" por el nombre de tu archivo
      const response = await pagoService.crearPagoConQR(ordenId, 'mi_yape_qr.png');
      setQrData(response.data);
      setActiveStep(1);
      
      // Actualizar información del pago
      verificarEstadoPago();
      
    } catch (error) {
      setError(error.response?.data || 'Error al generar QR');
    } finally {
      setLoading(false);
    }
  };

  const handleFileChange = (event) => {
    const selectedFile = event.target.files[0];
    if (selectedFile) {
      // Validar tipo de archivo
      const validTypes = ['image/jpeg', 'image/png', 'image/jpg', 'application/pdf'];
      if (!validTypes.includes(selectedFile.type)) {
        setError('Por favor, sube una imagen JPG, PNG o PDF');
        return;
      }
      
      // Validar tamaño (5MB máximo)
      if (selectedFile.size > 5 * 1024 * 1024) {
        setError('El archivo debe ser menor a 5MB');
        return;
      }
      
      setFile(selectedFile);
      setError('');
    }
  };

  const subirComprobante = async () => {
    if (!file) {
      setError('Por favor, selecciona un archivo');
      return;
    }

    setLoading(true);
    
    try {
      const response = await pagoService.subirComprobante(ordenId, file);
      setPagoInfo(response.data);
      setActiveStep(3);
      setError('');
      
    } catch (error) {
      setError(error.response?.data || 'Error al subir comprobante');
    } finally {
      setLoading(false);
    }
  };

  const renderStepContent = (step) => {
    switch (step) {
      case 0:
        return (
          <Box textAlign="center" py={4}>
            <Payment sx={{ fontSize: 64, color: 'primary.main', mb: 2 }} />
            <Typography variant="h5" gutterBottom>
              Pago con Yape
            </Typography>
            <Typography variant="body1" color="text.secondary" paragraph>
              Total a pagar: <strong>S/ {montoTotal}</strong>
            </Typography>
            <Button 
              variant="contained" 
              size="large"
              onClick={generarQR}
              disabled={loading}
              startIcon={loading ? <CircularProgress size={20} /> : <QrCode2 />}
            >
              {loading ? 'Generando QR...' : 'Generar QR Yape'}
            </Button>
          </Box>
        );

      case 1:
        return (
          <Box textAlign="center" py={3}>
            <Typography variant="h6" gutterBottom>
              Escanea este QR con Yape
            </Typography>
            
            {qrData && (
              <Box mt={2}>
                <Paper elevation={3} sx={{ p: 2, display: 'inline-block', mb: 2 }}>
                  <img 
                    src={`http://localhost:8080${qrData.qrImageUrl}`} 
                    alt="QR Yape" 
                    style={{ width: 250, height: 250 }}
                    onError={(e) => {
                      e.target.src = '/placeholder-qr.png'; // Fallback image
                    }}
                  />
                </Paper>
                
                <Card variant="outlined" sx={{ mb: 3, maxWidth: 400, mx: 'auto' }}>
                  <CardContent>
                    <Typography variant="body2" color="text.secondary" gutterBottom>
                      💡 Instrucciones:
                    </Typography>
                    <Typography variant="body2" sx={{ whiteSpace: 'pre-line', textAlign: 'left' }}>
                      {qrData.instrucciones}
                    </Typography>
                    <Divider sx={{ my: 2 }} />
                    <Typography variant="body2">
                      <strong>📱 Número Yape:</strong> {qrData.numeroCelular}
                    </Typography>
                    <Typography variant="body2">
                      <strong>💰 Monto exacto:</strong> S/ {qrData.monto}
                    </Typography>
                    <Typography variant="body2">
                      <strong>📝 Concepto:</strong> {qrData.concepto}
                    </Typography>
                  </CardContent>
                </Card>
              </Box>
            )}

            <input
              type="file"
              accept="image/jpeg,image/png,image/jpg,application/pdf"
              onChange={handleFileChange}
              style={{ display: 'none' }}
              id="comprobante-file"
            />
            <label htmlFor="comprobante-file">
              <Button 
                variant="outlined" 
                component="span"
                startIcon={<Upload />}
              >
                Subir Comprobante
              </Button>
            </label>
            
            {file && (
              <Box mt={2}>
                <Typography variant="body2">
                  Archivo seleccionado: {file.name}
                </Typography>
                <Button 
                  variant="contained" 
                  onClick={subirComprobante}
                  disabled={loading}
                  sx={{ mt: 1 }}
                  startIcon={loading ? <CircularProgress size={20} /> : <Upload />}
                >
                  {loading ? 'Subiendo...' : 'Confirmar Subida'}
                </Button>
              </Box>
            )}
          </Box>
        );

      case 2:
        return (
          <Box textAlign="center" py={4}>
            <Upload sx={{ fontSize: 64, color: 'warning.main', mb: 2 }} />
            <Typography variant="h6" gutterBottom>
              Sube tu comprobante
            </Typography>
            <Typography variant="body2" color="text.secondary" paragraph>
              Por favor, sube una captura de pantalla del comprobante de pago de Yape
            </Typography>
            
            <input
              type="file"
              accept="image/jpeg,image/png,image/jpg,application/pdf"
              onChange={handleFileChange}
              style={{ display: 'none' }}
              id="comprobante-file-2"
            />
            <label htmlFor="comprobante-file-2">
              <Button 
                variant="contained" 
                component="span"
                startIcon={<Upload />}
                sx={{ mb: 2 }}
              >
                Seleccionar Archivo
              </Button>
            </label>
            
            {file && (
              <Box>
                <Typography variant="body2" sx={{ mb: 2 }}>
                  <strong>Archivo:</strong> {file.name}
                </Typography>
                <Button 
                  variant="contained" 
                  onClick={subirComprobante}
                  disabled={loading}
                  startIcon={loading ? <CircularProgress size={20} /> : <Upload />}
                >
                  {loading ? 'Subiendo...' : 'Subir Comprobante'}
                </Button>
              </Box>
            )}
          </Box>
        );

      case 3:
        return (
          <Box textAlign="center" py={4}>
            <Verified sx={{ fontSize: 64, color: 'info.main', mb: 2 }} />
            <Typography variant="h6" gutterBottom>
              Comprobante Recibido
            </Typography>
            <Typography variant="body2" color="text.secondary" paragraph>
              Tu comprobante ha sido subido exitosamente y está en proceso de verificación.
              Te notificaremos cuando sea aprobado.
            </Typography>
            <Alert severity="info" sx={{ maxWidth: 400, mx: 'auto' }}>
              Estado actual: Pendiente de verificación
            </Alert>
          </Box>
        );

      case 4:
        return (
          <Box textAlign="center" py={4}>
            <CheckCircle sx={{ fontSize: 64, color: 'success.main', mb: 2 }} />
            <Typography variant="h6" gutterBottom color="success.main">
              ¡Pago Verificado!
            </Typography>
            <Typography variant="body2" color="text.secondary" paragraph>
              Tu pago ha sido verificado exitosamente. Tu orden está siendo procesada.
            </Typography>
            <Alert severity="success" sx={{ maxWidth: 400, mx: 'auto' }}>
              Pago aprobado - Orden confirmada
            </Alert>
          </Box>
        );

      default:
        return null;
    }
  };

  return (
    <Card>
      <CardContent>
        <Stepper activeStep={activeStep} alternativeLabel sx={{ mb: 4 }}>
          {steps.map((label) => (
            <Step key={label}>
              <StepLabel>{label}</StepLabel>
            </Step>
          ))}
        </Stepper>

        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        {renderStepContent(activeStep)}
      </CardContent>
    </Card>
  );
};

export default YapePayment;