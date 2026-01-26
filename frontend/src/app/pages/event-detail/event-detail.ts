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
  isClient = false;
  editMode = false;
  elements: any[] = [];
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
      this.isEmployee = user.role === 'EMPLOYEE';
      this.isClient = user.role === 'CLIENT';
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

    this.eventService.getElementsWithOptions().subscribe((elements) => {
      this.elements = elements;
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
    const payload = {
      ...this.editForm,
      optionIds: this.selectedOptionIds,
    };

    this.eventService.updateEvent(this.event.id, payload).subscribe({
      next: (updated) => {
        this.event = updated;
        this.editMode = false;
      },
      error: (err) => {
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
    if (status !== 'APPROVED' && status !== 'REJECTED') return;

    if (!confirm(`¿Confirmar ${status === 'APPROVED' ? 'aprobación' : 'rechazo'} del evento?`)) {
      return;
    }

    this.eventService.updateEventStatus(this.event.id, status).subscribe({
      next: (updatedEvent) => {
        this.event = updatedEvent;

        this.cdr.detectChanges();

        alert('Estado actualizado');
      },
      error: (err) => {
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
    if (!confirm('¿Estás seguro de que querés cancelar este evento?')) {
      return;
    }

    this.eventService.cancelEvent(this.event.id).subscribe({
      next: () => {
        this.router.navigate(['/event-list']);
      },
      error: (err) => {
        console.error(err);
        alert(err.error?.message || 'No se pudo cancelar el evento');
      },
    });
  }
}
