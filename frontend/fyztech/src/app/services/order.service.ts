import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';
import { CartItem } from './cart.service';

export interface OrderRequest {
    customerName: string;
    customerEmail: string;
    customerPhone: string;
    deliveryAddress: string;
    totalPrice: number;
    idsCantidad: IdQuantity[];
}

export interface IdQuantity {
    id: number;
    quantity: number;
}

export interface OrderResponse {
    success: boolean;
    message: string;
}

export interface Order {
    id: number;
    customer_name: string;
    customer_phone: string;
    customer_email: string;
    delivery_address: string;
    total: number;
}

export interface OrderItem {
    id: number;
    idOrder: number;
    idProduct: number;
    quantity: number;
    productName: string;
    productPrice: number;
    productImgUrl: string;
}

@Injectable({
    providedIn: 'root'
})
export class OrderService {
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient) { }

    createOrder(orderRequest: OrderRequest): Observable<OrderResponse> {
        return this.http.post<OrderResponse>(
            `${this.apiUrl}/orders`,
            orderRequest
        );
    }

    getAllOrders(): Observable<Order[]> {
        return this.http.get<Order[]>(`${this.apiUrl}/orders`, {
            withCredentials: true
        });
    }

    getOrderItems(orderId: number): Observable<OrderItem[]> {
        return this.http.get<OrderItem[]>(`${this.apiUrl}/orders/${orderId}/items`, {
            withCredentials: true
        });
    }

    cartItemsToOrderItems(cartItems: CartItem[]): IdQuantity[] {
        return cartItems.map(item => ({
            id: item.product.id,
            quantity: item.quantity
        }));
    }
}
