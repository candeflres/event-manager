import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
@Injectable({ providedIn: 'root' })
export class AuthService {
  private authSubject = new BehaviorSubject<string | null>(localStorage.getItem('auth'));

  auth$ = this.authSubject.asObservable();

  private api = 'http://localhost:8080/api/auth';

  login(auth: string) {
    localStorage.setItem('auth', auth);
    this.authSubject.next(auth);
  }

  logout() {
    localStorage.removeItem('auth');
    this.authSubject.next(null);
  }

  getAuthHeader(): string | null {
    return this.authSubject.value;
  }

  isLoggedIn(): boolean {
    return !!this.getAuthHeader();
  }

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

  constructor(private http: HttpClient) {}
}
