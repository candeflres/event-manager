import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './forgot-password.html',
  styleUrl: './forgot-password.css',
})
export class ForgotPassword {
  email = '';
  loading = false;

  constructor(
    private http: HttpClient,
    private router: Router,
  ) {}

  sendCode() {
    if (!this.email) {
      alert('Ingresá un email');
      return;
    }

    this.loading = true;

    this.http
      .post('http://localhost:8080/api/auth/forgot-password', {
        email: this.email,
      })
      .subscribe({
        next: () => {
          this.router.navigate(['/verify-code'], {
            queryParams: { email: this.email },
          });
        },
        error: (err) => {
          alert(err.error?.message || 'Error enviando el código');
          this.loading = false;
        },
      });
  }
}
