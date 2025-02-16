import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { UserResponse } from '../../../models/user-response.model';
import { UserService } from '../../../services/user.service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth.service';


@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  standalone: true,
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    ReactiveFormsModule,
    CommonModule
  ]
})
export class UserEditComponent implements OnInit {
  editUserForm: FormGroup;
  userId!: number;
  user!: UserResponse;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private authService: AuthService
  ) {
    this.editUserForm = this.fb.group({
      username: [{value:'', disabled: true}, ],
      password: [''],
      role: ['', [Validators.required]]
    });
  }

  ngOnInit() {
    this.userId = Number(this.route.snapshot.paramMap.get('id'));
    this.userService.getAllUsers(0, 1).subscribe(data => {
      this.user = data.content.find((u: UserResponse) => u.id === this.userId);
      if (this.user) {
        this.editUserForm.patchValue(this.user);
        this.editUserForm= this.fb.group({role: this.user.roles});
      }
    });

  }

  onSubmit() {
    if (this.editUserForm.valid) {
      const newRole = this.editUserForm.value.role;
      this.userService.updateUserRole(this.userId, newRole).subscribe(() => {
        this.router.navigate(['/dashboard/users']);
      });
    }
  }
}
