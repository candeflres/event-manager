import {Injectable, signal} from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AuthService {
  //private authSubject = new BehaviorSubject<string | null>(localStorage.getItem('auth'));

  //auth$ = this.authSubject.asObservable();

  private _auth = signal<string | null>(localStorage.getItem('auth'));
  auth = this._auth.asReadonly();
  private api = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  /* ================= SESIÓN ================= */

  login(authHeader: string) {
    localStorage.setItem('auth', authHeader);
    this._auth.set(authHeader);
  }

  logout() {
    localStorage.removeItem('auth');
    this._auth.set(null);
  }

  getAuthHeader(): string | null {
    return this._auth();
  }

  isLoggedIn(): boolean {
    return !!this._auth();
  }

  /* ================= PASSWORD RESET ================= */

  verifyCode(email: string, code: string) {
    return this.http.post(`${this.api}/verify-code`, { email, code });
  }

  resetPassword(email: string, code: string, newPassword: string) {
    return this.http.post(`${this.api}/reset-password`, {
      email,
      code,
      newPassword,
    });
  }
}
