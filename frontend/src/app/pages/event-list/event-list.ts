import { Component, OnInit } from '@angular/core';
import { EventResponse } from '../../model/event-response';
import { EventService } from '../../services/event-service';
import { CommonModule } from '@angular/common';
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
    this.eventService.getEventList().subscribe({
      next: (data) => (this.eventos = data),
      error: (err) => console.error(err),
    });
  }
}
