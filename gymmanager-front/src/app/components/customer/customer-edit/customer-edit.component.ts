import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CustomerRequest } from '../../../models/customer-request.model';
import { CustomerResponse } from '../../../models/customer-response.model';
import { CustomerService } from '../../../services/customer.service';

@Component({
  selector: 'app-customer-edit',
  templateUrl: './customer-edit.component.html',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ]
})
export class CustomerEditComponent implements OnInit {
  customerForm: FormGroup;
  customerId!: number;
  customer!: CustomerResponse;
  errorMessage!: string;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private customerService: CustomerService
  ) {
    this.customerForm = this.fb.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      phone: ['', [Validators.required, Validators.pattern('^\\d{8}$')]]
    });
  }

  ngOnInit() {
    this.customerId = Number(this.route.snapshot.paramMap.get('id'));
    this.customerService.getCustomerById(this.customerId).subscribe(data => {
      this.customer = data;
      if (this.customer) {
        this.customerForm.patchValue({
          firstName: this.customer.fullName.split(' ')[0],
          lastName: this.customer.fullName.split(' ')[1],
          phone: this.customer.phone
        });
      }
    });
  }

  onSubmit() {
    if (this.customerForm.valid) {
      const customer: CustomerRequest = this.customerForm.value;
      this.customerService.updateCustomer(this.customerId, customer).subscribe(() => {
        this.errorMessage = '';
        this.router.navigate(['/dashboard/customers']);
      });
    }else{
      this.errorMessage = 'Numéro invalide ou déja utilisé';
    }
  }
}
