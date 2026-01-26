import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { EMPTY, Observable } from 'rxjs';
import { EventResponse } from '../model/event-response';
import { Page } from '../model/page';
import { EventStatus } from '../model/event-status';
@Injectable({
  providedIn: 'root',
})
export class EventService {
  private apiUrl = 'http://localhost:8080/api/events';
  private elementsUrl = 'http://localhost:8080/api/elements';

  constructor(private http: HttpClient) {}

  getEventList(page: number, size: number) {
    const auth = localStorage.getItem('auth');
    console.log('AUTH HEADER', auth);

    if (!auth) {
      console.warn('No auth header');
      return EMPTY;
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
  getEventDetail(id: number) {
    const auth = localStorage.getItem('auth');

    const headers = new HttpHeaders({
      Authorization: 'Basic ' + auth,
    });

    return this.http.get<EventResponse>(`${this.apiUrl}/${id}`, { headers });
  }

  updateEvent(eventId: number, payload: any) {
    const auth = localStorage.getItem('auth');

    const headers = {
      Authorization: 'Basic ' + auth,
    };

    return this.http.put<EventResponse>(`${this.apiUrl}/${eventId}`, payload, { headers });
  }

  updateEventStatus(eventId: number, status: EventStatus): Observable<EventResponse> {
    const auth = localStorage.getItem('auth');

    return this.http.put<EventResponse>(
      `/api/events/${eventId}/status`,
      { status },
      {
        headers: {
          Authorization: 'Basic ' + auth,
        },
      },
    );
  }

  getPublicEvent(id: number) {
    return this.http.get<EventResponse>(`http://localhost:8080/api/public/events/${id}`);
  }
  getFilteredEvents(
    page: number,
    size: number,
    status?: 'PENDING' | 'APPROVED' | 'REJECTED' | 'COMPLETED',
    order: 'asc' | 'desc' = 'asc',
  ) {
    const auth = localStorage.getItem('auth');

    if (!auth) {
      console.warn('No auth header');
      return EMPTY;
    }

    const headers = new HttpHeaders({
      Authorization: 'Basic ' + auth,
    });

    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('order', order);

    if (status) {
      params = params.set('status', status);
    }

    return this.http.get<any>(`${this.apiUrl}/filter`, {
      headers,
      params,
    });
  }
}
