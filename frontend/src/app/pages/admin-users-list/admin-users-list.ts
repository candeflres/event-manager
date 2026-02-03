import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UserProfile } from '../../model/userProfile';
import { UserService } from '../../services/user-service';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './admin-users-list.html',
  styleUrls: ['./admin-users-list.css'],
})
export class AdminUsersList implements OnInit {
  users: UserProfile[] = [];
  filteredUsers: UserProfile[] = [];

  roleFilter: '' | 'CLIENT' | 'EMPLOYEE' | 'ADMIN' = '';
  statusFilter: '' | 'active' | 'inactive' = '';

  constructor(
    private userService: UserService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.userService.getAllUsersIncludingInactive().subscribe((res) => {
      this.users = res;
      this.applyFilters();
      this.cdr.detectChanges();
    });
  }

  applyFilters(): void {
    this.filteredUsers = this.users.filter((u) => {
      const roleOk = !this.roleFilter || u.role === this.roleFilter;

      const statusOk =
        !this.statusFilter ||
        (this.statusFilter === 'active' && u.active) ||
        (this.statusFilter === 'inactive' && !u.active);

      return roleOk && statusOk;
    });
  }
}
