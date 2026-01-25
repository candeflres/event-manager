import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { EventService } from '../../services/event-service';
import { EventResponse } from '../../model/event-response';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-event-detail',
  standalone: true,
  imports: [CommonModule, NgIf, NgFor, FormsModule],
  templateUrl: './event-detail.html',
  styleUrl: './event-detail.css',
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
  ) {}

  ngOnInit(): void {
    const role = localStorage.getItem('role'); // EMPLOYEE / CLIENT
    this.isEmployee = role === 'EMPLOYEE';
    this.isClient = role === 'CLIENT';

    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.eventService.getEventDetail(id).subscribe((res) => {
      this.event = res;
      this.editForm = {
        name: res.name,
        description: res.description,
        eventDate: res.eventDate,
      };
      this.loading = false;
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

  changeStatus(status: 'APPROVED' | 'REJECTED'): void {
    this.eventService.updateEventStatus(this.event.id, status).subscribe(() => {
      alert(`Evento ${status}`);
      this.reload();
    });
  }

  reload(): void {
    this.router.navigate(['/event-list']);
  }

  goBack(): void {
    this.router.navigate(['/event-list']);
  }

  share(): void {
    alert('Compartir (pendiente)');
  }
}
