import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';

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

  constructor(
    private route: ActivatedRoute,
    private router: Router,
  ) {
    this.email = this.route.snapshot.queryParamMap.get('email') || '';
  }

  confirmCode() {
    if (!this.code) {
      alert('Ingresá el código');
      return;
    }

    this.router.navigate(['/reset-password'], {
      queryParams: {
        email: this.email,
        code: this.code,
      },
    });
  }
}
