import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Header } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';
import { UserService } from '../../services/user-service';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
@Component({
  selector: 'app-register',
  imports: [RouterModule, CommonModule, ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private service = inject(UserService);
  private fb = inject(FormBuilder);
  private router = inject(Router);

  form = this.fb.nonNullable.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    phone: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
  });

  register() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const newUser = this.form.getRawValue();

    this.service.register(newUser).subscribe({
      next: () => {
        alert('Usuario registrado con éxito');
        this.router.navigate(['/login']); // 👈 REDIRECCIÓN
      },
      error: (err) => {
        console.error(err);
        alert('Error al registrar usuario');
      },
    });
  }
}
