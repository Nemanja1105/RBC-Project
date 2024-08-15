export type AccountDTO = {
  id: number;
  name: string;
  balance: number;
  currency: string;
};

export type AccountDTOWithConvertedCurrency = {
  account: AccountDTO;
  convertedBalance: number;
};

export type AccountRequestDTO = {
  name: string;
  balance: number;
  currency: string;
};
