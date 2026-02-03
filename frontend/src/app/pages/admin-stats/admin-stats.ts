import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from '../../services/admin-service';
import { AdminStatsdto } from '../../model/admin-stats-dto.model';
@Component({
  selector: 'app-admin-stats',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-stats.html',
  styleUrls: ['./admin-stats.css'],
})
export class AdminStats implements OnInit {
  stats: AdminStatsdto | null = null;
  loading = true;

  constructor(
    private adminService: AdminService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.adminService.getAdminStats().subscribe({
      next: (res) => {
        this.stats = res;
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
