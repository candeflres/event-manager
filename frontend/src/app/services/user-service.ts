import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NewUser, User } from '../model/user.model';
import { Observable } from 'rxjs';
import { UserProfile } from '../model/userProfile';
@Injectable({
  providedIn: 'root',
})
export class UserService {
  private urlApi = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  // ---------- PUBLIC / CLIENT ----------

  register(newUser: NewUser): Observable<User> {
    return this.http.post<User>(this.urlApi, newUser);
  }

  getMyProfile() {
    const auth = localStorage.getItem('auth');
    return this.http.get<UserProfile>(`${this.urlApi}/me`, {
      headers: { Authorization: 'Basic ' + auth },
    });
  }

  updateMyProfile(payload: { firstName: string; lastName: string; email: string; phone: string }) {
    const auth = localStorage.getItem('auth');
    return this.http.put<UserProfile>(`${this.urlApi}/me`, payload, {
      headers: { Authorization: 'Basic ' + auth },
    });
  }

  changeMyPassword(payload: { currentPassword: string; newPassword: string }) {
    const auth = localStorage.getItem('auth');
    return this.http.put<void>(`${this.urlApi}/me/password`, payload, {
      headers: { Authorization: 'Basic ' + auth },
    });
  }

  deleteMyAccount() {
    const auth = localStorage.getItem('auth');
    return this.http.put<void>(
      `${this.urlApi}/me/deactivate`,
      {},
      { headers: { Authorization: 'Basic ' + auth } },
    );
  }

  // ---------- ADMIN ----------

  getEmployees() {
    const auth = localStorage.getItem('auth');
    return this.http.get<UserProfile[]>(`${this.urlApi}/employees`, {
      headers: { Authorization: 'Basic ' + auth },
    });
  }

  getUserById(id: number) {
    const auth = localStorage.getItem('auth');
    return this.http.get<UserProfile>(`${this.urlApi}/${id}`, {
      headers: { Authorization: 'Basic ' + auth },
    });
  }

  createEmployee(payload: {
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    password: string;
  }) {
    const auth = localStorage.getItem('auth');
    return this.http.post(`${this.urlApi}/employees`, payload, {
      headers: { Authorization: 'Basic ' + auth },
    });
  }

  updateUserById(
    id: number,
    payload: {
      firstName: string;
      lastName: string;
      email: string;
      phone: string;
    },
  ) {
    const auth = localStorage.getItem('auth');
    return this.http.put<UserProfile>(`${this.urlApi}/${id}`, payload, {
      headers: { Authorization: 'Basic ' + auth },
    });
  }

  updateUserRole(userId: number, role: 'CLIENT' | 'EMPLOYEE' | 'ADMIN') {
    const auth = localStorage.getItem('auth');
    return this.http.put<UserProfile>(
      `${this.urlApi}/${userId}/role?role=${role}`,
      {},
      { headers: { Authorization: 'Basic ' + auth } },
    );
  }

  deactivateUser(userId: number) {
    const auth = localStorage.getItem('auth');
    return this.http.delete(`${this.urlApi}/${userId}`, {
      headers: { Authorization: 'Basic ' + auth },
    });
  }
}
