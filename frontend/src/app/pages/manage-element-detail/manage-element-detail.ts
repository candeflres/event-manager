import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ElementResponse } from '../../model/element-response';
import { ActivatedRoute } from '@angular/router';
import { ElementService } from '../../services/element-service';
import { OptionService } from '../../services/option-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OptionResponse } from '../../model/option-response';

@Component({
  selector: 'app-manage-element-detail',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './manage-element-detail.html',
  styleUrl: './manage-element-detail.css',
})
export class ManageElementDetail implements OnInit {
  element!: ElementResponse;
  loading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private elementService: ElementService,
    private optionService: OptionService,
    private cdr: ChangeDetectorRef,
  ) {}

  // =========================
  // INIT
  // =========================
  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadElement(id);
  }

  // =========================
  // LOAD ELEMENT
  // =========================
  loadElement(id: number) {
    this.loading = true;

    this.elementService.getByIdForManagement(id).subscribe({
      next: (res) => {
        this.element = res;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.error = err.error?.message || 'No se pudo cargar el elemento';
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  // =========================
  // UPDATE ELEMENT
  // =========================
  updateElement() {
    this.elementService
      .update(this.element.id, {
        name: this.element.name,
        description: this.element.description,
        available: this.element.available,
      })
      .subscribe({
        next: (updated) => {
          this.element.name = updated.name;
          this.element.description = updated.description;
          this.element.available = updated.available;

          this.cdr.detectChanges();
          alert('Elemento guardado correctamente');
        },
        error: (err) => {
          alert(err.error?.message || 'No se pudo guardar el elemento');
        },
      });
  }

  // =========================
  // OPTIONS HELPERS
  // =========================
  hasUnsavedOption(): boolean {
    return this.element?.options?.some((o: any) => o._new) ?? false;
  }

  // =========================
  // ADD OPTION (UI ONLY)
  // =========================
  addOption() {
    if (this.hasUnsavedOption()) return;

    const newOption: any = {
      id: undefined,
      name: 'Nueva opción',
      description: '',
      price: 0,
      available: true,
      elementId: this.element.id,
      _new: true,
    };

    this.element.options.push(newOption);
    this.cdr.detectChanges();
  }

  // =========================
  // SAVE OPTION (CREATE / UPDATE)
  // =========================
  saveOption(option: any) {
    if (!option.name || option.price < 0) {
      alert('Nombre y precio válido son obligatorios');
      return;
    }

    // ---- CREATE ----
    if (option._new) {
      const createRequest = {
        name: option.name,
        description: option.description,
        price: option.price,
        available: option.available,
        elementId: this.element.id,
      };

      this.optionService.create(createRequest).subscribe({
        next: (created) => {
          Object.assign(option, created);
          delete option._new;

          this.cdr.detectChanges();
          alert('Opción creada correctamente');
        },
        error: (err) => {
          alert(err.error?.message || 'No se pudo crear la opción');
        },
      });

      return;
    }

    // ---- UPDATE ----
    const updateRequest = {
      name: option.name,
      description: option.description,
      price: option.price,
      available: option.available,
    };

    this.optionService.update(option.id, updateRequest).subscribe({
      next: (updated) => {
        Object.assign(option, updated);
        this.cdr.detectChanges();
        alert('Opción guardada correctamente');
      },
      error: (err) => {
        alert(err.error?.message || 'No se pudo guardar la opción');
      },
    });
  }

  // =========================
  // DELETE OPTION
  // =========================
  deleteOption(option: any) {
    // si es nueva y no está guardada
    if (option._new) {
      this.element.options = this.element.options.filter((o) => o !== option);
      this.cdr.detectChanges();
      return;
    }

    if (!confirm('¿Eliminar esta opción?')) return;

    this.optionService.delete(option.id).subscribe({
      next: () => {
        this.element.options = this.element.options.filter((o) => o.id !== option.id);
        this.cdr.detectChanges();
      },
      error: () => alert('No se pudo eliminar la opción'),
    });
  }

  // =========================
  // NAVIGATION
  // =========================
  goBack() {
    window.location.href = 'http://localhost:4200/manage-elements';
  }
}
