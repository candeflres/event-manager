import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-audit.html',
  styleUrls: ['./admin-audit.css'],
})
export class AdminAudit implements OnInit {
  logs: any[] = [];
  loading = true;
  page = 0;
  size = 10;
  totalPages = 0;

  filters = {
    entity: undefined as 'EVENT' | 'ELEMENT' | 'OPTION' | 'USER' | undefined,
    userEmail: '',
    eventId: undefined as number | undefined,
    order: 'desc' as 'asc' | 'desc',
  };

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;

    const auth = localStorage.getItem('auth');

    const params: any = {
      order: this.filters.order,
      page: this.page,
      size: this.size,
    };

    if (this.filters.entity) params.entity = this.filters.entity;
    if (this.filters.userEmail?.trim()) params.userEmail = this.filters.userEmail.trim();
    if (this.filters.eventId) params.eventId = this.filters.eventId;

    this.http
      .get<any>('/api/audit', {
        headers: { Authorization: 'Basic ' + auth },
        params,
      })
      .subscribe({
        next: (res) => {
          this.logs = res.content;
          this.totalPages = res.totalPages;
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.loading = false;
          this.cdr.detectChanges();
        },
      });
  }

  applyFilters(): void {
    this.page = 0;
    this.load();
  }

  nextPage() {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.load();
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  prevPage() {
    if (this.page > 0) {
      this.page--;
      this.load();
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  clearFilters(): void {
    this.filters = {
      entity: undefined,
      userEmail: '',
      eventId: undefined,
      order: 'desc',
    };
    this.page = 0;
    this.load();
  }
}
