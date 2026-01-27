import {Component, OnInit} from '@angular/core';
import {BotService} from '../../services/bot-service';
import {BotOption} from '../../model/bot-option.model';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-bot',
  templateUrl: './bot.html',
  standalone: true,
  imports: [CommonModule],
  providers: [BotService]
})
export class BotComponent {

  messages: string[] = [];
  options: BotOption[] = [];

  constructor(private botService: BotService) { }

  ngOnInit() {
    this.botService.startBot().subscribe(resp => {
      console.log(resp); // 🔹 para debug
      this.addMessage(resp.message);
      this.options = resp.options;
    });
  }

  startBot() {
    this.botService.startBot().subscribe(resp => {
      this.addMessage(resp.message);
      this.options = resp.options;
    });
  }

  handleOption(option: BotOption) {
    this.addMessage(`> ${option.text}`);

    if (option.action === 'BACK') {
      this.startBot();
      return;
    }

    this.botService.sendAction(option.action).subscribe(resp => {
      this.addMessage(resp.message);
      this.options = resp.options;
    });
  }

  addMessage(message: string) {
    this.messages.push(message);
  }
}
