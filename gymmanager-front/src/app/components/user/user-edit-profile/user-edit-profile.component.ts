import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserUpdateRequest } from '../../../models/user-update-request.model';
import { UserResponse } from '../../../models/user-response.model';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-user-edit-profile',
  templateUrl: './user-edit-profile.component.html',
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
export class UserEditProfileComponent implements OnInit {
  editProfileForm: FormGroup;
  userId!: number;
  user!: UserResponse;
  succesMessage!: string;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private userService: UserService
  ) {
    this.editProfileForm = this.fb.group({
      email: ['', [ Validators.email,Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/)]],
      password: ['', [ Validators.minLength(6), Validators.maxLength(40)]],
      phoneNumber: ['']
    });
  }

  ngOnInit() {
    this.userId = Number(this.route.snapshot.paramMap.get('id'));

    console.log(this.userId, 'userId', this.user);
    this.userService.getAllUsers(0, 1).subscribe(data => {
      this.user = data.content.find((u: UserResponse) => u.id === this.userId);
      if (this.user) {
        this.editProfileForm.patchValue(this.user);
        this.editProfileForm = this.fb.group({email:this.user.email, phoneNumber:this.user.phoneNumber});
      }
    });

    this.authService.getCurrentUser().subscribe(user => {
      this.editProfileForm = this.fb.group({email: user.email, phoneNumber: user.phoneNumber})
      console.log('User ID:', this.userId);
    });
  }

  onSubmit() {
    if (this.editProfileForm.valid) {
      const user: UserUpdateRequest = this.editProfileForm.value;
      console.log(user, this.userId);
      this.userService.updateUserProfile(this.userId, user).subscribe(() => {
        // this.router.navigate(['/dashboard']);
        this.succesMessage = 'Profil mis à jour avec succès!';

      });
    }
  }
}
