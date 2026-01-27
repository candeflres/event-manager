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
export const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
  },
  {
    path: 'home',
    component: Home,
  },
  {
    path: 'register',
    component: Register,
  },
  {
    path: 'login',
    component: Login,
  },
  {
    path: 'forgot-password',
    component: ForgotPassword,
  },
  {
    path: 'verify-code',
    component: VerifyCode,
  },
  {
    path: 'contact',
    component: Contact,
  },
  {
    path: 'message-sent',
    component: MessageSent,
  },
  {
    path: 'home-logged',
    component: HomeLogged,
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
    path: 'profile',
    canActivate: [AuthGuard],
    loadComponent: () => import('./pages/profile/profile').then((m) => m.ProfilePage),
  },
  {
    path: 'know-more',
    component: KnowMoreElement,
  },
  {
    path: 'know-more/element/:id',
    component: KnowMoreElementOptions,
  },
  { path: 'forgot-password', component: ForgotPassword },
  { path: 'verify-code', component: VerifyCode },
  { path: 'reset-password', component: ResetPassword },
  {
    path: '**',
    redirectTo: 'home',
  },
];
