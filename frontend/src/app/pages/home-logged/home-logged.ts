import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home-logged',
  imports: [CommonModule, RouterModule],
  templateUrl: './home-logged.html',
  styleUrl: './home-logged.css',
})
export class HomeLogged {
  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}
  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
