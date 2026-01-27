import { BotOption } from './bot-option.model';

export interface BotResponse {
  message: string;
  options: BotOption[];
}
