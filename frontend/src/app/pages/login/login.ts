import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  email = '';
  password = '';

  constructor(
    private http: HttpClient,
    private router: Router,
  ) {}

  login() {
    const headers = new HttpHeaders({
      Authorization: 'Basic ' + btoa(`${this.email}:${this.password}`),
    });

    this.http.get('http://localhost:8080/api/users', { headers }).subscribe({
      next: () => {
        this.router.navigate(['/home-logged']);
      },
      error: (err) => {
        console.error(err);
        alert('Error de login');
      },
    });
  }
}
