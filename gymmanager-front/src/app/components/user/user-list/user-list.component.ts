import { Component, OnInit } from '@angular/core';

import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { ViewChild } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { RouterLink } from '@angular/router';
import { UserResponse } from '../../../models/user-response.model';
import { UserService } from '../../../services/user.service';
import { MatIconModule } from '@angular/material/icon';


@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  standalone: true,
  imports: [
    MatCardModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    ReactiveFormsModule,
    RouterLink,
    MatIconModule
  ]
})
export class UserListComponent implements OnInit {
  displayedColumns: string[] = ['id', 'username', 'email', 'phoneNumber', 'roles', 'actions'];
  dataSource!: MatTableDataSource<UserResponse>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  erroMessage!: string;

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.loadUsers(0, 10);
  }

  loadUsers(page: number, size: number) {
    this.userService.getAllUsers(page, size).subscribe(data => {
      this.dataSource = new MatTableDataSource(data.content);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  deleteUser(userId: number) {
    if(!confirm('Are you sure you want to delete this user?')) {
    this.userService.deleteUser(userId).subscribe(data => {
      this.loadUsers(0, 10);
      console.log('bjvcddcd');
    }
  , error =>{
      this.erroMessage = error.message
  });
  }}
}
