import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CartService, CartItem } from '../../services/cart.service';

@Component({
    selector: 'app-cart',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './cart.component.html',
    styleUrl: './cart.component.css'
})
export class CartComponent implements OnInit {
    cartItems: CartItem[] = [];
    total: number = 0;

    constructor(
        private cartService: CartService,
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

    confirmPurchase(): void {
        if (this.cartItems.length === 0) {
            alert('El carrito está vacío');
            return;
        }

        alert(`Compra confirmada!\nTotal: ${this.formatPrice(this.total)}\n\nGracias por tu compra!`);
        this.cartService.clearCart();
        this.router.navigate(['/']);
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
