import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ProductService, Product, CreateProductRequest } from '../../services/product.service';
import { SupabaseService } from '../../services/supabase.service';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-admin-dashboard',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './admin-dashboard.component.html',
    styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit {
    products: Product[] = [];
    isLoading: boolean = false;
    errorMessage: string = '';

    // Form state
    showForm: boolean = false;
    isSubmitting: boolean = false;
    selectedFile: File | null = null;
    imagePreview: string | null = null;

    // Form fields
    productName: string = '';
    productPrice: number = 0;
    productDescr: string = '';

    constructor(
        private productService: ProductService,
        private supabaseService: SupabaseService,
        private authService: AuthService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.loadMyProducts();
    }

    loadMyProducts(): void {
        this.isLoading = true;
        this.errorMessage = '';

        this.productService.getMyProducts().subscribe({
            next: (products) => {
                this.products = products;
                this.isLoading = false;
            },
            error: (error) => {
                this.errorMessage = 'Error al cargar tus productos';
                this.isLoading = false;
                console.error('Error loading products:', error);
            }
        });
    }

    onFileSelected(event: Event): void {
        const input = event.target as HTMLInputElement;
        if (input.files && input.files[0]) {
            this.selectedFile = input.files[0];

            // Create preview
            const reader = new FileReader();
            reader.onload = (e) => {
                this.imagePreview = e.target?.result as string;
            };
            reader.readAsDataURL(this.selectedFile);
        }
    }

    async onSubmit(): Promise<void> {
        if (!this.productName || !this.productPrice || !this.productDescr || !this.selectedFile) {
            alert('Por favor completa todos los campos y selecciona una imagen');
            return;
        }

        this.isSubmitting = true;

        try {
            // Upload image to Supabase
            const imgUrl = await this.supabaseService.uploadImage(this.selectedFile);

            // Create product
            const productRequest: CreateProductRequest = {
                name: this.productName,
                price: this.productPrice,
                descr: this.productDescr,
                imgUrl: imgUrl
            };

            this.productService.createProduct(productRequest).subscribe({
                next: (response) => {
                    if (response.success) {
                        alert('Producto creado exitosamente');
                        this.resetForm();
                        this.loadMyProducts();
                    } else {
                        alert('Error: ' + response.message);
                    }
                    this.isSubmitting = false;
                },
                error: (error) => {
                    alert('Error al crear producto');
                    console.error('Error creating product:', error);
                    this.isSubmitting = false;
                }
            });
        } catch (error) {
            alert('Error al subir la imagen');
            console.error('Error uploading image:', error);
            this.isSubmitting = false;
        }
    }

    deleteProduct(id: number): void {
        if (!confirm('¿Estás seguro de eliminar este producto?')) {
            return;
        }

        this.productService.deleteProduct(id).subscribe({
            next: (response) => {
                if (response.success) {
                    alert('Producto eliminado');
                    this.loadMyProducts();
                } else {
                    alert('Error: ' + response.message);
                }
            },
            error: (error) => {
                alert('Error al eliminar producto');
                console.error('Error deleting product:', error);
            }
        });
    }

    resetForm(): void {
        this.showForm = false;
        this.productName = '';
        this.productPrice = 0;
        this.productDescr = '';
        this.selectedFile = null;
        this.imagePreview = null;
    }

    logout(): void {
        this.authService.logout();
        this.router.navigate(['/login']);
    }

    formatPrice(price: number): string {
        return new Intl.NumberFormat('es-PE', {
            style: 'currency',
            currency: 'PEN'
        }).format(price);
    }
}
