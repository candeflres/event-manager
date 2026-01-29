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

  filters = {
    entity: undefined as 'EVENT' | 'ELEMENT' | 'OPTION' | 'USER' | undefined,
    userId: undefined as number | undefined,
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
    };

    if (this.filters.entity) params.entity = this.filters.entity;
    if (this.filters.userId) params.userId = this.filters.userId;
    if (this.filters.userEmail?.trim()) params.userEmail = this.filters.userEmail.trim();
    if (this.filters.eventId) params.eventId = this.filters.eventId;

    this.http
      .get<any[]>('/api/audit', {
        headers: {
          Authorization: 'Basic ' + auth,
        },
        params,
      })
      .subscribe({
        next: (res: any[]) => {
          this.logs = res;
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.loading = false;
          this.cdr.detectChanges();
        },
      });
  }

  clearFilters(): void {
    this.filters = {
      entity: undefined,
      userId: undefined,
      userEmail: '',
      eventId: undefined,
      order: 'desc',
    };

    this.load();
  }
}
