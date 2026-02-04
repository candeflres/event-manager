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
  menuOpen = false;

  constructor(
    private router: Router,
    private authService: AuthService,
  ) {}

  toggleMenu() {
    this.menuOpen = !this.menuOpen;
    document.body.classList.toggle('menu-open', this.menuOpen);
  }

  closeMenu() {
    this.menuOpen = false;
    document.body.classList.remove('menu-open');
  }

  goHome() {
    this.closeMenu();

    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/home-logged']);
    } else {
      this.router.navigate(['/home']);
    }
  }

  goOrLogin(path: string) {
    this.closeMenu();

    if (this.authService.isLoggedIn()) {
      this.router.navigate([path]);
    } else {
      this.router.navigate(['/login']);
    }
  }
}
