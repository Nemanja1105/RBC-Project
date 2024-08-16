import { inject, Pipe, PipeTransform } from '@angular/core';
import { CurrencyService } from '../services/currency.service';

@Pipe({
  name: 'currencyConverter',
  standalone: true,
  pure: true,
})
export class CurrencyConverterPipe implements PipeTransform {
  currencyService = inject(CurrencyService);
  transform(amount: number, fromCurrency: string): number {
    return this.currencyService.convertAmount(amount, fromCurrency);
  }
}
