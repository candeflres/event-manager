import { Component } from '@angular/core';
import { EventService } from '../../services/event-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';

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

  loadElements() {
    this.eventService.getElementsWithOptions().subscribe((res) => {
      this.elements = res.filter((e) => e.options && e.options.length > 0);
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
    this.estimatedBudget = Array.from(this.selectedOptions.values()).reduce(
      (acc, opt) => acc + opt.price,
      0,
    );
  }

  createEvent() {
    if (this.isSubmitting) return;

    if (!this.form.eventDate) {
      alert('Seleccioná una fecha para el evento');
      return;
    }

    const selectedDate = new Date(this.form.eventDate);
    const today = new Date();

    today.setHours(0, 0, 0, 0);
    selectedDate.setHours(0, 0, 0, 0);

    const diffInDays = (selectedDate.getTime() - today.getTime()) / (1000 * 60 * 60 * 24);

    if (diffInDays < 2) {
      alert('El evento debe crearse con al menos 2 días de anticipación');
      return;
    }

    const payload = {
      name: this.form.name,
      description: this.form.description,
      eventDate: this.form.eventDate,
      optionIds: Array.from(this.selectedOptions.values()).map((o) => o.id),
    };

    this.isSubmitting = true;

    this.eventService.createEvent(payload).subscribe({
      next: () => {
        alert('Evento creado con éxito');
        this.router.navigate(['/event-list']);
      },
      error: (err) => {
        console.error('Error creando evento', err);
        alert(err.error?.message || 'Error al crear el evento');
        this.isSubmitting = false;
      },
    });
  }
}
