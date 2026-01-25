import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user-service';
import { AuthService } from '../../services/auth-service';
import { UserProfile } from '../../model/userProfile';
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.html',
  styleUrls: ['./profile.css'],
})
export class ProfilePage implements OnInit {
  user: UserProfile | null = null;
  loading = true;

  isClientUser = false;
  canSeeUserRole = false;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.userService.getMyProfile().subscribe({
      next: (data) => {
        console.log('PERFIL DATA:', data);

        this.user = data;
        this.isClientUser = data.role === 'CLIENT';
        this.canSeeUserRole = data.role === 'EMPLOYEE' || data.role === 'ADMIN';

        this.loading = false;

        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR PERFIL:', err);
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  deleteMyAccount(): void {
    if (!confirm('¿Estás seguro de que querés dar de baja tu cuenta?')) return;

    this.userService.deleteMyAccount().subscribe({
      next: () => {
        alert('Tu cuenta fue dada de baja');

        this.authService.logout();
        this.router.navigate(['/login']);
      },
      error: () => alert('No se pudo dar de baja la cuenta'),
    });
  }
}
