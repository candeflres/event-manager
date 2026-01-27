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
  // CLIENT
  // =====================

  getAvailable(): Observable<ElementResponse[]> {
    return this.http.get<ElementResponse[]>(`${this.baseUrl}/available`);
  }

  getById(id: number): Observable<ElementResponse> {
    const auth = localStorage.getItem('auth');

    return this.http.get<ElementResponse>(`${this.baseUrl}/${id}`, {
      headers: new HttpHeaders({
        Authorization: 'Basic ' + auth,
      }),
    });
  }

  // =====================
  // EMPLOYEE / ADMIN
  // =====================

  getAll(): Observable<ElementResponse[]> {
    const auth = localStorage.getItem('auth');

    return this.http.get<ElementResponse[]>(this.baseUrl, {
      headers: new HttpHeaders({
        Authorization: 'Basic ' + auth,
      }),
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
    const auth = localStorage.getItem('auth');

    return this.http.put<ElementResponse>(`${this.baseUrl}/${id}`, payload, {
      headers: new HttpHeaders({
        Authorization: 'Basic ' + auth,
      }),
    });
  }
  getByIdForManagement(id: number): Observable<ElementResponse> {
    const auth = localStorage.getItem('auth');

    return this.http.get<ElementResponse>(`${this.baseUrl}/${id}/manage`, {
      headers: {
        Authorization: 'Basic ' + auth,
      },
    });
  }
}
