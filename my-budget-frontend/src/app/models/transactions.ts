import { AccountDTO } from './account';

export type TransactionDTO = {
  id: number;
  description: string;
  type: string;
  amount: number;
  account: AccountDTO;
};

export type TransactionRequestDTO = {
  description: string;
  type: number;
  amount: number;
};
