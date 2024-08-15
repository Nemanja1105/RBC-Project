import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environments';
import { BehaviorSubject, catchError, first, map, of, tap } from 'rxjs';
import { MessageService } from 'primeng/api';
import {
  Currency,
  ExchangeRates,
  ExchangeRatesResponse,
} from '../../models/currency';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Injectable({
  providedIn: 'root',
})
export class CurrencyService {
  private defaultCurrencyKey = 'default_currency';

  private http = inject(HttpClient);
  private messageService = inject(MessageService);

  private currenciesSubject = new BehaviorSubject<Currency[]>([]);
  private exchangeRatesSubject = new BehaviorSubject<ExchangeRates | null>(
    null
  );

  getCurrencies() {
    return this.currenciesSubject.asObservable().pipe(takeUntilDestroyed());
  }

  getExchangeRatesObservable() {
    return this.exchangeRatesSubject.asObservable().pipe(takeUntilDestroyed());
  }

  fetchCurrencies() {
    return this.http.get(`${environment.currencyApiUrl}/currencies.json`).pipe(
      first(),
      map((el: any) => {
        return Object.keys(el)
          .map((k) => {
            return { code: k, name: el[k] };
          })
          .filter((t) => t.name != '');
      }),
      tap((currencies) => {
        this.currenciesSubject.next(currencies);
      }),
      catchError((error) => {
        return of([]);
      })
    );
  }

  fetchExchangeRate() {
    const curr = this.getDefaultCurrency();
    return this.http
      .get(`${environment.currencyApiUrl}/currencies/${curr}.json`)
      .pipe(
        first(),
        map((response) => {
          let tmp = response as ExchangeRatesResponse;
          return { date: tmp.date, rates: tmp[curr!] } as ExchangeRates;
        }),
        tap((rate) => {
          this.exchangeRatesSubject.next(rate);
        }),
        catchError((error) => {
          return of([]);
        })
      );
  }

  getDefaultCurrency(): string {
    let tmp = localStorage.getItem(this.defaultCurrencyKey);
    if (tmp) return tmp;
    this.setDefaultCurrency(environment.defaultCurrency);
    return environment.defaultCurrency;
  }

  changeCurrency(currency: Currency) {
    this.setDefaultCurrency(currency.code);
    this.fetchExchangeRate().subscribe();
  }

  setDefaultCurrency(currency: string) {
    localStorage.setItem(this.defaultCurrencyKey, currency);
  }

  convertAmount(amount: number, fromCurrency: string): number {
    if (fromCurrency == this.getDefaultCurrency()) return amount;
    const exchangeRates = this.exchangeRatesSubject.getValue();
    const fromRate = exchangeRates?.rates[fromCurrency];

    if (!fromRate) return NaN;
    return amount / fromRate;
  }
}
