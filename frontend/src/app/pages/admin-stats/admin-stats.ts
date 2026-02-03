import { Component, OnInit, ChangeDetectorRef, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Chart, registerables } from 'chart.js';

import { AdminService } from '../../services/admin-service';
import { AdminStatsDTO } from '../../model/admin-stats-dto.model';

Chart.register(...registerables);

@Component({
  selector: 'app-admin-stats',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-stats.html',
  styleUrls: ['./admin-stats.css'],
})
export class AdminStats implements OnInit {
  stats: AdminStatsDTO | null = null;
  loading = true;

  @ViewChild('eventsChart') eventsChart!: ElementRef<HTMLCanvasElement>;
  @ViewChild('usersChart') usersChart!: ElementRef<HTMLCanvasElement>;

  private eventsBarChart?: Chart;
  private usersBarChart?: Chart;

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

        this.buildEventsBarChart(res);
        this.buildUsersBarChart(res);
      },
    });
  }

  private buildEventsBarChart(stats: AdminStatsDTO): void {
    this.eventsBarChart?.destroy();

    this.eventsBarChart = new Chart(this.eventsChart.nativeElement, {
      type: 'bar',
      data: {
        labels: ['Pendientes', 'Aprobados', 'Cancelados', 'Completados'],
        datasets: [
          {
            label: 'Eventos',
            data: [
              stats.pendingEvents,
              stats.approvedEvents,
              stats.cancelledEvents,
              stats.completedEvents,
            ],
            backgroundColor: '#351725',
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
        },
      },
    });
  }

  private buildUsersBarChart(stats: AdminStatsDTO): void {
    this.usersBarChart?.destroy();

    this.usersBarChart = new Chart(this.usersChart.nativeElement, {
      type: 'bar',
      data: {
        labels: ['Administradores', 'Empleados', 'Clientes'],
        datasets: [
          {
            label: 'Usuarios',
            data: [stats.adminUsers, stats.employeeUsers, stats.clientUsers],
            backgroundColor: '#351725',
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
        },
      },
    });
  }
}
