import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private authSubject = new BehaviorSubject<string | null>(localStorage.getItem('auth'));

  auth$ = this.authSubject.asObservable();

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
}
