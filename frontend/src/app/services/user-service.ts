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

  register(newUser: NewUser): Observable<User> {
    console.log('entro');
    return this.http.post<User>(this.urlApi, newUser);
  }

  getMyProfile() {
    const auth = localStorage.getItem('auth');

    return this.http.get<UserProfile>('/api/users/me', {
      headers: {
        Authorization: 'Basic ' + auth,
      },
    });
  }

  deleteMyAccount() {
    const auth = localStorage.getItem('auth');

    return this.http.put<void>(
      '/api/users/me/deactivate',
      {},
      {
        headers: {
          Authorization: 'Basic ' + auth,
        },
      },
    );
  }

  updateMyProfile(payload: { firstName: string; lastName: string; email: string; phone: string }) {
    const auth = localStorage.getItem('auth');

    return this.http.put<UserProfile>('/api/users/me', payload, {
      headers: {
        Authorization: 'Basic ' + auth,
      },
    });
  }

  changeMyPassword(payload: { currentPassword: string; newPassword: string }) {
    const auth = localStorage.getItem('auth');

    return this.http.put<void>('/api/users/me/password', payload, {
      headers: {
        Authorization: 'Basic ' + auth,
      },
    });
  }
}
