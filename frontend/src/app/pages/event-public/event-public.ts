import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { EventService } from '../../services/event-service';
import { OnInit } from '@angular/core';
import { EventResponse } from '../../model/event-response';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-event-public',
  templateUrl: './event-public.html',
  imports: [CommonModule],
  styleUrls: ['./event-public.css'],
})
export class EventPublic implements OnInit {
  event!: EventResponse;
  countdown: string = '';

  constructor(
    private route: ActivatedRoute,
    private eventService: EventService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.eventService.getPublicEvent(id).subscribe((res) => {
      console.log('EVENTO PUBLICO:', res);

      this.event = res;
      this.startCountdown(res.eventDate);

      this.cdr.detectChanges();
    });
  }

  startCountdown(eventDate: string): void {
    const [year, month, day] = eventDate.split('-').map(Number);

    const today = new Date();
    const todayDateOnly = new Date(today.getFullYear(), today.getMonth(), today.getDate());

    const eventDateOnly = new Date(year, month - 1, day);

    const diffTime = eventDateOnly.getTime() - todayDateOnly.getTime();
    const diffDays = Math.round(diffTime / (1000 * 60 * 60 * 24));

    if (diffDays > 1) {
      this.countdown = `Faltan ${diffDays} días`;
    } else if (diffDays === 1) {
      this.countdown = 'Falta 1 día';
    } else if (diffDays === 0) {
      this.countdown = '¡Es hoy! 🎉';
    } else {
      this.countdown = 'Evento finalizado';
    }
  }
}
