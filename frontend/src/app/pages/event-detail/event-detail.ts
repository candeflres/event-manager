import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { EventService } from '../../services/event-service';
import { EventResponse } from '../../model/event-response';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
import { EventStatus } from '../../model/event-status';
import { UserService } from '../../services/user-service';
import { ElementResponse } from '../../model/element-response';
import { OptionResponse } from '../../model/option-response';
@Component({
  selector: 'app-event-detail',
  standalone: true,
  imports: [CommonModule, NgIf, NgFor, FormsModule, RouterLink],
  templateUrl: './event-detail.html',
  styleUrls: ['./event-detail.css'],
})
export class EventDetail implements OnInit {
  event!: EventResponse;
  loading = true;
  isEmployee = false;
  isAdmin = false;
  isClient = false;
  editMode = false;
  processingAction: 'APPROVE' | 'REJECT' | 'SAVE' | 'CANCEL' | null = null;
  isSubmitting = false;
  elements: ElementResponse[] = [];
  selectedOptionIds: number[] = [];
  estimatedBudget = 0;

  editForm = {
    name: '',
    description: '',
    eventDate: '',
  };

  constructor(
    private route: ActivatedRoute,
    private eventService: EventService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private userService: UserService,
  ) {}

  toggleOption(optionId: number, event: any) {
    if (event.target.checked) {
      this.selectedOptionIds.push(optionId);
    } else {
      this.selectedOptionIds = this.selectedOptionIds.filter((id) => id !== optionId);
    }

    this.updateEstimatedBudget();
  }

  updateEstimatedBudget() {
    let total = 0;

    for (const el of this.elements) {
      for (const opt of el.options) {
        if (this.selectedOptionIds.includes(opt.id)) {
          total += opt.price;
        }
      }
    }

    this.estimatedBudget = total;
  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.userService.getMyProfile().subscribe((user) => {
      this.isClient = user.role === 'CLIENT';
      this.isEmployee = user.role === 'EMPLOYEE';
      this.isAdmin = user.role === 'ADMIN';
      this.cdr.detectChanges();
    });

    this.eventService.getEventDetail(id).subscribe((res) => {
      this.event = res;

      this.editForm = {
        name: res.name,
        description: res.description,
        eventDate: res.eventDate,
      };

      this.loading = false;
      this.cdr.detectChanges();
    });
  }

  enableEdit() {
    if (!this.canEdit()) return;

    this.editMode = true;

    this.editForm = {
      name: this.event.name,
      description: this.event.description,
      eventDate: this.event.eventDate?.substring(0, 10),
    };

    this.selectedOptionIds = this.event.options.map((o) => o.id);

    this.eventService.getElementsWithOptions().subscribe((elements: ElementResponse[]) => {
      this.elements = elements
        .filter((el) => el.available)
        .map((el) => ({
          ...el,
          options: el.options.filter((opt) => opt.available),
        }))
        .filter((el) => el.options.length > 0);

      this.updateEstimatedBudget();
      this.cdr.detectChanges();
    });
  }

  canEdit(): boolean {
    return this.event.status === 'PENDING' || this.event.status === 'REJECTED';
  }

  goToEdit() {
    this.router.navigate(['/events', this.event.id, 'edit']);
  }

  saveChanges() {
    if (this.processingAction) return;

    this.processingAction = 'SAVE';

    const payload = {
      ...this.editForm,
      optionIds: this.selectedOptionIds,
    };

    this.eventService.updateEvent(this.event.id, payload).subscribe({
      next: (updated) => {
        this.event = updated;
        this.editMode = false;
        this.processingAction = null;
        this.cdr.detectChanges();
        alert('Evento actualizado');
      },
      error: (err) => {
        this.processingAction = null;
        alert(err.error?.message || 'Error al guardar el evento');
      },
    });
  }

  approve(): void {
    this.changeStatus('APPROVED');
  }

  reject(): void {
    this.changeStatus('REJECTED');
  }

  changeStatus(status: EventStatus): void {
    if (this.processingAction) return;

    if (!confirm(`¿Confirmar ${status === 'APPROVED' ? 'aprobación' : 'rechazo'} del evento?`)) {
      return;
    }

    this.processingAction = status === 'APPROVED' ? 'APPROVE' : 'REJECT';

    this.eventService.updateEventStatus(this.event.id, status).subscribe({
      next: (updatedEvent) => {
        this.event = updatedEvent;
        this.processingAction = null;
        this.cdr.detectChanges();
        alert('Estado actualizado');
      },
      error: (err) => {
        this.processingAction = null;
        alert(err.error?.message || 'No se pudo actualizar el estado');
      },
    });
  }
  reload(): void {
    this.router.navigate(['/event-list']);
  }

  goBack(): void {
    this.router.navigate(['/event-list']);
  }

  share(): void {
    const link = `${window.location.origin}/event/${this.event.id}`;
    console.log('LINK PUBLICO:', link);
    window.open(link, '_blank');
  }

  selectOption(elementId: number, optionId: number) {
    this.selectedOptionIds = this.selectedOptionIds.filter((id) => {
      for (const el of this.elements) {
        if (el.id === elementId) {
          return !el.options.some((opt: any) => opt.id === id);
        }
      }
      return true;
    });

    this.selectedOptionIds.push(optionId);

    this.updateEstimatedBudget();
  }

  cancelEvent() {
    if (this.processingAction) return;

    if (!confirm('¿Estás seguro de que querés cancelar este evento?')) {
      return;
    }

    this.processingAction = 'CANCEL';

    this.eventService.cancelEvent(this.event.id).subscribe({
      next: () => {
        this.router.navigate(['/event-list']);
      },
      error: (err) => {
        this.processingAction = null;
        alert(err.error?.message || 'No se pudo cancelar el evento');
      },
    });
  }
}
