import { Component } from '@angular/core';
import { KnowMore } from './know-more/know-more';
import { Hero } from './hero/hero';
import { PastEvents } from './past-events/past-events';
import { Footer } from '../../components/footer/footer';

@Component({
  selector: 'app-home',
  imports: [KnowMore, Hero, PastEvents, Footer],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {}
