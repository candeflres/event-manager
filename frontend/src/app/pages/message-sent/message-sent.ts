import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-message-sent',
  imports: [RouterLink, CommonModule],
  templateUrl: './message-sent.html',
  styleUrl: './message-sent.css',
})
export class MessageSent {}
