import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NewUser, User } from '../model/user.model';
import { Observable } from 'rxjs';

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
}
