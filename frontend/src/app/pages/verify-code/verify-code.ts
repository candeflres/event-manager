import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import { finalize } from 'rxjs';
import { ChangeDetectorRef } from '@angular/core';
@Component({
  selector: 'app-verify-code',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
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
    private cdr: ChangeDetectorRef,
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

    this.authService
      .verifyCode(this.email, this.code)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdr.detectChanges();
        }),
      )
      .subscribe({
        next: () => {
          this.router.navigate(['/reset-password'], {
            queryParams: { email: this.email, code: this.code },
          });
        },
        error: (err) => {
          this.errorMessage =
            err.status === 401 ? 'El código ingresado es incorrecto' : 'Código inválido o expirado';
        },
      });
  }
}
