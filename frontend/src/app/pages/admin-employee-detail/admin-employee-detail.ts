import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { UserService } from '../../services/user-service';
import { UserProfile } from '../../model/userProfile';

type Role = 'CLIENT' | 'EMPLOYEE' | 'ADMIN';

@Component({
  selector: 'app-admin-employee-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './admin-employee-detail.html',
  styleUrls: ['./admin-employee-detail.css'],
})
export class AdminEmployeeDetail implements OnInit {
  loading = true;
  error: string | null = null;

  employee!: UserProfile;
  editMode = false;

  form = {
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    role: 'EMPLOYEE' as Role,
  };

  private employeeId!: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.employeeId = Number(idParam);

    if (!this.employeeId || Number.isNaN(this.employeeId)) {
      this.error = 'ID inválido';
      this.loading = false;
      return;
    }

    this.load();
  }

  private load(): void {
    this.loading = true;
    this.error = null;

    this.userService.getUserById?.(this.employeeId)?.subscribe({
      next: (res: UserProfile) => {
        this.employee = res;
        this.patchFormFromEmployee();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'No se pudo cargar el empleado';
        this.loading = false;
        this.cdr.detectChanges();
      },
    }) ?? this.fallbackLoadFromList();
  }

  private fallbackLoadFromList(): void {
    this.userService.getEmployees().subscribe({
      next: (list) => {
        const found = list.find((u) => u.id === this.employeeId);
        if (!found) {
          this.error = 'Empleado no encontrado';
          this.loading = false;
          this.cdr.detectChanges();
          return;
        }
        this.employee = found;
        this.patchFormFromEmployee();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'No se pudo cargar el empleado';
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  private patchFormFromEmployee(): void {
    this.form.firstName = this.employee.firstName ?? '';
    this.form.lastName = this.employee.lastName ?? '';
    this.form.email = this.employee.email ?? '';
    this.form.phone = (this.employee as any).phone ?? '';
    this.form.role = (this.employee as any).role ?? 'EMPLOYEE';
  }

  goBack(): void {
    this.router.navigate(['/admin/employees']);
  }

  enableEdit(): void {
    this.editMode = true;
    this.patchFormFromEmployee();
  }

  cancelEdit(): void {
    this.editMode = false;
    this.patchFormFromEmployee();
  }

  save(): void {
    this.error = null;

    const roleChanged = (this.employee as any).role !== this.form.role;

    const payloadBasics = {
      firstName: this.form.firstName.trim(),
      lastName: this.form.lastName.trim(),
      email: this.form.email.trim(),
      phone: this.form.phone.trim(),
    };

    const ops: Array<Promise<void>> = [];

    if (roleChanged) {
      ops.push(
        this.userService
          .updateUserRole(this.employeeId, this.form.role)
          .toPromise()
          .then(() => void 0),
      );
    }

    if ((this.userService as any).updateUserById) {
      ops.push(
        (this.userService as any)
          .updateUserById(this.employeeId, payloadBasics)
          .toPromise()
          .then(() => void 0),
      );
    } else {
      if (!roleChanged) {
        this.error =
          'No tenés endpoint para actualizar datos (solo rol). Agregá PUT /api/users/{id} o un método updateUserById en el service.';
        return;
      }
    }

    this.loading = true;

    Promise.all(ops)
      .then(() => {
        alert('Cambios guardados');
        this.editMode = false;
        this.load();
      })
      .catch((err) => {
        this.error = err?.error?.message || 'No se pudieron guardar los cambios';
        this.loading = false;
        this.cdr.detectChanges();
      });
  }
}
