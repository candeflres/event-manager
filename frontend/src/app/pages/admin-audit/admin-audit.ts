import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
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

  orderSelected: 'asc' | 'desc' = 'desc';

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

    this.http
      .get<any[]>('/api/audit', {
        headers: {
          Authorization: 'Basic ' + auth,
        },
        params: {
          order: this.orderSelected,
        },
      })
      .subscribe({
        next: (res) => {
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
}
