import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuditService {
  private baseUrl = 'http://localhost:8080/api/audit';

  constructor(private http: HttpClient) {}

  getAuditLogs(filters: {
    entity?: 'EVENT' | 'ELEMENT' | 'OPTION' | 'USER';
    userId?: number;
    userEmail?: string;
    eventId?: number;
    order?: 'asc' | 'desc';
  }): Observable<any[]> {
    const auth = localStorage.getItem('auth');

    let params = new HttpParams();

    Object.entries(filters).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        params = params.set(key, value.toString());
      }
    });

    return this.http.get<any[]>(this.baseUrl, {
      headers: new HttpHeaders({
        Authorization: 'Basic ' + auth,
      }),
      params,
    });
  }
}
