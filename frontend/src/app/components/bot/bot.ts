import {Component, OnInit, OnDestroy, ChangeDetectorRef, signal, effect} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';

import { BotService } from '../../services/bot-service';
import { AuthService } from '../../services/auth-service';
import { BotOption } from '../../model/bot-option.model';
import { BotResponse } from '../../model/bot-response.model';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-bot',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './bot.html',
  styleUrls: ['./bot.css'],
})
export class BotComponent {

  messages = signal<string[]>([]);
  options = signal<BotOption[]>([]);
  isOpen = signal(false);
  pendingAction = signal<string | null>(null);
  inputValue = signal('');

  constructor(
    private botService: BotService,
    private authService: AuthService
  ) {

    // 🔥 reacciona AUTOMÁTICAMENTE a login / logout
    effect(() => {
      this.authService.auth(); // dependencia
      if (this.isOpen()) {
        this.loadBot();
      }
    });
  }

  openBot() {
    this.isOpen.set(true);
  }

  closeBot() {
    this.isOpen.set(false);
    this.resetBot();
  }

  private loadBot() {
    this.resetBot();

    const bot$ = this.authService.isLoggedIn()
      ? this.botService.startLoggedBot()
      : this.botService.startBot();

    bot$.subscribe(resp => this.handleBotResponse(resp));
  }

  handleOption(option: BotOption) {
    this.addMessage(`> ${option.text}`);
    this.options.set([]);
    this.pendingAction.set(null);

    if (option.action === 'BACK') {
      this.loadBot();
      return;
    }

    this.botService.sendAction(option.action)
      .subscribe(resp => this.handleBotResponse(resp));
  }

  sendInput() {
    if (!this.pendingAction() || !this.inputValue()) return;

    this.addMessage(`> ${this.inputValue()}`);

    this.botService
      .sendAction(this.pendingAction()!, this.inputValue())
      .subscribe((resp: BotResponse) => {
        this.handleBotResponse(resp);
        this.inputValue.set('');
      });
  }

  private handleBotResponse(resp: BotResponse) {
    this.addMessage(resp.message);
    this.options.set(resp.options ?? []);
    this.pendingAction.set(resp.nextAction ?? null);
  }

  private addMessage(message: string) {
    this.messages.update(m => [...m, message]);
  }

  private resetBot() {
    this.messages.set([]);
    this.options.set([]);
    this.pendingAction.set(null);
    this.inputValue.set('');
  }
}
