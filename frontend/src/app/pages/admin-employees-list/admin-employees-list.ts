import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user-service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { UserProfile } from '../../model/userProfile';
@Component({
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-employees-list.html',
  styleUrls: ['./admin-employees-list.css'],
})
export class AdminEmployeesList implements OnInit {
  employees: UserProfile[] = [];

  constructor(
    private userService: UserService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.userService.getEmployees().subscribe((res) => {
      this.employees = res;
      this.cdr.detectChanges();
    });
  }

  goBack() {
    window.location.href = 'http://localhost:4200/home-logged';
  }
}
