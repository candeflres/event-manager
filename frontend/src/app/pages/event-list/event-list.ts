import { Component, OnInit } from '@angular/core';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { EventService } from '../../services/event-service';
import { EventResponse } from '../../model/event-response';
import { Router, RouterLink, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-event-list',
  standalone: true,
  imports: [CommonModule, NgFor, NgIf],
  templateUrl: './event-list.html',
  styleUrls: ['./event-list.css'],
})
export class EventList implements OnInit {
  eventos: EventResponse[] = [];

  page = 0;
  size = 9;
  totalPages = 0;
  isFirst = true;
  isLast = false;

  constructor(
    private eventService: EventService,
    private cdr: ChangeDetectorRef,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents(): void {
    this.eventService.getEventList(this.page, this.size).subscribe({
      next: (res) => {
        this.eventos = res.content;

        this.totalPages = res.totalPages;
        this.isFirst = this.page === 0;
        this.isLast = this.page >= this.totalPages - 1;

        console.log('PAGE:', this.page);
        console.log('TOTAL PAGES:', this.totalPages);
        console.log('isFirst:', this.isFirst);
        console.log('isLast:', this.isLast);

        this.cdr.detectChanges();
      },
      error: (err) => console.error(err),
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

  trackById(index: number, evento: any) {
    return evento.id;
  }
}
