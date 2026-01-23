import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EventResponse } from '../model/event-response';

@Injectable({
  providedIn: 'root',
})
export class EventService {
  private apiUrl = 'http://localhost:8080/api/eventos';

  constructor(private http: HttpClient) {}

  getEventList(): Observable<EventResponse[]> {
    return this.http.get<EventResponse[]>(`${this.apiUrl}/event-list`);
  }
}
