import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
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
  providers: [BotService],
  templateUrl: './bot.html',
  styleUrls: ['./bot.css'],
})
export class BotComponent implements OnInit, OnDestroy {
  messages: string[] = [];
  options: BotOption[] = [];

  isOpen = false;
  isLoggedIn = false;

  pendingAction?: string;
  inputValue = '';

  private authSub?: Subscription;
  private loadId = 0;

  constructor(
    private botService: BotService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.authSub = this.authService.auth$.subscribe((auth) => {
      const newStatus = !!auth;
      if (this.isLoggedIn !== newStatus) {
        this.isLoggedIn = newStatus;

        // si el bot está abierto, recargar
        if (this.isOpen) {
          this.loadBot();
        }
      }
    });

    this.isLoggedIn = this.authService.isLoggedIn();
  }

  ngOnDestroy(): void {
    this.authSub?.unsubscribe();
  }

  // =========================
  // OPEN / CLOSE
  // =========================
  openBot(): void {
    this.isOpen = true;

    if (this.messages.length === 0) {
      this.loadBot();
    }
  }

  closeBot(): void {
    this.isOpen = false;
    this.pendingAction = undefined;
    this.inputValue = '';
  }

  // =========================
  // BOT FLOW
  // =========================
  private loadBot(): void {
    const currentLoad = ++this.loadId;

    this.messages = [];
    this.options = [];
    this.pendingAction = undefined;
    this.inputValue = '';

    const bot$ = this.isLoggedIn ? this.botService.startLoggedBot() : this.botService.startBot();

    bot$.subscribe((resp) => {
      if (currentLoad === this.loadId) {
        this.handleBotResponse(resp);
      }
    });
  }

  handleOption(option: BotOption): void {
    this.addMessage(`> ${option.text}`);
    this.options = [];
    this.pendingAction = undefined;

    if (option.action === 'BACK') {
      this.loadBot();
      return;
    }

    this.botService.sendAction(option.action).subscribe((resp) => {
      this.handleBotResponse(resp);
    });
  }

  sendInput(): void {
    if (!this.pendingAction || !this.inputValue) return;

    this.addMessage(`> ${this.inputValue}`);

    this.botService
      .sendAction(this.pendingAction, this.inputValue)
      .subscribe((resp: BotResponse) => {
        this.handleBotResponse(resp);
        this.inputValue = '';
      });
  }
  private handleBotResponse(resp: BotResponse): void {
    this.addMessage(resp.message);
    this.options = resp.options ?? [];
    this.pendingAction = resp.nextAction;
    this.cdr.detectChanges();
  }

  private addMessage(message: string): void {
    this.messages.push(message);
  }
}
