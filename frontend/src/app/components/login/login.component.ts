import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './login.component.html',
    styleUrl: './login.component.css'
})
export class LoginComponent {
    email: string = '';
    password: string = '';
    showPassword: boolean = false;
    errorMessage: string = '';
    isLoading: boolean = false;

    constructor(
        private authService: AuthService,
        private router: Router
    ) { }

    togglePasswordVisibility(): void {
        this.showPassword = !this.showPassword;
    }

    onSubmit(): void {
        if (!this.email || !this.password) {
            this.errorMessage = 'Por favor completa todos los campos';
            return;
        }

        this.isLoading = true;
        this.errorMessage = '';

        this.authService.login(this.email, this.password).subscribe({
            next: (response) => {
                if (response.success) {
                    this.router.navigate(['/admin']);
                } else {
                    this.errorMessage = response.message;
                }
                this.isLoading = false;
            },
            error: (error) => {
                this.errorMessage = 'Error al iniciar sesión. Por favor intenta de nuevo.';
                this.isLoading = false;
                console.error('Login error:', error);
            }
        });
    }
}
