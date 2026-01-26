import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';

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
    private http: HttpClient,
    private router: Router,
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

    this.http
      .post('http://localhost:8080/api/auth/reset-password', {
        email: this.email,
        code: this.code,
        newPassword: this.newPassword,
      })
      .subscribe({
        next: () => {
          alert('Contraseña actualizada con éxito');
          this.router.navigate(['/login']);
        },
        error: (err) => {
          alert(err.error?.message || 'Error al cambiar la contraseña');
          this.loading = false;
        },
      });
  }
}
