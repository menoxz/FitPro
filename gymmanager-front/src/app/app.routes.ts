import { Route } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { OverviewComponent } from './components/overview/overview.component';
import { CustomerCreateComponent } from './components/customer/customer-create/customer-create.component';
import { CustomerDetailsComponent } from './components/customer/customer-details/customer-details.component';
import { CustomerEditComponent } from './components/customer/customer-edit/customer-edit.component';
import { CustomerListComponent } from './components/customer/customer-list/customer-list.component';
import { UserCreateComponent } from './components/user/user-create/user-create.component';
import { UserEditProfileComponent } from './components/user/user-edit-profile/user-edit-profile.component';
import { UserEditComponent } from './components/user/user-edit/user-edit.component';
import { UserListComponent } from './components/user/user-list/user-list.component';
import { PackCreateComponent } from './components/pack/pack-create/pack-create.component';
import { PackEditComponent } from './components/pack/pack-edit/pack-edit.component';
import { PackListComponent } from './components/pack/pack-list/pack-list.component';
import { SubscriptionCreateComponent } from './components/subscription/subscription-create/subscription-create.component';
import { SubscriptionListComponent } from './components/subscription/subscription-list/subscription-list.component';
import { StatisticsComponent } from './components/statistic/statistic.component';
import { canActivateAuthGuard,  } from './guards/auth-guard.guard';
import { canActivateStaffGuard } from './guards/staff-guard.guard';

export const routes: Route[] = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  // { path: 'register', component: RegisterComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [canActivateAuthGuard],
    children: [
      { path: '', redirectTo: 'overview', pathMatch: 'full' },
      { path: 'overview', component: OverviewComponent },
      { path: 'users', component: UserListComponent, canActivate: [canActivateStaffGuard] },
      { path: 'users/create', component: UserCreateComponent, canActivate: [canActivateStaffGuard] },
      { path: 'users/edit/:id', component: UserEditComponent, canActivate: [canActivateStaffGuard] },
      { path: 'profile/edit/:id', component: UserEditProfileComponent, canActivate: [canActivateAuthGuard] },
      { path: 'customers', component: CustomerListComponent, canActivate: [canActivateAuthGuard] },
      { path: 'customers/create', component: CustomerCreateComponent, canActivate: [canActivateAuthGuard] },
      { path: 'customers/edit/:id', component: CustomerEditComponent, canActivate: [canActivateAuthGuard] },
      { path: 'customers/details/:id', component: CustomerDetailsComponent, canActivate: [canActivateAuthGuard] },
      { path: 'packs', component: PackListComponent, canActivate: [canActivateAuthGuard] },
      { path: 'packs/create', component: PackCreateComponent, canActivate: [canActivateAuthGuard] },
      { path: 'packs/edit/:id', component: PackEditComponent, canActivate: [canActivateAuthGuard] },
      { path: 'subscriptions', component: SubscriptionListComponent, canActivate: [canActivateAuthGuard] },
      { path: 'subscriptions/create', component: SubscriptionCreateComponent, canActivate: [canActivateAuthGuard] },
      { path: 'statistics', component: StatisticsComponent, canActivate: [canActivateStaffGuard] }
     ]
  }
];
