import { Pipe, PipeTransform } from '@angular/core';
import { AccountDTOWithConvertedCurrency } from '../../models/account';

@Pipe({
  name: 'sum',
  standalone: true,
  pure: true,
})
export class SumPipe implements PipeTransform {
  transform(value: AccountDTOWithConvertedCurrency[]): number {
    if (!Array.isArray(value)) {
      return 0;
    }
    return value.reduce((acc, num) => acc + num.convertedBalance, 0);
  }
}
