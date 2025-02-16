import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CustomerResponse } from '../../../models/customer-response.model';
import { CustomerService } from '../../../services/customer.service';
import { SubscriptionResponse } from '../../../models/subscription-response.model';
import { SubscriptionService } from '../../../services/subscription.service';

@Component({
  selector: 'app-customer-details',
  templateUrl: './customer-details.component.html',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    RouterLink
  ]
})
export class CustomerDetailsComponent implements OnInit {
  customer!: CustomerResponse;
  subscriptions!: SubscriptionResponse[];
  
  constructor(
    private route: ActivatedRoute,
    private customerService: CustomerService,
    private subscriptionService: SubscriptionService,
    private router: Router
  ) {}

  ngOnInit() {
    const customerId = Number(this.route.snapshot.paramMap.get('id'));
    this.customerService.getCustomerById(customerId).subscribe(data => {
      this.customer = data;
    });

    this.subscriptionService.getCustomerSubscriptions(this.customer.id).subscribe(data => {
      this.subscriptions = data;
    });
  }

  deleteCustomer(customerId: number) {
    this.customerService.deleteCustomer(customerId).subscribe(() => {
      this.router.navigate(['/customers']);
    });
  }
}
