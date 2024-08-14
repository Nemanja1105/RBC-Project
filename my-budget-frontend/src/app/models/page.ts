import { AccountDTO } from './account';

export type Page<T> = {
  totalElements: number;
  content: T[];
};

export type DropdownOption = {
  value: AccountDTO | null;
  label: string;
};
