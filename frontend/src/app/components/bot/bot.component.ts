import {Component, OnInit} from '@angular/core';
import {BotOption} from '../../model/bot-option.model';
import {BotService} from '../../services/bot-service';
import {AuthService} from '../../services/auth-service';
import {BotResponse} from '../../model/bot-response.model';

@Component({
  selector: 'app-bot',
  templateUrl: './bot.component.html',
  styleUrls: ['./bot.component.css']
})
export class BotComponent implements OnInit {

  message = '';
  options: BotOption[] = [];

  constructor(
    private botService: BotService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.loadLoggedBot();
    } else {
      this.loadPublicBot();
    }
  }

  loadPublicBot() {
    this.botService.startBot().subscribe(res => this.updateBot(res));
  }

  loadLoggedBot() {
    this.botService.startLoggedBot().subscribe(res => this.updateBot(res));
  }

  onOptionClick(option: BotOption) {
    this.botService.sendAction({
      action: option.action
    }).subscribe(res => this.updateBot(res));
  }

  private updateBot(res: BotResponse) {
    this.message = res.message;
    this.options = res.options;
  }
}
