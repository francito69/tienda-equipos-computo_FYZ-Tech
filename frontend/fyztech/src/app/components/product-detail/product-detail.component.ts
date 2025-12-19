import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService, Product } from '../../services/product.service';
import { CartService } from '../../services/cart.service';

@Component({
    selector: 'app-product-detail',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './product-detail.component.html',
    styleUrl: './product-detail.component.css'
})
export class ProductDetailComponent implements OnInit {
    product: Product | null = null;
    quantity: number = 1;
    isLoading: boolean = false;
    errorMessage: string = '';

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private productService: ProductService,
        private cartService: CartService,
        private cdr: ChangeDetectorRef
    ) { }

    ngOnInit(): void {
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.loadProduct(parseInt(id));
        }
    }

    loadProduct(id: number): void {
        this.isLoading = true;
        this.productService.getProductById(id).subscribe({
            next: (product) => {
                this.product = product;
                this.isLoading = false;
                this.cdr.markForCheck();
            },
            error: (error) => {
                this.errorMessage = 'Error al cargar el producto';
                this.isLoading = false;
                this.cdr.markForCheck();
                console.error('Error loading product:', error);
            }
        });
    }

    increaseQuantity(): void {
        this.quantity++;
        this.cdr.detectChanges();
    }

    decreaseQuantity(): void {
        if (this.quantity > 1) {
            this.quantity--;
            this.cdr.detectChanges();
        }
    }

    addToCart(): void {
        if (this.product) {
            this.cartService.addToCart(this.product, this.quantity);
            this.cdr.detectChanges();
            alert(`${this.quantity} x ${this.product.name} añadido al carrito`);
            this.router.navigate(['/']);
        }
    }

    goBack(): void {
        this.router.navigate(['/']);
    }

    formatPrice(price: number): string {
        return new Intl.NumberFormat('es-PE', {
            style: 'currency',
            currency: 'PEN'
        }).format(price);
    }
}
