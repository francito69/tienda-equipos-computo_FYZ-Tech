import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CartService, CartItem } from '../../services/cart.service';
import { OrderService, OrderRequest } from '../../services/order.service';

@Component({
    selector: 'app-cart',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './cart.component.html',
    styleUrl: './cart.component.css'
})
export class CartComponent implements OnInit {
    cartItems: CartItem[] = [];
    total: number = 0;
    showCheckoutForm: boolean = false;
    isSubmitting: boolean = false;

    // Form fields
    customerName: string = '';
    customerEmail: string = '';
    customerPhone: string = '';
    deliveryAddress: string = '';

    constructor(
        private cartService: CartService,
        private orderService: OrderService,
        private router: Router,
        private cdr: ChangeDetectorRef
    ) { }

    ngOnInit(): void {
        this.loadCart();
    }

    loadCart(): void {
        this.cartItems = this.cartService.getCart();
        this.calculateTotal();
        this.cdr.markForCheck();
    }

    calculateTotal(): void {
        this.total = this.cartService.getCartTotal();
    }

    increaseQuantity(productId: number): void {
        const item = this.cartItems.find(i => i.product.id === productId);
        if (item) {
            this.cartService.updateQuantity(productId, item.quantity + 1);
            this.loadCart();
            this.cdr.detectChanges();
        }
    }

    decreaseQuantity(productId: number): void {
        const item = this.cartItems.find(i => i.product.id === productId);
        if (item && item.quantity > 1) {
            this.cartService.updateQuantity(productId, item.quantity - 1);
            this.loadCart();
            this.cdr.detectChanges();
        }
    }

    removeItem(productId: number): void {
        if (confirm('¿Eliminar este producto del carrito?')) {
            this.cartService.removeFromCart(productId);
            this.loadCart();
            this.cdr.detectChanges();
        }
    }

    proceedToCheckout(): void {
        if (this.cartItems.length === 0) {
            alert('El carrito está vacío');
            return;
        }
        this.showCheckoutForm = true;
        this.cdr.detectChanges();
    }

    confirmPurchase(): void {
        if (!this.customerName || !this.customerEmail || !this.customerPhone || !this.deliveryAddress) {
            alert('Por favor completa todos los campos');
            return;
        }

        this.isSubmitting = true;

        const orderRequest: OrderRequest = {
            customerName: this.customerName,
            customerEmail: this.customerEmail,
            customerPhone: this.customerPhone,
            deliveryAddress: this.deliveryAddress,
            totalPrice: this.total,
            idsCantidad: this.orderService.cartItemsToOrderItems(this.cartItems)
        };

        this.orderService.createOrder(orderRequest).subscribe({
            next: (response) => {
                if (response.success) {
                    alert(`¡Compra confirmada!\nTotal: ${this.formatPrice(this.total)}\n\nGracias por tu compra, ${this.customerName}!`);
                    this.cartService.clearCart();
                    this.router.navigate(['/']);
                } else {
                    alert('Error: ' + response.message);
                }
                this.isSubmitting = false;
                this.cdr.detectChanges();
            },
            error: (error) => {
                alert('Error al procesar la orden. Por favor intenta de nuevo.');
                console.error('Error creating order:', error);
                this.isSubmitting = false;
                this.cdr.detectChanges();
            }
        });
    }

    cancelCheckout(): void {
        this.showCheckoutForm = false;
        this.customerName = '';
        this.customerEmail = '';
        this.customerPhone = '';
        this.deliveryAddress = '';
        this.cdr.detectChanges();
    }

    continueShopping(): void {
        this.router.navigate(['/']);
    }

    formatPrice(price: number): string {
        return new Intl.NumberFormat('es-PE', {
            style: 'currency',
            currency: 'PEN'
        }).format(price);
    }

    getSubtotal(item: CartItem): number {
        return item.product.price * item.quantity;
    }
}
