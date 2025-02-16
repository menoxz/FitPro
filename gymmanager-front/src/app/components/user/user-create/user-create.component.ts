import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Router, RouterLink } from '@angular/router';
import { UserCreateRequest } from '../../../models/user-create-request.model';
import { UserService } from '../../../services/user.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-create',
  templateUrl: './user-create.component.html',
  standalone: true,
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    ReactiveFormsModule,
    CommonModule,
  ]
})
export class UserCreateComponent {
  createUserForm: FormGroup;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(private fb: FormBuilder, private userService: UserService, private router: Router) {
    this.createUserForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      role: ['', [Validators.required]]
    });
  }

  onSubmit() {
    if (this.createUserForm.valid) {
      const user: UserCreateRequest = this.createUserForm.value;
      this.userService.createUser(user).subscribe(() => {
        this.router.navigate(['/dashboard/users']);
      }, error => {
        this.errorMessage = error;
      }
    );
    }
  }
}
