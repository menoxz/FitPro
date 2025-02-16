import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CustomerRequest } from '../../../models/customer-request.model';
import { CustomerService } from '../../../services/customer.service';

@Component({
  selector: 'app-customer-create',
  templateUrl: './customer-create.component.html',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ]
})
export class CustomerCreateComponent {
  customerForm: FormGroup;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(private fb: FormBuilder, private customerService: CustomerService, private router: Router) {
    this.customerForm = this.fb.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      phone: ['', [Validators.required, Validators.pattern('^\\d{8}$')]]
    });
  }

  onSubmit() {
    if (this.customerForm.valid) {
      const customer: CustomerRequest = this.customerForm.value;
      this.customerService.addCustomer(customer).subscribe(() => {
        this.router.navigate(['/dashboard/customers']);
      }, error =>{
        this.errorMessage = error;
      }
    );
    }
  }
}
