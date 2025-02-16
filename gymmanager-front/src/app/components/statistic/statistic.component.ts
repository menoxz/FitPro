import { Component, OnInit } from '@angular/core';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { ViewChild } from '@angular/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { SubscriptionResponse } from '../../models/subscription-response.model';
import { StatisticsService } from '../../services/statistic.service';
import { SubscriptionService } from '../../services/subscription.service';
import { saveAs } from 'file-saver';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

interface Notification {
  title: string;
  message: string;
}

interface SubscriptionsByMonth {
  month: number;
  count: number;
}

interface RevenueByMonth {
  year: number;
  month: number;
  revenue: number;
}

interface CustomersCountByPack {
  packName: string;
  count: number;
}

@Component({
  selector: 'app-statistics',
  templateUrl: './statistic.component.html',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatDatepickerModule,
    MatNativeDateModule
  ]
})
export class StatisticsComponent implements OnInit {
  activeCustomersCount!: number;
  monthlyRevenue!: number;
  subscriptions: SubscriptionResponse[] = [];
  displayedColumns: string[] = ['id', 'customerId', 'packId', 'startDate', 'active'];
  dataSource!: MatTableDataSource<SubscriptionResponse>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  exportForm: FormGroup;
  subscriptionsByMonthData: any[] = [];
  customersCountByPackData: CustomersCountByPack[] = [];

  constructor(
    private fb: FormBuilder,
    private statisticsService: StatisticsService,
    private subscriptionService: SubscriptionService,
  ) {
    this.exportForm = this.fb.group({
      startDate: [null, Validators.required],
      endDate: [null, Validators.required]
    });
  }

  ngOnInit() {
    this.getActiveCustomersCount();
    this.getMonthlyRevenue();
    this.loadSubscriptions(0,10);
    this.loadCustomersCountByPack()
    // this.loadSubscriptionsByMonth();
  }

  getActiveCustomersCount() {
    this.statisticsService.getActiveCustomersCount().subscribe(data => {
      this.activeCustomersCount = data;
    });
  }

  getMonthlyRevenue() {
    this.statisticsService.getMonthlyRevenue().subscribe(data => {
      this.monthlyRevenue = data;
      console.log(this.monthlyRevenue);
    });
  }

  exportSubscriptions() {
    this.subscriptionService.exportSubscriptions().subscribe(data => {
      const blob = new Blob([data], { type: 'text/csv;charset=utf-8;' });
      saveAs(blob, 'subscriptions.csv');
    });
  }
  loadSubscriptions(page: number, size: number) {
    this.subscriptionService.getAllSubscriptions(page, size).subscribe(data => {
      console.log('Subscriptions Data:', data); // Ajoutez ce log
      if (data && data.length > 0) {
        this.subscriptions = data;
        this.dataSource = new MatTableDataSource(this.subscriptions);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      } else {
        console.log('No subscriptions data received');
      }
    }, error => {
      console.error('Error loading subscriptions:', error);
    });
  }

  loadCustomersCountByPack() {
    this.statisticsService.getCustomersCountByPack().subscribe(data => {
      console.log('Customers Count by Pack Data:', data);
      this.customersCountByPackData = Object.entries(data).map(([packName, count]) => ({ packName, count }));
      console.log('Processed Customers Count by Pack Data:', this.customersCountByPackData);
      this.renderCustomersCountByPackChart();
    }, error => {
      console.error('Error loading customers count by pack:', error);
    });
  }

  // loadSubscriptionsByMonth() {
  //   this.statisticsService.getSubscriptionsByMonth().subscribe(data => {
  //     console.log('Subscriptions by Month Data:', data);
  //     this.subscriptionsByMonthData = data;
  //     this.renderSubscriptionsByMonthChart();
  //   }, error => {
  //     console.error('Error loading subscriptions by month:', error);
  //   });
  // }

  renderCustomersCountByPackChart() {
    const labels = this.customersCountByPackData.map(item => item.packName);
    const data = this.customersCountByPackData.map(item => item.count);

    console.log('Customers Count by Pack Chart Labels:', labels);
    console.log('Customers Count by Pack Chart Data:', data);

    new Chart('customersCountByPackChart', {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'Customers Count',
          data: data,
          backgroundColor: 'rgba(75, 192, 192, 0.2)',
          borderColor: '#4BC0C0',
          borderWidth: 1
        }]
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
          }
        }
      }
    });
  }
}
