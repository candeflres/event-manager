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

  private isMobile = window.innerWidth <= 768;

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
        labels: ['Pendientes', 'Aprobados', 'Cancelados'],
        datasets: [
          {
            label: 'Eventos',
            data: [stats.pendingEvents, stats.approvedEvents, stats.cancelledEvents],
            backgroundColor: 'rgba(205, 162, 177, 0.85)',
            barThickness: this.isMobile ? 24 : 36,
          },
        ],
      },
      options: this.getChartOptions(),
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
            backgroundColor: 'rgba(205, 162, 177, 0.85)',
            barThickness: this.isMobile ? 24 : 36,
          },
        ],
      },
      options: this.getChartOptions(),
    });
  }

  // =====================
  // CHART OPTIONS (RESPONSIVE)
  // =====================
  private getChartOptions() {
    return {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: false },
        tooltip: {
          backgroundColor: '#351923',
          titleColor: '#f1e4e9',
          bodyColor: '#f1e4e9',
          borderColor: '#cda2b1',
          borderWidth: 1,
        },
      },
      scales: {
        x: {
          ticks: {
            color: '#f1e4e9',
            font: {
              size: this.isMobile ? 10 : 12,
              weight: 500,
            },
          },
          grid: {
            display: !this.isMobile,
            color: 'rgba(241, 228, 233, 0.15)',
          },
        },
        y: {
          ticks: {
            color: '#f1e4e9',
            font: {
              size: this.isMobile ? 10 : 12,
            },
          },
          grid: {
            display: !this.isMobile,
            color: 'rgba(241, 228, 233, 0.1)',
          },
        },
      },
    };
  }
  goBack() {
    window.location.href = 'http://localhost:4200/home-logged';
  }
}
