import { UserResponse } from './../models/user-response.model';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { JwtResponse } from '../models/jwt-response.model';
import { MessageResponse } from '../models/message-response.model';
import { UserCreateRequest } from '../models/user-create-request.model';
import { jwtDecode } from 'jwt-decode'; // Utilisez des accolades pour l'importation
import { catchError } from 'rxjs/operators';


@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private userApiUrl = 'http://localhost:8080/api/users';


  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(`${this.apiUrl}/signin`, { username, password }).pipe(
      tap(response => {
        localStorage.setItem('authToken', response.token);
        console.log('Token stored:', response.token);
      }),
      catchError(this.handleError)

    );
  }

  signup(user: UserCreateRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${this.apiUrl}/signup`, user);
  }

  logout() {
    localStorage.removeItem('authToken');
  }

  changePassword(currentPassword: string, newPassword: string): Observable<MessageResponse> {
    return this.http.put<MessageResponse>(`${this.apiUrl}/change-password`, { currentPassword, newPassword });
  }

  isLoggedIn(): boolean {
    const token = localStorage.getItem('authToken');
    console.log('Checking if logged in:', token); // Ajout de logs
    return !!token;
  }

  getCurrentUser(): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.userApiUrl}/me`);
  }

  getUserRoles(): string[] {
    const token = localStorage.getItem('authToken');
    if (token) {
      const decodedToken: JwtResponse = jwtDecode(token);
      console.log('User roles:', decodedToken); // Ajout de logs
      return decodedToken.roles;
    }
    return [];
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 400) {
      return throwError(() => error.error.message);
    }
    return throwError(() => 'Une erreur est survenue');
  }

  // getUserRoles(): string[] {
  //   const token = localStorage.getItem('authToken');
  //   if (token) {
  //     const decodedToken: JwtResponse = JSON.parse(token);
  //     console.log('User roles:', decodedToken); // Ajout de logs
  //     return decodedToken.roles || [];
  //   }
  //   return [];
  // }


}
