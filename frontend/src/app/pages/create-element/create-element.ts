import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ElementService } from '../../services/element-service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './create-element.html',
  styleUrls: ['./create-element.css'],
})
export class CreateElement {
  name = '';
  description = '';
  loading = false;
  error = '';

  constructor(
    private elementService: ElementService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}

  save() {
    if (!this.name.trim()) {
      this.error = 'El nombre es obligatorio';
      return;
    }

    this.loading = true;
    this.error = '';

    this.elementService
      .create({
        name: this.name,
        description: this.description,
      })
      .subscribe({
        next: () => {
          this.router.navigate(['/manage-elements']);
          this.cdr.detectChanges();
        },
        error: (err) => {
          this.error = err.error?.message || 'Error al crear elemento';
          this.loading = false;
          this.cdr.detectChanges();
        },
      });
  }

  cancel() {
    this.router.navigate(['/manage-elements']);
  }
}
