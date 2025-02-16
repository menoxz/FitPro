import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
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
import { SubscriptionResponse } from '../../../models/subscription-response.model';
import { SubscriptionService } from '../../../services/subscription.service';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-subscription-list',
  templateUrl: './subscription-list.component.html',
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
    MatIconModule,
    RouterLink
  ]
})
export class SubscriptionListComponent implements OnInit {
  displayedColumns: string[] = ['id', 'customerId', 'packId', 'startDate', 'endDate', 'active', 'actions'];
  dataSource!: MatTableDataSource<SubscriptionResponse>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private subscriptionService: SubscriptionService, private router: Router) {}

  ngOnInit() {
    this.loadSubscriptions(0, 10);
  }

  loadSubscriptions(page: number, size: number) {
    this.subscriptionService.getAllSubscriptions(page, size).subscribe(data => {
      this.dataSource = new MatTableDataSource(data);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      console.log(this.dataSource);
    });
  }

  subscribe() {
    this.router.navigate(['/dashboard/subscriptions/create']);
  }

  cancelSubscription(subscriptionId: number) {
    this.subscriptionService.cancelSubscription(subscriptionId).subscribe(() => {
      this.loadSubscriptions(0, 10);
    });
  }

  getEndDate(startDate: string | Date, days: number): Date {
    const date = new Date(startDate);
    date.setDate(date.getMonth() + days);
    return date;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.searchSubscriptions(filterValue, 0 , 10);
  }

  searchSubscriptions(query: string, page: number, size: number) {
    this.subscriptionService.searchSubscription(query,page,size).subscribe(data => {
      this.dataSource = new MatTableDataSource(data.content);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }
}
