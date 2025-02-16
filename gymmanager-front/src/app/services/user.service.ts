import { catchError } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { UserCreateRequest } from '../models/user-create-request.model';
import { UserResponse } from '../models/user-response.model';
import { UserUpdateRequest } from '../models/user-update-request.model';

@Injectable({ providedIn: 'root' })
export class UserService {
  private apiUrl = 'http://localhost:8080/api/admin/users';
  private userApiUrl = 'http://localhost:8080/api/users';


  constructor(private http: HttpClient) {}

  createUser(user: UserCreateRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(this.apiUrl, user).pipe(catchError(this.handleError));
  }

  updateUserRole(userId: number, newRole: string): Observable<UserResponse> {
    const params = new HttpParams().set('newRole', newRole);
    return this.http.patch<UserResponse>(`${this.apiUrl}/${userId}/role`, {}, { params });
  }

  deleteUser(userId: number): Observable<void> {
    console.log('delete userr?');
    return this.http.delete<void>(`${this.apiUrl}/${userId}`);
  }

  getAllUsers(page: number, size: number): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(this.apiUrl, { params });
  }

  updateUserProfile(userId: number, user: UserUpdateRequest): Observable<UserResponse> {
    return this.http.patch<UserResponse>(`${this.userApiUrl}/${userId}`, user);
  }
  getAllUsersWithoutPagination(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(`${this.apiUrl}?page=0&size=1000`); // Adjust size as needed
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 400) {
      return throwError(() => error.error.message);
    }
    if (error.status === 404) {
      return throwError(() => error.error.message);
    }
    if (error.status === 403) {
      return throwError(() => error.error.message);
    }
    if (error.status === 500) {
      return throwError(() => 'Erreur interne du serveur');
    }

    return throwError(() => 'Une erreur est survenue');
  }
}
