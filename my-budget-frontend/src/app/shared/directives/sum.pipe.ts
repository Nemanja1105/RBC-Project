import { inject, Pipe, PipeTransform } from '@angular/core';
import {
  AccountDTO,
  AccountDTOWithConvertedCurrency,
} from '../../models/account';
import { CurrencyService } from '../services/currency.service';

@Pipe({
  name: 'currencySum',
  standalone: true,
  pure: true,
})
export class CurrencySumPipe implements PipeTransform {
  currencyService = inject(CurrencyService);
  transform(value: AccountDTO[]): number {
    if (!Array.isArray(value)) {
      return 0;
    }
    return value.reduce(
      (acc, num) =>
        acc + this.currencyService.convertAmount(num.balance, num.currency),
      0
    );
  }
}
