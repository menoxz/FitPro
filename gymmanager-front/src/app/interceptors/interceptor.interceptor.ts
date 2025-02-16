import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const authToken = localStorage.getItem('authToken');
    if (authToken) {
      console.log('Token found:', authToken); // Ajout de logs
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${authToken}`,
          'Content-Type': 'application/json'
        }
      });
    } else {
      console.log('No token found'); // Ajout de logs
    }
    console.log('Request URL:', req.url); // Ajout de logs
    console.log('Request Headers:', req.headers.keys()); // Ajout de logs
    return next.handle(req);
  }
}
