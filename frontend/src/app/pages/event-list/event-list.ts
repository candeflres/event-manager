import { Component, OnInit } from '@angular/core';
import { EventResponse } from '../../model/event-response';
import { EventService } from '../../services/event-service';
import { CommonModule } from '@angular/common';
import { Page } from '../../model/page';
@Component({
  selector: 'app-event-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './event-list.html',
  styleUrl: './event-list.css',
})
export class EventList implements OnInit {
  eventos: EventResponse[] = [];

  constructor(private eventService: EventService) {}
  ngOnInit(): void {
    this.eventService.getEventList(0, 9).subscribe({
      next: (page) => {
        this.eventos = page.content;
      },
      error: (err) => {
        console.error('STATUS', err.status);
        console.error('BODY', err.error);
      },
    });
  }
}
