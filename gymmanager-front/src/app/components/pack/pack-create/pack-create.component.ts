import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PackRequest } from '../../../models/pack-request.model';
import { PackService } from '../../../services/pack.service';

@Component({
  selector: 'app-pack-create',
  templateUrl: './pack-create.component.html',
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
export class PackCreateComponent {
  packForm: FormGroup;

  constructor(private fb: FormBuilder, private packService: PackService, private router: Router) {
    this.packForm = this.fb.group({
      name: ['', [Validators.required]],
      durationInMonths: [null, [Validators.required, Validators.min(1)]],
      monthlyPrice: [null, [Validators.required, Validators.min(0.01)]]
    });
  }

  onSubmit() {
    if (this.packForm.valid) {
      const pack: PackRequest = this.packForm.value;
      this.packService.addPack(pack).subscribe(() => {
        this.router.navigate(['/dashboard/packs']);
      });
    }
  }
}
