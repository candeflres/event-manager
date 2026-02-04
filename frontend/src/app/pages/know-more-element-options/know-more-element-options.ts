import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ElementResponse } from '../../model/element-response';
import { ElementService } from '../../services/element-service';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-know-more-element-options',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './know-more-element-options.html',
  styleUrls: ['./know-more-element-options.css'],
})
export class KnowMoreElementOptions implements OnInit {
  element: ElementResponse | null = null;

  constructor(
    private route: ActivatedRoute,
    private elementService: ElementService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.elementService.getPublicById(id).subscribe((res) => {
      console.log('RESPUESTA', res);
      this.element = res;
      this.cdr.markForCheck();
    });
  }
}
