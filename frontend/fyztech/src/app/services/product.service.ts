import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';

export interface Product {
    id: number;
    createdAt: string;
    name: string;
    price: number;
    descr: string;
    createdBy: number;
    imgUrl: string;
}

export interface ProductResponse {
    success: boolean;
    message: string;
}

export interface CreateProductRequest {
    name: string;
    price: number;
    descr: string;
    imgUrl: string;
}

@Injectable({
    providedIn: 'root'
})
export class ProductService {
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient) { }

    /**
     * Search products by query (name or description)
     */
    searchProducts(query: string): Observable<Product[]> {
        const params = new HttpParams().set('query', query);
        return this.http.get<Product[]>(`${this.apiUrl}/products/search`, { params });
    }

    /**
     * Get product by ID
     */
    getProductById(id: number): Observable<Product> {
        return this.http.get<Product>(`${this.apiUrl}/products/${id}`);
    }

    /**
     * Get products created by authenticated admin
     */
    getMyProducts(): Observable<Product[]> {
        return this.http.get<Product[]>(
            `${this.apiUrl}/products/my-products`,
            { withCredentials: true }
        );
    }

    /**
     * Create a new product (admin only)
     */
    createProduct(product: CreateProductRequest): Observable<ProductResponse> {
        return this.http.post<ProductResponse>(
            `${this.apiUrl}/products`,
            product,
            { withCredentials: true }
        );
    }

    /**
     * Delete a product by ID (admin only)
     */
    deleteProduct(id: number): Observable<ProductResponse> {
        return this.http.delete<ProductResponse>(
            `${this.apiUrl}/products/${id}`,
            { withCredentials: true }
        );
    }
}
