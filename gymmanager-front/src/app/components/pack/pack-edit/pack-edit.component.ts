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
import { PackResponse } from '../../../models/pack-response.model';
import { PackRequest } from '../../../models/pack-request.model';
import { PackService } from '../../../services/pack.service';

@Component({
  selector: 'app-pack-edit',
  templateUrl: './pack-edit.component.html',
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
export class PackEditComponent implements OnInit {
  packForm: FormGroup;
  packId!: number;
  pack!: PackResponse;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private packService: PackService
  ) {
    this.packForm = this.fb.group({
      name: ['', [Validators.required]],
      durationInMonths: [null, [Validators.required, Validators.min(1)]],
      monthlyPrice: [null, [Validators.required, Validators.min(0.01)]]
    });
  }

  ngOnInit() {
    this.packId = Number(this.route.snapshot.paramMap.get('id'));
    this.packService.getPackById(this.packId).subscribe(data => {
      this.pack = data;
      if (this.pack) {
        this.packForm.patchValue(this.pack);
      }
    });
  }

  onSubmit() {
    if (this.packForm.valid) {
      const pack: PackRequest = this.packForm.value;
      this.packService.updatePack(this.packId, pack).subscribe(() => {
        this.router.navigate(['/dashboard/packs']);
      });
    }
  }
}
