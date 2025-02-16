import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { CustomerRequest } from '../models/customer-request.model';
import { CustomerResponse } from '../models/customer-response.model';

@Injectable({ providedIn: 'root' })
export class CustomerService {
  private apiUrl = 'http://localhost:8080/api/customers';

  constructor(private http: HttpClient) {}

  getAllCustomers(page: number, size: number): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(this.apiUrl, { params });
  }
  getAllCustomersWithoutPagination(): Observable<CustomerResponse[]> {
    return this.http.get<CustomerResponse[]>(`${this.apiUrl}/all`);
  }

  searchCustomers(query: string, page: number, size: number): Observable<any> {
    const params = new HttpParams().set('query', query).set('page', page).set('size', size);
    return this.http.get<any>(`${this.apiUrl}/search`, { params });
  }

  getCustomerById(id: number): Observable<CustomerResponse> {
    return this.http.get<CustomerResponse>(`${this.apiUrl}/${id}`);
  }

  addCustomer(customer: CustomerRequest): Observable<CustomerResponse> {
    return this.http.post<CustomerResponse>(this.apiUrl, customer).pipe(catchError(this.handleError));
  }

  updateCustomer(id: number, customer: CustomerRequest): Observable<CustomerResponse> {
    return this.http.put<CustomerResponse>(`${this.apiUrl}/${id}`, customer);
  }

  deleteCustomer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(catchError(this.handleError));
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
