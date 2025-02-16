import { Injectable } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class StaffGuard {
  canActivate(): boolean {
    const authService = inject(AuthService);
    const router = inject(Router);
    if (authService.isLoggedIn()) {
      const roles = authService.getUserRoles();
      console.log(roles);
      if (roles.includes('ROLE_ADMIN')) {
        return true;
      } else {
        router.navigate(['/dashboard']);
        return false;
      }
    } else {
      router.navigate(['/login']);
      return false;
    }
  }
}

export const canActivateStaffGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  console.log('staffGuard');
  if (authService.isLoggedIn()) {
    const roles = authService.getUserRoles();
    if (roles.includes('ROLE_ADMIN')) {
      return true;
    } else {
      router.navigate(['/dashboard']);
      return false;
    }
  } else {
    router.navigate(['/login']);
    return false;
  }
};
