import { OptionResponse } from './option-response';

export interface ElementResponse {
  id: number;
  name: string;
  description: string;
  available: boolean;
  options: OptionResponse[];
}
