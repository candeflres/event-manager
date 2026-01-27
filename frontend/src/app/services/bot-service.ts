import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BotResponse} from '../model/bot-response.model';
import {BotActionRequest} from '../model/bot-action.model';

@Injectable({
  providedIn: 'root'
})
export class BotService{

  private apiUrl = 'http://localhost:8080/api/bot';

  constructor(private http: HttpClient) {}

  startBot(){
    return this.http.get<BotResponse>(`${this.apiUrl}/start`);
  }
  startLoggedBot() {
    return this.http.get<BotResponse>(`${this.apiUrl}/logged`);
  }

  sendAction(action: string, value?: string) {
    return this.http.post<BotResponse>(`${this.apiUrl}/action`, {action, value});
  }
}
