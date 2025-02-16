import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { SubscriptionRequest } from '../models/subscription-request.model';
import { SubscriptionResponse } from '../models/subscription-response.model';

@Injectable({ providedIn: 'root' })
export class SubscriptionService {
  private apiUrl = 'http://localhost:8080/api/subscriptions';

  constructor(private http: HttpClient) {}

  subscribe(request: SubscriptionRequest): Observable<SubscriptionResponse> {
    return this.http.post<SubscriptionResponse>(this.apiUrl, request).pipe(
      catchError(this.handleError)
    );;
  }

  getAll(): Observable<SubscriptionResponse[]> {
    return this.http.get<SubscriptionResponse[]>(`${this.apiUrl}`);
  }

  getAllSubscriptions(page: number, size: number): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(`${this.apiUrl}/pages`, { params });
  }
  getCustomerSubscriptions(customerId: number): Observable<SubscriptionResponse[]> {
    return this.http.get<SubscriptionResponse[]>(`${this.apiUrl}/customer/${customerId}`);
  }

  searchSubscription(query: string, page: number, size: number): Observable<any> {
    const params = new HttpParams().set('query', query).set('page', page).set('size', size);
    return this.http.get<any>(`${this.apiUrl}/search`, { params });
  }

  cancelSubscription(subscriptionId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${subscriptionId}`);
  }

  checkSubscriptionStatus(subscriptionId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/${subscriptionId}/status`);
  }

  exportSubscriptions(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export`, { responseType: 'blob' });
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
    return throwError(() => 'Une erreur est survenue');
  }
}
