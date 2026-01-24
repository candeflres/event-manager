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

  constructor(private http: HttpClient) {}

  getEventList(page: number, size: number) {
    const auth = localStorage.getItem('auth');

    const headers = new HttpHeaders({
      Authorization: 'Basic ' + auth,
    });

    const params = new HttpParams().set('page', page).set('size', size);

    return this.http.get<any>(`${this.apiUrl}`, { headers, params });
  }
}
