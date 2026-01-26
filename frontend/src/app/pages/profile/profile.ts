import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user-service';
import { AuthService } from '../../services/auth-service';
import { UserProfile } from '../../model/userProfile';
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.html',
  styleUrls: ['./profile.css'],
})
export class ProfilePage implements OnInit {
  user: UserProfile | null = null;
  loading = true;

  isClientUser = false;
  canSeeUserRole = false;
  editMode = false;

  profileForm = {
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
  };

  passwordForm = {
    currentPassword: '',
    newPassword: '',
  };

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  enableEdit(): void {
    this.editMode = true;
  }

  loadProfile(): void {
    this.userService.getMyProfile().subscribe({
      next: (data) => {
        console.log('PERFIL DATA:', data);

        this.user = data;
        this.isClientUser = data.role === 'CLIENT';
        this.canSeeUserRole = data.role === 'EMPLOYEE' || data.role === 'ADMIN';

        this.profileForm = {
          firstName: data.firstName,
          lastName: data.lastName,
          email: data.email,
          phone: data.phone,
        };

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

  goHome(): void {
    this.router.navigate(['/home-logged']);
  }

  changePassword(): void {
    this.userService.changeMyPassword(this.passwordForm).subscribe({
      next: () => {
        alert('Contraseña actualizada');
        this.passwordForm = { currentPassword: '', newPassword: '' };
      },
      error: (err) => {
        alert(err.error?.message || 'Error al cambiar la contraseña');
      },
    });
  }

  cancelEdit(): void {
    if (!this.user) return;

    this.profileForm = {
      firstName: this.user.firstName,
      lastName: this.user.lastName,
      email: this.user.email,
      phone: this.user.phone,
    };

    this.editMode = false;
  }

  saveProfile(): void {
    this.userService.updateMyProfile(this.profileForm).subscribe({
      next: (updated) => {
        this.user = updated;
        this.editMode = false;
        alert('Perfil actualizado correctamente');
        this.cdr.detectChanges();
      },
      error: (err) => {
        alert(err.error?.message || 'No se pudo actualizar el perfil');
      },
    });
  }

  handleBack(): void {
    if (this.editMode) {
      this.editMode = false;

      if (this.user) {
        this.profileForm = {
          firstName: this.user.firstName,
          lastName: this.user.lastName,
          email: this.user.email,
          phone: this.user.phone,
        };
      }
    } else {
      this.goHome();
    }
  }
}
