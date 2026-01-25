export interface UserProfile {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  role: 'CLIENT' | 'EMPLOYEE' | 'ADMIN';
  active: boolean;
  created: string;
}
