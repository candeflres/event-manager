import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-past-events',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './past-events.html',
  styleUrls: ['./past-events.css'],
})
export class PastEvents implements AfterViewInit {
  @ViewChild('group') group!: ElementRef<HTMLDivElement>;
  @ViewChild('track') track!: ElementRef<HTMLDivElement>;

  eventos = [
    'https://i.pinimg.com/1200x/9c/bf/11/9cbf112665d6811d0202c52fc41b0f5d.jpg',
    'https://i.pinimg.com/736x/57/36/43/5736435e7bc1a177af72e6d05956271e.jpg',
    'https://i.pinimg.com/736x/ff/df/25/ffdf2515d70406d5c063ccc1543481d0.jpg',
    'https://i.pinimg.com/1200x/74/59/10/74591086a019af1ee11f2c423820deca.jpg',
    'https://i.pinimg.com/1200x/bb/cd/f1/bbcdf1fa0a1f6144bb66864d50f0e7ba.jpg',
    'https://i.pinimg.com/1200x/fa/bf/13/fabf133762114f38b45fb5b9f2dc6f14.jpg',
    'https://i.pinimg.com/736x/2b/14/de/2b14de10902cd3c557dbc9734481396b.jpg',
    'https://i.pinimg.com/736x/db/30/e8/db30e838f4502d98cea6ce391b150789.jpg',
  ];

  ngAfterViewInit() {
    const width = this.group.nativeElement.offsetWidth;
    this.track.nativeElement.style.setProperty('--scroll-width', `${width}px`);
  }
}
