import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { ProductsComponent } from './components/products/products.component';
import { ProductDetailComponent } from './components/product-detail/product-detail.component';
import { CartComponent } from './components/cart/cart.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
    { path: '', component: ProductsComponent },
    { path: 'producto/:id', component: ProductDetailComponent },
    { path: 'carrito', component: CartComponent },
    { path: 'login', component: LoginComponent },
    { path: 'admin', component: AdminDashboardComponent, canActivate: [authGuard] },
    { path: '**', redirectTo: '' }
];
