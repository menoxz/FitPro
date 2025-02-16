import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../shared/material.module';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MaterialModule,
    RouterOutlet,
    CommonModule,
    RouterLink,
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatSidenavModule,
    MatListModule,


  ]
})
export class DashboardComponent implements OnInit {
  constructor(private authService: AuthService, private router: Router) {}
  ngOnInit(): void {
    this.authService.getCurrentUser().subscribe(user => {
      this.userId = user.id;
      this.userRole = user.roles;
      console.log('User ID:', this.userId);
    });
    }

  isExpanded = true;
  userId : any;
  userRole!: any;
  role : String = "ROLE_ADMIN"

  logout() {
    localStorage.removeItem('authToken');
    this.router.navigate(['/login']);
  }

  toggleSidenav() {
    this.isExpanded = !this.isExpanded;
  }


}
