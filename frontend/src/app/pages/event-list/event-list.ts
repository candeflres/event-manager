import { Component, OnInit } from '@angular/core';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { EventService } from '../../services/event-service';
import { EventResponse } from '../../model/event-response';
import { Router, RouterLink, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-event-list',
  standalone: true,
  imports: [CommonModule, NgFor, NgIf, RouterLink, FormsModule],
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

  statusSelected: '' | 'PENDING' | 'APPROVED' | 'REJECTED' | 'COMPLETED' = '';
  orderSelected: 'asc' | 'desc' = 'asc';

  constructor(
    private eventService: EventService,
    private cdr: ChangeDetectorRef,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents(): void {
    this.eventService
      .getFilteredEvents(this.page, this.size, this.statusSelected || undefined, this.orderSelected)
      .subscribe({
        next: (res) => {
          this.eventos = res.content;

          this.totalPages = res.totalPages;
          this.isFirst = this.page === 0;
          this.isLast = this.page >= this.totalPages - 1;

          this.cdr.detectChanges();
        },
        error: (err) => console.error(err),
      });
  }

  onFilterChange(): void {
    this.page = 0;
    this.loadEvents();
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

  trackById(index: number, evento: EventResponse) {
    return evento.id;
  }
}
