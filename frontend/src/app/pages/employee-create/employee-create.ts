import { Component } from '@angular/core';
import { UserService } from '../../services/user-service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-admin-employee-create',
  imports: [CommonModule, FormsModule],
  templateUrl: './employee-create.html',
  styleUrl: './employee-create.css',
})
export class AdminEmployeeCreate {
  loading = false;
  error: string | null = null;

  form = {
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    password: '',
  };

  constructor(
    private userService: UserService,
    private router: Router,
  ) {}

  save() {
    this.error = null;
    this.loading = true;

    this.userService.createEmployee(this.form).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/admin/employees']);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message || 'No se pudo crear el empleado';
      },
    });
  }

  cancel() {
    this.router.navigate(['/admin/employees']);
  }
}
