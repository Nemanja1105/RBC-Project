export type Currency = {
  code: string;
  name: string;
};

export type ExchangeRatesResponse = {
  date: string;
  [baseCurrency: string]: Record<string, number> | string;
};

export type ExchangeRates = {
  date: string;
  rates: Record<string, number>;
};
