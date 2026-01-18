import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from './components/header/header';
import { Home } from './components/home/home';
import { KnowMore } from './components/know-more/know-more';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Home, KnowMore],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
}
