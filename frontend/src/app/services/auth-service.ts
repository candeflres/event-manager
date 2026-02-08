import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private authApi = 'http://localhost:8080/api/auth';
  private userApi = 'http://localhost:8080/api/users';

  // auth header (Basic ...)
  private _auth = signal<string | null>(localStorage.getItem('auth'));
  auth = this._auth.asReadonly();

  // role
  private _role = signal<'CLIENT' | 'EMPLOYEE' | 'ADMIN' | null>(null);
  role = this._role.asReadonly();

  constructor(private http: HttpClient) {}

  /* ================= SESIÓN ================= */

  login(authHeader: string) {
    localStorage.setItem('auth', authHeader);
    this._auth.set(authHeader);
  }

  logout() {
    localStorage.removeItem('auth');
    this._auth.set(null);
    this._role.set(null);
  }

  getAuthHeader(): string | null {
    return this._auth();
  }

  isLoggedIn(): boolean {
    return !!this._auth();
  }

  getRole() {
    return this._role();
  }

  loadMe() {
    const auth = this._auth();
    if (!auth || this._role()) return;

    this.http.get<any>(`${this.userApi}/me`).subscribe({
      next: (res) => {
        this._role.set(res.role);
      },
      error: (err) => {
        console.warn('Error loading profile', err.status);
      },
    });
  }

  /* ================= PASSWORD RESET ================= */

  verifyCode(email: string, code: string) {
    return this.http.post(`${this.authApi}/verify-code`, { email, code });
  }

  resetPassword(email: string, code: string, newPassword: string) {
    return this.http.post(`${this.authApi}/reset-password`, {
      email,
      code,
      newPassword,
    });
  }
}
