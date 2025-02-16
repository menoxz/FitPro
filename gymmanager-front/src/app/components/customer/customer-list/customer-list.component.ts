import { Component, OnInit } from '@angular/core';

import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { ViewChild } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CustomerResponse } from '../../../models/customer-response.model';
import { CustomerService } from '../../../services/customer.service';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    RouterLink,
    MatIconModule,
  ]
})
export class CustomerListComponent implements OnInit {
  displayedColumns: string[] = ['id', 'fullName', 'phone', 'registrationDate', 'subscriptionCount', 'actions'];
  dataSource!: MatTableDataSource<CustomerResponse>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  errorMessage!: string;

  constructor(private customerService: CustomerService, private router: Router) {}

  ngOnInit() {
    this.loadCustomers(0, 10);
  }

  loadCustomers(page: number, size: number) {
    this.customerService.getAllCustomers(page, size).subscribe(data => {
      this.dataSource = new MatTableDataSource(data.content);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  searchCustomers(query: string, page: number, size: number) {
    this.customerService.searchCustomers(query, page, size).subscribe(data => {
      this.dataSource = new MatTableDataSource(data.content);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.searchCustomers(filterValue, 0, 10);
  }

  addCustomer() {
    this.router.navigate(['/customers/create']);
  }

  editCustomer(customerId: number) {
    this.router.navigate(['/customers/edit', customerId]);
  }

  deleteCustomer(customerId: number) {
    if(confirm('Are you sure you want to delete this customer')) {
    this.customerService.deleteCustomer(customerId).subscribe(() => {
      this.loadCustomers(0, 10);
    }, error => {
      this.errorMessage = error;
      console.error(error);
    }
  );
  }}
}
