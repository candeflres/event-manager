import { OptionResponse } from './option-response';
export interface EventResponse {
  id: number;
  name: string;
  description: string;
  eventDate: string;
  status: string;
  estimatedBudget: number;
  options: OptionResponse[];
}
