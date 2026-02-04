import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ElementResponse } from '../model/element-response';

@Injectable({
  providedIn: 'root',
})
export class ElementService {
  private baseUrl = 'http://localhost:8080/api/elements';

  constructor(private http: HttpClient) {}

  // =====================
  // PUBLIC / LANDING
  // =====================

  getAvailable(): Observable<ElementResponse[]> {
    return this.http.get<ElementResponse[]>(`${this.baseUrl}/available`);
  }

  getPublicById(id: number): Observable<ElementResponse> {
    return this.http.get<ElementResponse>(`${this.baseUrl}/${id}`);
  }

  // =====================
  // EMPLOYEE / ADMIN
  // =====================

  getAll(): Observable<ElementResponse[]> {
    return this.http.get<ElementResponse[]>(this.baseUrl, {
      headers: this.authHeaders(),
    });
  }

  create(payload: { name: string; description: string }): Observable<ElementResponse> {
    return this.http.post<ElementResponse>(this.baseUrl, payload, {
      headers: this.authHeaders(),
    });
  }

  update(
    id: number,
    payload: {
      name: string;
      description: string;
      available: boolean;
    },
  ): Observable<ElementResponse> {
    return this.http.put<ElementResponse>(`${this.baseUrl}/${id}`, payload, {
      headers: this.authHeaders(),
    });
  }

  deactivate(id: number): Observable<void> {
    return this.http.put<void>(
      `${this.baseUrl}/${id}/deactivate`,
      {},
      {
        headers: this.authHeaders(),
      },
    );
  }

  getByIdForManagement(id: number): Observable<ElementResponse> {
    return this.http.get<ElementResponse>(`${this.baseUrl}/${id}/manage`, {
      headers: this.authHeaders(),
    });
  }

  // =====================
  // HELPERS
  // =====================

  private authHeaders(): HttpHeaders {
    const auth = localStorage.getItem('auth') ?? '';
    return new HttpHeaders({
      Authorization: 'Basic ' + auth,
    });
  }
}
