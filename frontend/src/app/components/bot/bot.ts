import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {BotService} from '../../services/bot-service';
import {BotOption} from '../../model/bot-option.model';
import {CommonModule} from '@angular/common';
import {Subscription} from 'rxjs';
import {AuthService} from '../../services/auth-service';

@Component({
  selector: 'app-bot',
  templateUrl: './bot.html',
  standalone: true,
  imports: [CommonModule],
  providers: [BotService]
})
export class BotComponent implements OnInit, OnDestroy {
  messages: string[] = [];
  options: BotOption[] = [];
  isLoggedIn = false;
  private loadId = 0;
  private authSub?: Subscription;

  constructor(
    private botService: BotService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.authSub = this.authService.auth$.subscribe(auth => {
      const newStatus = !!auth;
      if (this.isLoggedIn !== newStatus) {
        this.isLoggedIn = newStatus;
        this.resetAndReload();
      }
    });

    this.isLoggedIn = !!this.authService.getAuthHeader();
    this.loadBot();
  }

  ngOnDestroy(): void {
    this.authSub?.unsubscribe();
  }

  private resetAndReload(): void {
    this.messages = [];
    this.options = [];
    this.cdr.detectChanges();
    this.loadBot();
  }

  loadBot(): void {
    const currentLoad = ++this.loadId;
    this.messages = [];

    const bot$ = this.isLoggedIn
      ? this.botService.startLoggedBot()
      : this.botService.startBot();

    bot$.subscribe(resp => {
      if (currentLoad === this.loadId) {
        this.addMessage(resp.message);
        this.options = resp.options;
        this.cdr.detectChanges();
      }
    });
  }

  handleOption(option: BotOption): void {
    this.addMessage(`> ${option.text}`);
    this.options = [];

    if (option.action === 'BACK') {
      this.loadBot();
      return;
    }

    this.botService.sendAction(option.action).subscribe(resp => {
      this.addMessage(resp.message);
      this.options = resp.options;
      this.cdr.detectChanges();
    });
  }

  addMessage(message: string): void {
    this.messages.push(message);
  }
}
