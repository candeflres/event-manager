import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ElementResponse } from '../../model/element-response';
import { ElementService } from '../../services/element-service';
import { RouterLink, RouterModule } from '@angular/router';
import { CommonModule, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-manage-elements-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './manage-elements-list.html',
  styleUrls: ['./manage-elements-list.css'],
})
export class ManageElementsList implements OnInit {
  elements: ElementResponse[] = [];
  loading = true;
  error: string | null = null;

  constructor(
    private elementService: ElementService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.elementService.getAll().subscribe({
      next: (res) => {
        console.log('ELEMENTS', res);
        this.elements = res;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }
}
