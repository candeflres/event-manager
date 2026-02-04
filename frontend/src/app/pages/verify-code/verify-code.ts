import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-verify-code',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './verify-code.html',
  styleUrl: './verify-code.css',
})
export class VerifyCode {
  code = '';
  email = '';
  errorMessage: string | null = null;
  loading = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
  ) {
    this.email = this.route.snapshot.queryParamMap.get('email') || '';
  }

  confirmCode() {
    if (!this.code) {
      this.errorMessage = 'Ingresá el código';
      return;
    }

    this.loading = true;
    this.errorMessage = null;

    this.authService.verifyCode(this.email, this.code).subscribe({
      next: () => {
        this.router.navigate(['/reset-password'], {
          queryParams: {
            email: this.email,
            code: this.code,
          },
        });
      },
      error: (err: any) => {
        this.errorMessage = err.error || 'Código inválido o expirado';
        this.loading = false;
      },
    });
  }
}
