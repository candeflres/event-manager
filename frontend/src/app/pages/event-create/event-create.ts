import { Component } from '@angular/core';
import { EventService } from '../../services/event-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { Login } from '../login/login';
@Component({
  selector: 'app-event-create',
  imports: [CommonModule, FormsModule],
  templateUrl: './event-create.html',
  styleUrl: './event-create.css',
})
export class EventCreate {
  elements: any[] = [];
  selectedOptions: Map<number, any> = new Map();

  isSubmitting = false;
  estimatedBudget = 0;
  errorMessage: string | null = null;

  form = {
    name: '',
    description: '',
    eventDate: '',
  };

  constructor(
    private eventService: EventService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit() {
    this.loadElements();
  }

  get selectedOptionsArray() {
    return Array.from(this.selectedOptions.values());
  }

  loadElements() {
    this.eventService.getElementsWithOptions().subscribe((res) => {
      this.elements = res.filter((e) => e.options?.length);
      this.cdr.detectChanges();
    });
  }

  selectOption(elementId: number, option: any) {
    this.selectedOptions.set(elementId, option);
    this.calculateBudget();
  }

  isSelected(optionId: number): boolean {
    return Array.from(this.selectedOptions.values()).some((o) => o.id === optionId);
  }

  calculateBudget() {
    this.estimatedBudget = this.selectedOptionsArray.reduce((acc, opt) => acc + opt.price, 0);
  }

  createEvent() {
    if (this.isSubmitting) return;

    this.errorMessage = null;

    if (!this.form.name.trim()) {
      this.errorMessage = 'El nombre del evento es obligatorio';
      return;
    }

    if (!this.form.description.trim()) {
      this.errorMessage = 'La descripción es obligatoria';
      return;
    }

    if (!this.form.eventDate) {
      this.errorMessage = 'Seleccioná una fecha para el evento';
      return;
    }

    if (this.selectedOptions.size === 0) {
      this.errorMessage = 'Tenés que seleccionar al menos una opción';
      return;
    }

    const selectedDate = new Date(this.form.eventDate);
    const minDate = new Date();
    minDate.setDate(minDate.getDate() + 2);

    if (selectedDate < minDate) {
      this.errorMessage = 'El evento debe crearse con al menos 2 días de anticipación';
      return;
    }

    this.isSubmitting = true;

    const payload = {
      name: this.form.name,
      description: this.form.description,
      eventDate: this.form.eventDate,
      optionIds: this.selectedOptionsArray.map((o) => o.id),
    };

    this.eventService.createEvent(payload).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.router.navigate(['/event-list'], {
          state: { created: true },
        });
      },
      error: (err) => {
        this.isSubmitting = false;
        this.errorMessage = typeof err.error === 'string' ? err.error : 'Error al crear el evento';
      },
    });
  }

  goBack() {
    window.location.href = 'http://localhost:4200/home-logged';
  }
}
