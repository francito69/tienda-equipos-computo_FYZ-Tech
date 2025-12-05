import { Injectable, ChangeDetectorRef } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Product } from './product.service';

export interface CartItem {
    product: Product;
    quantity: number;
}

@Injectable({
    providedIn: 'root'
})
export class CartService {
    private readonly CART_KEY = 'shopping_cart';
    private cartSubject = new BehaviorSubject<CartItem[]>(this.loadCart());
    public cart$ = this.cartSubject.asObservable();

    constructor() { }

    /**
     * Load cart from localStorage
     */
    private loadCart(): CartItem[] {
        const cartData = localStorage.getItem(this.CART_KEY);
        return cartData ? JSON.parse(cartData) : [];
    }

    /**
     * Save cart to localStorage
     */
    private saveCart(cart: CartItem[]): void {
        localStorage.setItem(this.CART_KEY, JSON.stringify(cart));
        this.cartSubject.next(cart);
    }

    /**
     * Get current cart items
     */
    getCart(): CartItem[] {
        return this.cartSubject.value;
    }

    /**
     * Add product to cart or update quantity if exists
     */
    addToCart(product: Product, quantity: number = 1): void {
        const cart = this.getCart();
        const existingItem = cart.find(item => item.product.id === product.id);

        if (existingItem) {
            existingItem.quantity += quantity;
        } else {
            cart.push({ product, quantity });
        }

        this.saveCart(cart);
    }

    /**
     * Update quantity of a cart item
     */
    updateQuantity(productId: number, quantity: number): void {
        const cart = this.getCart();
        const item = cart.find(item => item.product.id === productId);

        if (item) {
            if (quantity <= 0) {
                this.removeFromCart(productId);
            } else {
                item.quantity = quantity;
                this.saveCart(cart);
            }
        }
    }

    /**
     * Remove item from cart
     */
    removeFromCart(productId: number): void {
        const cart = this.getCart().filter(item => item.product.id !== productId);
        this.saveCart(cart);
    }

    /**
     * Get total number of items in cart
     */
    getCartCount(): number {
        return this.getCart().reduce((total, item) => total + item.quantity, 0);
    }

    /**
     * Get total price of cart
     */
    getCartTotal(): number {
        return this.getCart().reduce((total, item) =>
            total + (item.product.price * item.quantity), 0
        );
    }

    /**
     * Clear entire cart
     */
    clearCart(): void {
        this.saveCart([]);
    }

    /**
     * Check if product is in cart
     */
    isInCart(productId: number): boolean {
        return this.getCart().some(item => item.product.id === productId);
    }

    /**
     * Get quantity of specific product in cart
     */
    getProductQuantity(productId: number): number {
        const item = this.getCart().find(item => item.product.id === productId);
        return item ? item.quantity : 0;
    }
}
