import { Component, inject, OnInit } from '@angular/core';
import { TitleWrapperComponent } from '../../../../shared/components/title-wrapper/title-wrapper.component';
import { DataViewModule } from 'primeng/dataview';
import { TransactionsDataViewItemsComponent } from '../../components/transactions-data-view-items/transactions-data-view-items.component';
import { TransactionDTO } from '../../../../models/transactions';
import { TransactionService } from '../../services/transaction.service';
import { DropdownModule } from 'primeng/dropdown';
import { AccountDTO } from '../../../../models/account';
import { AccountService } from '../../../account/services/account.service';
import { DropdownOption } from '../../../../models/page';
import { FormBuilder, FormControl, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged, Observable } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { TransactionFooterComponent } from '../../components/transaction-footer/transaction-footer.component';
import { CurrencyService } from '../../../../shared/services/currency.service';
import { UpperCasePipe } from '@angular/common';
import { CurrencySumPipe } from '../../../../shared/directives/sum.pipe';

@Component({
  selector: 'app-transaction-main-page',
  standalone: true,
  imports: [
    TitleWrapperComponent,
    DataViewModule,
    TransactionsDataViewItemsComponent,
    DropdownModule,
    ReactiveFormsModule,
    TransactionFooterComponent,
    UpperCasePipe,
    CurrencySumPipe,
  ],
  templateUrl: './transaction-main-page.component.html',
  styleUrl: './transaction-main-page.component.scss',
})
export class TransactionMainPageComponent implements OnInit {
  isLoading: boolean = false;
  transactions: TransactionDTO[] = [];
  accounts: AccountDTO[] = [];
  defaultOption: DropdownOption = { label: 'All accounts', value: null };
  accountDropdownOptions: DropdownOption[] = [this.defaultOption];
  numOfRows: number = 5;
  formChanged$: Observable<any>;
  defaultCurrency: string = '';
  accounts$: Observable<AccountDTO[]>;

  transactionService = inject(TransactionService);
  accountService = inject(AccountService);
  formBuilder = inject(FormBuilder);
  currencyService = inject(CurrencyService);

  formData = new FormControl<AccountDTO | null>(null);

  constructor() {
    this.formChanged$ = this.formData.valueChanges.pipe(
      debounceTime(400),
      distinctUntilChanged(),
      takeUntilDestroyed()
    );
    this.defaultCurrency = this.currencyService.getDefaultCurrency();
    this.accounts$ = this.accountService.getAccountsObservable();
  }

  ngOnInit(): void {
    this.loadTransactions(null);
    this.loadAccounts();
    this.formChanged$.subscribe(() => {
      if (this.formData.valid) {
        this.loadTransactions(this.formData.value?.id || null);
      }
    });
  }

  loadTransactions(id: number | null) {
    let observable: Observable<any>;
    if (!id) observable = this.transactionService.findAll();
    else observable = this.transactionService.findAllByAccountId(id!);
    this.isLoading = true;
    observable.subscribe({
      next: (data) => {
        this.transactions = data;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  loadAccounts() {
    this.accounts$.subscribe({
      next: (data) => {
        this.accounts = data;
        let tmp: DropdownOption[] = data.map((el: AccountDTO) => {
          return { label: el.name, value: el };
        });
        tmp.unshift(this.defaultOption);
        this.accountDropdownOptions = tmp;
      },
    });
  }

  onSubmit(transaction: TransactionDTO | null) {
    if (transaction) this.transactions = [...this.transactions, transaction];
  }
}
