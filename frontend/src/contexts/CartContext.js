// frontend/src/contexts/CartContext.js
import React, { createContext, useState, useContext, useEffect } from 'react';
import { carritoService } from '../services/api';

const CartContext = createContext();

export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error('useCart debe ser usado dentro de un CartProvider');
  }
  return context;
};

export const CartProvider = ({ children }) => {
  const [cart, setCart] = useState({
    items: [],
    total: 0,
    totalItems: 0
  });
  const [loading, setLoading] = useState(false);

  const fetchCart = async () => {
    try {
      const response = await carritoService.obtenerCarrito();
      setCart(response.data);
    } catch (error) {
      console.error('Error al obtener carrito:', error);
    }
  };

  const addToCart = async (productoId, cantidad = 1) => {
    setLoading(true);
    try {
      await carritoService.agregarAlCarrito(productoId, cantidad);
      await fetchCart();
    } catch (error) {
      console.error('Error al agregar al carrito:', error);
      throw error;
    } finally {
      setLoading(false);
    }
  };

  const updateQuantity = async (productoId, cantidad) => {
    try {
      await carritoService.actualizarCantidad(productoId, cantidad);
      await fetchCart();
    } catch (error) {
      console.error('Error al actualizar cantidad:', error);
      throw error;
    }
  };

  const removeFromCart = async (productoId) => {
    try {
      await carritoService.removerDelCarrito(productoId);
      await fetchCart();
    } catch (error) {
      console.error('Error al remover del carrito:', error);
      throw error;
    }
  };

  const clearCart = () => {
    setCart({
      items: [],
      total: 0,
      totalItems: 0
    });
  };

  useEffect(() => {
    fetchCart();
  }, []);

  const value = {
    cart,
    addToCart,
    updateQuantity,
    removeFromCart,
    clearCart,
    loading,
    refreshCart: fetchCart
  };

  return (
    <CartContext.Provider value={value}>
      {children}
    </CartContext.Provider>
  );
};