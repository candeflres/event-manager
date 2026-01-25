import { Component, OnInit } from '@angular/core';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { EventService } from '../../services/event-service';
import { EventResponse } from '../../model/event-response';

@Component({
  selector: 'app-event-list',
  standalone: true,
  imports: [CommonModule, NgFor, NgIf],
  templateUrl: './event-list.html',
  styleUrl: './event-list.css',
})
export class EventList implements OnInit {
  eventos: EventResponse[] = [];

  page = 0;
  size = 9;
  totalPages = 0;
  isFirst = true;
  isLast = false;

  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents(): void {
    this.eventService.getEventList(this.page, this.size).subscribe({
      next: (res) => {
        this.eventos = [...res.content];
        this.totalPages = res.totalPages;
        this.isFirst = res.first;
        this.isLast = res.last;
      },
      error: (err) => {
        console.error('STATUS', err.status);
        console.error('BODY', err.error);
      },
    });
  }

  nextPage(): void {
    if (!this.isLast) {
      this.page++;
      this.loadEvents();
    }
  }

  prevPage(): void {
    if (!this.isFirst) {
      this.page--;
      this.loadEvents();
    }
  }
}
