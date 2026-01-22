import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home-logged',
  imports: [CommonModule, RouterModule],
  templateUrl: './home-logged.html',
  styleUrl: './home-logged.css',
})
export class HomeLogged {}
