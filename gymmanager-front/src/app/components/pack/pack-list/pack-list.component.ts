import { Component, OnInit } from '@angular/core';

import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { ViewChild } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { PackResponse } from '../../../models/pack-response.model';
import { PackService } from '../../../services/pack.service';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-pack-list',
  templateUrl: './pack-list.component.html',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
    RouterLink
  ]
})
export class PackListComponent implements OnInit {
  displayedColumns: string[] = ['id', 'name', 'durationInMonths', 'monthlyPrice', 'actions'];
  dataSource!: MatTableDataSource<PackResponse>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  errorMessage!: string;

  constructor(private packService: PackService, private router: Router) {}

  ngOnInit() {
    this.loadPacks();
  }

  loadPacks() {
    this.packService.getAllPacks().subscribe(data => {
      this.dataSource = new MatTableDataSource(data);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  addPack() {
    this.router.navigate(['/packs/create']);
  }

  editPack(packId: number) {
    this.router.navigate(['/packs/edit', packId]);
  }

  deletePack(packId: number) {
    if (!confirm('Are you sure you want to delete this pack?')) {
      return;
    }else{
    this.packService.deletePack(packId).subscribe(() => {
      this.loadPacks();
    }, error =>{
      this.errorMessage = "can not delete pack, pack is in use";
    }
  );
  }}
}
