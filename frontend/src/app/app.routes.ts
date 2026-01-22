import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Register } from './pages/register/register';
import { Login } from './pages/login/login';
import { ForgotPassword } from './pages/forgot-password/forgot-password';
import { VerifyCode } from './pages/verify-code/verify-code';
import { Contact } from './pages/contact/contact';
import { MessageSent } from './pages/message-sent/message-sent';
import { HomeLogged } from './pages/home-logged/home-logged';

export const routes: Routes = [
  {
    path: '',
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
];
