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
  private chart?: Chart;

  @ViewChild('usersChart') usersChart!: ElementRef<HTMLCanvasElement>;
  private usersPieChart?: Chart;

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
        this.buildUsersPieChart(res);
      },
    });
  }

  private buildUsersPieChart(stats: AdminStatsDTO): void {
    if (this.usersPieChart) {
      this.usersPieChart.destroy();
    }

    this.usersPieChart = new Chart(this.usersChart.nativeElement, {
      type: 'pie',
      data: {
        labels: ['Administradores', 'Empleados', 'Clientes'],
        datasets: [
          {
            data: [stats.adminUsers, stats.employeeUsers, stats.clientUsers],
            backgroundColor: ['#7e57c2', '#42a5f5', '#66bb6a'],
          },
        ],
      },
      options: {
        responsive: true,
        plugins: {
          legend: { position: 'bottom' },
        },
      },
    });
  }

  private buildEventsBarChart(stats: AdminStatsDTO): void {
    if (this.chart) {
      this.chart.destroy();
    }

    this.chart = new Chart(this.eventsChart.nativeElement, {
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
            backgroundColor: ['#f4c542', '#4caf50', '#e57373', '#90a4ae'],
          },
        ],
      },
      options: {
        responsive: true,
        plugins: {
          legend: { display: false },
        },
      },
    });
  }
}
