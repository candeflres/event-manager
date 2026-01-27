import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ElementResponse } from '../model/element-response';
@Injectable({
  providedIn: 'root',
})
export class ElementService {
  private baseUrl = 'http://localhost:8080/api/elements';

  constructor(private http: HttpClient) {}

  getAvailable(): Observable<ElementResponse[]> {
    return this.http.get<ElementResponse[]>(`${this.baseUrl}/available`);
  }

  getById(id: number): Observable<ElementResponse> {
    return this.http.get<ElementResponse>(`${this.baseUrl}/${id}`);
  }
}
