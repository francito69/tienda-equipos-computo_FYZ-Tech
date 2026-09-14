import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService, Order, OrderItem } from '../../services/order.service';

@Component({
    selector: 'app-orders',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './orders.component.html',
    styleUrl: './orders.component.css'
})
export class OrdersComponent implements OnInit {
    orders: Order[] = [];
    selectedOrderItems: OrderItem[] = [];
    selectedOrder: Order | null = null;
    showModal: boolean = false;
    isLoading: boolean = false;
    isLoadingItems: boolean = false;
    errorMessage: string = '';

    constructor(
        private orderService: OrderService,
        private cdr: ChangeDetectorRef
    ) { }

    ngOnInit(): void {
        this.loadOrders();
    }

    loadOrders(): void {
        this.isLoading = true;
        this.errorMessage = '';

        this.orderService.getAllOrders().subscribe({
            next: (orders) => {
                this.orders = orders;
                this.isLoading = false;
                this.cdr.markForCheck();
            },
            error: (error) => {
                this.errorMessage = 'Error al cargar las órdenes';
                this.isLoading = false;
                this.cdr.markForCheck();
                console.error('Error loading orders:', error);
            }
        });
    }

    viewProducts(order: Order): void {
        this.selectedOrder = order;
        this.showModal = true;
        this.isLoadingItems = true;
        this.selectedOrderItems = [];

        this.orderService.getOrderItems(order.id).subscribe({
            next: (items) => {
                this.selectedOrderItems = items;
                this.isLoadingItems = false;
                this.cdr.detectChanges();
            },
            error: (error) => {
                console.error('Error loading order items:', error);
                this.isLoadingItems = false;
                this.cdr.detectChanges();
            }
        });
    }

    closeModal(): void {
        this.showModal = false;
        this.selectedOrder = null;
        this.selectedOrderItems = [];
        this.cdr.detectChanges();
    }

    formatPrice(price: number): string {
        return new Intl.NumberFormat('es-PE', {
            style: 'currency',
            currency: 'PEN'
        }).format(price);
    }

    getSubtotal(item: OrderItem): number {
        return item.productPrice * item.quantity;
    }
}
