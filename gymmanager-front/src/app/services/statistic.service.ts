import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StatisticsResponse } from '../models/statistics-response.model';
import { SubscriptionResponse } from '../models/subscription-response.model';

@Injectable({ providedIn: 'root' })
export class StatisticsService {
  private apiUrl = 'http://localhost:8080/api/statistics';

  constructor(private http: HttpClient) {}

  getActiveCustomersCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/active-customers`);
  }

  getMonthlyRevenue(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/monthly-revenue`);
  }

  exportSubscriptions(startDate: string, endDate: string): Observable<SubscriptionResponse[]> {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);
    return this.http.get<SubscriptionResponse[]>(`${this.apiUrl}/subscriptions/export`, { params });
  }

  getSubscriptionsByMonth(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/subscriptions-by-month`);
  }

  getCustomersCountByPack(): Observable<Map<string, number>> {
    return this.http.get<Map<string, number>>(`${this.apiUrl}/customers-count-by-pack`);
  }
}
interface SubscriptionsByMonth {
  month: number;
  count: number;
}

interface RevenueByMonth {
  year: number;
  month: number;
  revenue: number;
}
