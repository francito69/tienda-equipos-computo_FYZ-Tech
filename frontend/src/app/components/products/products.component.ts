import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ProductService, Product } from '../../services/product.service';
import { CartService } from '../../services/cart.service';

@Component({
    selector: 'app-products',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './products.component.html',
    styleUrl: './products.component.css'
})
export class ProductsComponent implements OnInit {
    products: Product[] = [];
    searchQuery: string = '';
    isLoading: boolean = false;
    errorMessage: string = '';

    constructor(
        private productService: ProductService,
        private cartService: CartService,
        private router: Router,
        private cdr: ChangeDetectorRef
    ) { }

    ngOnInit(): void {
        this.loadProducts();
    }

    loadProducts(): void {
        const query = this.searchQuery.trim() || '';
        this.isLoading = true;
        this.errorMessage = '';

        this.productService.searchProducts(query).subscribe({
            next: (products) => {
                this.products = products;
                this.isLoading = false;
                this.cdr.markForCheck();
            },
            error: (error) => {
                this.errorMessage = 'Error al cargar productos';
                this.isLoading = false;
                this.cdr.markForCheck();
                console.error('Error loading products:', error);
            }
        });
    }

    onSearch(): void {
        this.loadProducts();
    }

    viewProduct(id: number): void {
        this.router.navigate(['/producto', id]);
    }

    quickAddToCart(product: Product): void {
        this.cartService.addToCart(product, 1);
        this.cdr.detectChanges();
        // Optional: Show toast notification
        alert(`${product.name} añadido al carrito`);
    }

    formatPrice(price: number): string {
        return new Intl.NumberFormat('es-PE', {
            style: 'currency',
            currency: 'PEN'
        }).format(price);
    }
}
