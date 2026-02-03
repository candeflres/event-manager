import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AdminStatsdto } from '../model/admin-stats-dto.model';

@Injectable({ providedIn: 'root' })
export class AdminService {
  constructor(private http: HttpClient) {}

  getAdminStats() {
    const auth = localStorage.getItem('auth');

    return this.http.get<AdminStatsdto>('http://localhost:8080/api/admin/stats', {
      headers: { Authorization: 'Basic ' + auth },
    });
  }
}
