export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  password: string;
}

export interface NewUser {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  password: string;
}

export interface credentials {
  email: string;
  password: string;
}

