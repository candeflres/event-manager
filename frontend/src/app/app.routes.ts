import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Register } from './pages/register/register';
import { Login } from './pages/login/login';
import { ForgotPassword } from './pages/forgot-password/forgot-password';
import { VerifyCode } from './pages/verify-code/verify-code';
import { Contact } from './pages/contact/contact';
import { MessageSent } from './pages/message-sent/message-sent';
import { HomeLogged } from './pages/home-logged/home-logged';
import { EventList } from './pages/event-list/event-list';
import { EventCreate } from './pages/event-create/event-create';
import { AuthGuard } from './guards/auth.guard';
import { EventDetail } from './pages/event-detail/event-detail';
import { ResetPassword } from './pages/reset-password/reset-password';
import { KnowMoreElement } from './pages/know-more-element/know-more-element';
import { KnowMoreElementOptions } from './pages/know-more-element-options/know-more-element-options';
import { ManageElementsList } from './pages/manage-elements-list/manage-elements-list';
import { ManageElementDetail } from './pages/manage-element-detail/manage-element-detail';
export const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
  },

  { path: 'home', component: Home },
  { path: 'register', component: Register },
  { path: 'login', component: Login },
  { path: 'forgot-password', component: ForgotPassword },
  { path: 'verify-code', component: VerifyCode },
  { path: 'reset-password', component: ResetPassword },
  { path: 'contact', component: Contact },
  { path: 'message-sent', component: MessageSent },
  { path: 'know-more', component: KnowMoreElement },
  { path: 'know-more/element/:id', component: KnowMoreElementOptions },

  { path: 'home-logged', component: HomeLogged },
  {
    path: 'profile',
    canActivate: [AuthGuard],
    loadComponent: () => import('./pages/profile/profile').then((m) => m.ProfilePage),
  },

  {
    path: 'event-list',
    component: EventList,
    canActivate: [AuthGuard],
  },
  {
    path: 'event-create',
    component: EventCreate,
    canActivate: [AuthGuard],
  },
  {
    path: 'event-detail/:id',
    component: EventDetail,
    canActivate: [AuthGuard],
  },
  {
    path: 'event/:id',
    loadComponent: () => import('./pages/event-public/event-public').then((m) => m.EventPublic),
  },

  {
    path: 'manage-elements',
    canActivate: [AuthGuard],
    component: ManageElementsList,
  },
  {
    path: 'manage-elements/create',
    canActivate: [AuthGuard],
    loadComponent: () =>
      import('./pages/create-element/create-element').then((m) => m.CreateElement),
  },
  {
    path: 'manage-elements/:id',
    canActivate: [AuthGuard],
    component: ManageElementDetail,
  },

  // ==========================
  // ===== ADMIN SECTION =====
  // ==========================

  {
    path: 'admin/audit',
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () => import('./pages/admin-audit/admin-audit').then((m) => m.AdminAudit),
  },

  {
    path: 'admin/users',
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () =>
      import('./pages/admin-users-list/admin-users-list').then((m) => m.AdminUsersList),
  },
  {
    path: 'admin/users/:id',
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () =>
      import('./pages/admin-employee-detail/admin-employee-detail').then(
        (m) => m.AdminEmployeeDetail,
      ),
  },

  {
    path: 'admin/employees',
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () =>
      import('./pages/admin-employees-list/admin-employees-list').then((m) => m.AdminEmployeesList),
  },
  {
    path: 'admin/employees/new',
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () =>
      import('./pages/employee-create/employee-create').then((m) => m.AdminEmployeeCreate),
  },
  {
    path: 'admin/employees/:id',
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () =>
      import('./pages/admin-employee-detail/admin-employee-detail').then(
        (m) => m.AdminEmployeeDetail,
      ),
  },

  {
    path: 'admin/stats',
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () => import('./pages/admin-stats/admin-stats').then((m) => m.AdminStats),
  },
  {
    path: '**',
    redirectTo: 'home',
  },
];
