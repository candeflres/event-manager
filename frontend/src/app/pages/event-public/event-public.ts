import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { EventService } from '../../services/event-service';
import { OnInit } from '@angular/core';
import { EventResponse } from '../../model/event-response';
@Component({
  standalone: true,
  selector: 'app-event-public',
  templateUrl: './event-public.html',
  imports: [CommonModule],
  styleUrls: ['./event-public.css'],
})
export class EventPublic implements OnInit {
  event!: EventResponse;

  constructor(
    private route: ActivatedRoute,
    private eventService: EventService,
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.eventService.getPublicEvent(id).subscribe((res) => {
      this.event = res;
    });
  }
}
