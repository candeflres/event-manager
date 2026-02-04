import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ElementService } from '../../services/element-service';
import { ElementResponse } from '../../model/element-response';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
@Component({
  standalone: true,
  selector: 'app-know-more-element',
  imports: [CommonModule, RouterLink],
  templateUrl: './know-more-element.html',
  styleUrl: './know-more-element.css',
})
export class KnowMoreElement implements OnInit {
  elements: ElementResponse[] = [];

  constructor(
    private elementService: ElementService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.elementService.getAvailable().subscribe((res) => {
      this.elements = res.filter((element) => element.options && element.options.length > 0);
      this.cdr.detectChanges();
    });
  }
}
