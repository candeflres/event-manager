import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from './components/header/header';
import { Footer } from './components/footer/footer';
import {BotComponent} from './components/bot/bot.component';
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Footer, BotComponent],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {}
