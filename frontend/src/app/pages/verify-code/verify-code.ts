import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-verify-code',
  imports: [CommonModule, RouterModule],
  templateUrl: './verify-code.html',
  styleUrl: './verify-code.css',
})
export class VerifyCode {}
