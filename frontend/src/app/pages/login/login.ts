import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  email = '';
  password = '';
  loading = false;

  constructor(
    private http: HttpClient,
    private router: Router,
    private authService: AuthService,
  ) {}

  login() {
    if (this.loading) return;

    this.loading = true;

    const auth = btoa(`${this.email}:${this.password}`);

    const headers = new HttpHeaders({
      Authorization: 'Basic ' + auth,
    });

    this.http.get('http://localhost:8080/api/events?page=0&size=1', { headers }).subscribe({
      next: () => {
        this.authService.login(auth);
        this.router.navigate(['/home-logged']);
      },
      error: () => {
        alert('Credenciales incorrectas');
        this.loading = false;
      },
    });
  }
}
