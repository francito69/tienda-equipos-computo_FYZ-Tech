// frontend/src/pages/CheckoutPage.jsx
import React, { useState } from 'react';
import { 
  Container, 
  Typography, 
  Box, 
  Stepper, 
  Step, 
  StepLabel,
  Card,
  CardContent,
  Button
} from '@mui/material';
import { useCart } from '../contexts/CartContext';
import { useAuth } from '../contexts/AuthContext';
import { ordenService } from '../services/api';
import YapePayment from '../components/pagos/YapePayment';

const CheckoutPage = () => {
  const { cart, clearCart } = useCart();
  const { user } = useAuth();
  const [activeStep, setActiveStep] = useState(0);
  const [ordenId, setOrdenId] = useState(null);
  const [ordenData, setOrdenData] = useState(null);
  const [loading, setLoading] = useState(false);

  const steps = ['Resumen', 'Pago', 'Confirmación'];

  const crearOrden = async () => {
    setLoading(true);
    try {
      const ordenRequest = {
        direccionEnvio: 'Dirección de envío del usuario', // Puedes hacer un form para esto
        metodoPago: 'YAPE'
      };

      const response = await ordenService.crearOrden(ordenRequest);
      setOrdenId(response.data.id);
      setOrdenData(response.data);
      setActiveStep(1);
    } catch (error) {
      console.error('Error al crear orden:', error);
      alert('Error al crear la orden: ' + error.response?.data);
    } finally {
      setLoading(false);
    }
  };

  const handlePaymentSuccess = () => {
    setActiveStep(2);
    clearCart();
  };

  const renderStepContent = (step) => {
    switch (step) {
      case 0:
        return (
          <Box>
            <Typography variant="h5" gutterBottom>
              Resumen del Pedido
            </Typography>
            <Card variant="outlined" sx={{ mb: 3 }}>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  Items del Carrito
                </Typography>
                {cart.items.map((item, index) => (
                  <Box key={index} display="flex" justifyContent="space-between" mb={1}>
                    <Typography>
                      {item.productoNombre} x {item.cantidad}
                    </Typography>
                    <Typography>
                      S/ {item.subtotal}
                    </Typography>
                  </Box>
                ))}
                <Divider sx={{ my: 2 }} />
                <Box display="flex" justifyContent="space-between">
                  <Typography variant="h6">Total:</Typography>
                  <Typography variant="h6" color="primary">
                    S/ {cart.total}
                  </Typography>
                </Box>
              </CardContent>
            </Card>
            <Button 
              variant="contained" 
              size="large" 
              onClick={crearOrden}
              disabled={loading || cart.items.length === 0}
              fullWidth
            >
              {loading ? 'Creando Orden...' : 'Continuar al Pago'}
            </Button>
          </Box>
        );

      case 1:
        return (
          <Box>
            <Typography variant="h5" gutterBottom>
              Pago con Yape
            </Typography>
            {ordenId && (
              <YapePayment 
                ordenId={ordenId}
                montoTotal={ordenData.montoTotal}
                onPaymentSuccess={handlePaymentSuccess}
              />
            )}
          </Box>
        );

      case 2:
        return (
          <Box textAlign="center" py={6}>
            <Typography variant="h4" gutterBottom color="success.main">
              ¡Orden Confirmada!
            </Typography>
            <Typography variant="h6" gutterBottom>
              Número de orden: #{ordenId?.substring(0, 8).toUpperCase()}
            </Typography>
            <Typography variant="body1" color="text.secondary" paragraph>
              Tu pago ha sido verificado y tu orden está siendo procesada.
              Recibirás una confirmación por correo electrónico.
            </Typography>
            <Button 
              variant="contained" 
              size="large"
              onClick={() => window.location.href = '/mis-ordenes'}
              sx={{ mt: 2 }}
            >
              Ver Mis Órdenes
            </Button>
          </Box>
        );

      default:
        return null;
    }
  };

  if (!user) {
    return (
      <Container maxWidth="md" sx={{ py: 4 }}>
        <Typography variant="h5" textAlign="center">
          Debes iniciar sesión para realizar una compra
        </Typography>
      </Container>
    );
  }

  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Typography variant="h4" gutterBottom align="center">
        Checkout
      </Typography>
      
      <Stepper activeStep={activeStep} sx={{ mb: 4 }}>
        {steps.map((label) => (
          <Step key={label}>
            <StepLabel>{label}</StepLabel>
          </Step>
        ))}
      </Stepper>

      <Card>
        <CardContent>
          {renderStepContent(activeStep)}
        </CardContent>
      </Card>
    </Container>
  );
};

export default CheckoutPage;