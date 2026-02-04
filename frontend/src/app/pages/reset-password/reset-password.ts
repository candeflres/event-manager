import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.css',
})
export class ResetPassword {
  email = '';
  code = '';

  newPassword = '';
  confirmPassword = '';
  loading = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
  ) {
    this.email = this.route.snapshot.queryParamMap.get('email') || '';
    this.code = this.route.snapshot.queryParamMap.get('code') || '';
  }

  resetPassword() {
    if (!this.newPassword || !this.confirmPassword) {
      alert('Completá todos los campos');
      return;
    }

    if (this.newPassword !== this.confirmPassword) {
      alert('Las contraseñas no coinciden');
      return;
    }

    this.loading = true;

    this.authService.resetPassword(this.email, this.code, this.newPassword).subscribe({
      next: () => {
        alert('Contraseña actualizada con éxito');
        this.router.navigate(['/login']);
      },
      error: (err: any) => {
        alert(err.error || 'Error al cambiar la contraseña');
        this.loading = false;
      },
    });
  }
}
