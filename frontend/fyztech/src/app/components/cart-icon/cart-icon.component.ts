import { Component, OnInit, ChangeDetectorRef, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, NavigationEnd } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';

@Component({
    selector: 'app-cart-icon',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './cart-icon.component.html',
    styleUrl: './cart-icon.component.css'
})
export class CartIconComponent implements OnInit, OnDestroy {
    cartCount: number = 0;
    showCart: boolean = true;
    private cartSubscription?: Subscription;
    private routerSubscription?: Subscription;

    constructor(
        private cartService: CartService,
        private router: Router,
        private cdr: ChangeDetectorRef
    ) { }

    ngOnInit(): void {
        // Subscribe to cart changes
        this.cartSubscription = this.cartService.cart$.subscribe(() => {
            this.cartCount = this.cartService.getCartCount();
            this.cdr.markForCheck();
        });

        // Subscribe to route changes to hide cart on admin/login pages
        this.routerSubscription = this.router.events.pipe(
            filter(event => event instanceof NavigationEnd)
        ).subscribe((event: any) => {
            const url = event.url;
            this.showCart = !url.includes('/admin') && !url.includes('/login');
            this.cdr.markForCheck();
        });

        // Check initial route
        const currentUrl = this.router.url;
        this.showCart = !currentUrl.includes('/admin') && !currentUrl.includes('/login');
        this.cdr.markForCheck();
    }

    ngOnDestroy(): void {
        this.cartSubscription?.unsubscribe();
        this.routerSubscription?.unsubscribe();
    }

    goToCart(): void {
        this.router.navigate(['/carrito']);
    }
}
