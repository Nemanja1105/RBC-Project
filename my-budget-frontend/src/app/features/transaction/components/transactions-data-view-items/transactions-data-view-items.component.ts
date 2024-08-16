import { DecimalPipe, NgClass, UpperCasePipe } from '@angular/common';
import { Component, inject, Input } from '@angular/core';
import { SkeletonModule } from 'primeng/skeleton';
import { TransactionDTO } from '../../../../models/transactions';
import { CurrencyConverterPipe } from '../../../../shared/directives/currency-converter.pipe';
import { CurrencyService } from '../../../../shared/services/currency.service';

@Component({
  selector: 'app-transactions-data-view-items',
  standalone: true,
  imports: [
    NgClass,
    SkeletonModule,
    UpperCasePipe,
    DecimalPipe,
    CurrencyConverterPipe,
  ],
  templateUrl: './transactions-data-view-items.component.html',
  styleUrl: './transactions-data-view-items.component.scss',
})
export class TransactionsDataViewItemsComponent {
  @Input()
  transactions: TransactionDTO[] | undefined;
  @Input()
  isLoading: boolean = true;
  @Input()
  numOfRows: number = 5;
  defaultCurrency: string = '';
  currencyService = inject(CurrencyService);

  constructor() {
    this.defaultCurrency = this.currencyService.getDefaultCurrency();
  }
}
