import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BotResponse } from '../model/bot-response.model';
@Injectable({ providedIn: 'root' })
export class BotService {
  private apiUrl = 'http://localhost:8080/api/bot';

  constructor(private http: HttpClient) {}

  startBot() {
    return this.http.get<BotResponse>(`${this.apiUrl}/start`);
  }

  startLoggedBot() {
    const headers = this.buildAuthHeaders();

    return this.http.get<BotResponse>(`${this.apiUrl}/logged`, { headers });
  }

  sendAction(action: string, value?: string) {
    const headers = this.buildAuthHeaders();

    return this.http.post<BotResponse>(`${this.apiUrl}/action`, { action, value }, { headers });
  }

  private buildAuthHeaders(): HttpHeaders {
    const auth = localStorage.getItem('auth');

    return auth ? new HttpHeaders({ Authorization: 'Basic ' + auth }) : new HttpHeaders();
  }
}
