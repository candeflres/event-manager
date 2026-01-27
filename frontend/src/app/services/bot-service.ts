import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BotResponse} from '../model/bot-response.model';
import {BotActionRequest} from '../model/bot-action.model';

@Injectable({
  providedIn: 'root'
})
export class BotService{

  private API = 'http://localhost:8080/api/bot';

  constructor(private http: HttpClient) {}

  startBot(){
    return this.http.get<BotResponse>(`${this.API}/start`);
  }
  startLoggedBot() {
    return this.http.get<BotResponse>(`${this.API}/logged`);
  }

  sendAction(request: BotActionRequest) {
    return this.http.post<BotResponse>(`${this.API}/action`, request);
  }
}
