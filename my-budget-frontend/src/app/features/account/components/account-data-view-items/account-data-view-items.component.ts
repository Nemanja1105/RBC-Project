import {
  ChangeDetectionStrategy,
  Component,
  inject,
  Input,
} from '@angular/core';
import {
  AccountDTO,
  AccountDTOWithConvertedCurrency,
} from '../../../../models/account';
import { DecimalPipe, NgClass, UpperCasePipe } from '@angular/common';
import { SkeletonModule } from 'primeng/skeleton';
import { CurrencyService } from '../../../../shared/services/currency.service';

@Component({
  selector: 'app-account-data-view-items',
  standalone: true,
  imports: [NgClass, UpperCasePipe, SkeletonModule, DecimalPipe],
  templateUrl: './account-data-view-items.component.html',
  styleUrl: './account-data-view-items.component.scss',
})
export class AccountDataViewItemsComponent {
  @Input()
  accounts: AccountDTOWithConvertedCurrency[] | undefined;
  @Input()
  isLoading: boolean = true;
  @Input()
  numOfRows: number = 5;
  defaultCurrency: string | null = '';

  currencyService = inject(CurrencyService);

  constructor() {
    this.defaultCurrency = this.currencyService.getDefaultCurrency();
  }
}
