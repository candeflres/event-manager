import { EventStatus } from './event-status';
import { OptionResponse } from './option-response';
export interface EventResponse {
  id: number;
  name: string;
  description: string;
  eventDate: string;
  status: EventStatus;
  estimatedBudget: number;
  options: OptionResponse[];
}
