import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OptionResponse } from '../model/option-response';
import { OptionUpdateRequest } from '../model/option-update-request';
@Injectable({
  providedIn: 'root',
})
export class OptionService {
  private baseUrl = 'http://localhost:8080/api/options';

  constructor(private http: HttpClient) {}

  create(payload: {
    name: string;
    description?: string;
    price: number;
    available: boolean;
    elementId: number;
  }) {
    const auth = localStorage.getItem('auth');

    return this.http.post<OptionResponse>(this.baseUrl, payload, {
      headers: {
        Authorization: 'Basic ' + auth,
      },
    });
  }

  update(id: number, payload: OptionUpdateRequest) {
    const auth = localStorage.getItem('auth');

    return this.http.put<OptionResponse>(`${this.baseUrl}/${id}`, payload, {
      headers: {
        Authorization: 'Basic ' + auth,
      },
    });
  }

  delete(id: number) {
    const auth = localStorage.getItem('auth');

    return this.http.delete<void>(`${this.baseUrl}/${id}`, {
      headers: {
        Authorization: 'Basic ' + auth,
      },
    });
  }
}
