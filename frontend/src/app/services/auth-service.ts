import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  isLoggedIn(): boolean {
    return !!localStorage.getItem('auth');
  }

  getAuthHeader(): string | null {
    return localStorage.getItem('auth');
  }

  login(auth: string): void {
    localStorage.setItem('auth', auth);
  }

  logout(): void {
    localStorage.removeItem('auth');
  }
}
