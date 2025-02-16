import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatSelectModule } from '@angular/material/select';
import { CustomerResponse } from '../../../models/customer-response.model';
import { PackResponse } from '../../../models/pack-response.model';
import { SubscriptionRequest } from '../../../models/subscription-request.model';
import { CustomerService } from '../../../services/customer.service';
import { PackService } from '../../../services/pack.service';
import { SubscriptionService } from '../../../services/subscription.service';

@Component({
  selector: 'app-subscription-create',
  templateUrl: './subscription-create.component.html',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule
  ]
})
export class SubscriptionCreateComponent implements OnInit {
  subscriptionForm: FormGroup;
  customers: CustomerResponse[] = [];
  packs: PackResponse[] = [];
  errorMessage!: string;
  successMessage!: string;

  constructor(
    private fb: FormBuilder,
    private subscriptionService: SubscriptionService,
    private customerService: CustomerService,
    private packService: PackService,
    private router: Router
  ) {
    this.subscriptionForm = this.fb.group({
      customerId: [null, [Validators.required]],
      packId: [null, [Validators.required]]
    });
  }

  ngOnInit() {
    this.loadCustomers();
    this.loadPacks();
  }

  loadCustomers() {
    this.customerService.getAllCustomersWithoutPagination().subscribe(data => {
      this.customers = data;
    });
  }

  loadPacks() {
    this.packService.getAllPacks().subscribe(data => {
      this.packs = data;
    });
  }

  onSubmit() {
    if (this.subscriptionForm.valid) {
      const request: SubscriptionRequest = this.subscriptionForm.value;
      this.subscriptionService.subscribe(request).subscribe(response  => {
        console.log(response)

        // this.router.navigate(['/dashboard/subscriptions']);
      }, error => {
        this.errorMessage = error;
        console.error('subscription failed:', error); // Ajout de logs
      }
    );
    }
  }
}
