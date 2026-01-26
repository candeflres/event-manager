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

  constructor(
    private http: HttpClient,
    private router: Router,
    private authService: AuthService,
  ) {}

  login() {
    const auth = btoa(`${this.email}:${this.password}`);

    const headers = new HttpHeaders({
      Authorization: 'Basic ' + auth,
    });

    this.http.get('http://localhost:8080/api/events?page=0&size=1', { headers }).subscribe({
      next: () => {
        localStorage.setItem('auth', auth);
        this.authService.login(auth);
        this.router.navigate(['/home-logged']);
      },
      error: (err) => {
        console.error(err.status, err.error);
        alert('Credenciales incorrectas');
      },
    });
  }
}
