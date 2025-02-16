import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UserService } from '../../services/user.service';
import { UserResponse } from '../../models/user-response.model';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  standalone: true,
  imports: [
    RouterModule,
    CommonModule
  ]
})
export class OverviewComponent implements OnInit {
  user!: any;
  role: string = 'ROLE_ADMIN';
  constructor(
      private authService: AuthService,
    ) {}
  ngOnInit(): void {
    this.loadUser();
  }
  loadUser() {
    this.authService.getCurrentUser().subscribe(data => {
      this.user = data.roles;

    });
  }
}

