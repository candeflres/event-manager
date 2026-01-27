import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';
import { UserService } from '../../services/user-service';

@Component({
  selector: 'app-home-logged',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home-logged.html',
  styleUrls: ['./home-logged.css'],
})
export class HomeLogged implements OnInit {
  isClient = false;
  isEmployee = false;
  loading = true;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.userService.getMyProfile().subscribe({
      next: (user) => {
        console.log('ROL: ', user.role);
        this.isClient = user.role === 'CLIENT';
        this.isEmployee = user.role === 'EMPLOYEE';
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loading = false;
      },
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
