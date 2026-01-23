import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth-service';
@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.html',
  styleUrls: ['./header.css'],
})
export class Header {
  constructor(
    private router: Router,
    private authService: AuthService,
  ) {}

  goHome() {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/home-logged']);
    } else {
      this.router.navigate(['/home']);
    }
  }

  goOrLogin(path: string) {
    if (this.authService.isLoggedIn()) {
      this.router.navigate([path]);
    } else {
      this.router.navigate(['/login']);
    }
  }
}
