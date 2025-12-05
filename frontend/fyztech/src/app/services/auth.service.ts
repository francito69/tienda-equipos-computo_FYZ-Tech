import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../environment';

export interface LoginRequest {
    email: string;
    password: string;
}

export interface LoginResponse {
    success: boolean;
    message: string;
    email?: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = environment.apiUrl;
    private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
    public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

    constructor(private http: HttpClient) {
        // Check if user is already authenticated on init
        this.checkAuthStatus();
    }

    /**
     * Login with email and password
     */
    login(email: string, password: string): Observable<LoginResponse> {
        const loginRequest: LoginRequest = { email, password };

        return this.http.post<LoginResponse>(
            `${this.apiUrl}/auth/login`,
            loginRequest,
            { withCredentials: true } // Important for cookies
        ).pipe(
            tap(response => {
                if (response.success) {
                    this.isAuthenticatedSubject.next(true);
                    localStorage.setItem('userEmail', response.email || '');
                }
            })
        );
    }

    /**
     * Logout user
     */
    logout(): void {
        this.isAuthenticatedSubject.next(false);
        localStorage.removeItem('userEmail');
        // Cookie will be cleared by backend or expire
    }

    /**
     * Check if user is authenticated
     */
    isAuthenticated(): boolean {
        return this.isAuthenticatedSubject.value;
    }

    /**
     * Get current user email
     */
    getUserEmail(): string | null {
        return localStorage.getItem('userEmail');
    }

    /**
     * Check auth status (can be enhanced to verify with backend)
     */
    private checkAuthStatus(): void {
        const userEmail = localStorage.getItem('userEmail');
        if (userEmail) {
            this.isAuthenticatedSubject.next(true);
        }
    }
}
