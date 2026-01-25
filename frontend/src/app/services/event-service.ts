import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EventResponse } from '../model/event-response';
import { Page } from '../model/page';

@Injectable({
  providedIn: 'root',
})
export class EventService {
  private apiUrl = 'http://localhost:8080/api/events';
  private elementsUrl = 'http://localhost:8080/api/elements';

  constructor(private http: HttpClient) {}

  getEventList(page: number, size: number) {
    const auth = localStorage.getItem('auth');
    console.log('AUTH HEADER 👉', auth);

    if (!auth) {
      throw new Error('No auth header');
    }

    const headers = new HttpHeaders({
      Authorization: 'Basic ' + auth,
    });

    const params = new HttpParams().set('page', page.toString()).set('size', size.toString());

    return this.http.get<any>(this.apiUrl, { headers, params });
  }

  getElementsWithOptions() {
    const auth = localStorage.getItem('auth');
    const headers = new HttpHeaders({
      Authorization: 'Basic ' + auth,
    });

    return this.http.get<any[]>(this.elementsUrl, { headers });
  }

  createEvent(payload: any) {
    const auth = localStorage.getItem('auth');
    const headers = new HttpHeaders({
      Authorization: 'Basic ' + auth,
      'Content-Type': 'application/json',
    });

    return this.http.post(this.apiUrl, payload, { headers });
  }
}
