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

  enableEdit(): void {
    this.editMode = true;
  }

  saveChanges(): void {
    this.eventService.updateEvent(this.event.id, this.editForm).subscribe(() => {
      alert('Evento actualizado');
      this.editMode = false;
      this.reload();
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
}
