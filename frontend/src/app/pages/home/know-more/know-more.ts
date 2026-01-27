import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-know-more',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './know-more.html',
  styleUrl: './know-more.css',
})
export class KnowMore {}
