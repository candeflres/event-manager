import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UserProfile } from '../../model/userProfile';
import { UserService } from '../../services/user-service';

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-users-list.html',
  styleUrls: ['./admin-users-list.css'],
})
export class AdminUsersList implements OnInit {
  users: UserProfile[] = [];

  constructor(
    private userService: UserService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.userService.getAllUsersIncludingInactive().subscribe((res) => {
      this.users = res;
      this.cdr.detectChanges();
    });
  }
}
