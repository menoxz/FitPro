import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PackRequest } from '../models/pack-request.model';
import { PackResponse } from '../models/pack-response.model';

@Injectable({ providedIn: 'root' })
export class PackService {
  private apiUrl = 'http://localhost:8080/api/packs';

  constructor(private http: HttpClient) {}

  getAllPacks(): Observable<PackResponse[]> {
    return this.http.get<PackResponse[]>(this.apiUrl);
  }

  getPackById(id: number): Observable<PackResponse> {
    return this.http.get<PackResponse>(`${this.apiUrl}/${id}`);
  }

  addPack(pack: PackRequest): Observable<PackResponse> {
    return this.http.post<PackResponse>(this.apiUrl, pack);
  }

  updatePack(id: number, pack: PackRequest): Observable<PackResponse> {
    return this.http.put<PackResponse>(`${this.apiUrl}/${id}`, pack);
  }

  deletePack(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
