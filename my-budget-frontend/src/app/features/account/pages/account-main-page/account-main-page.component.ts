import { Component, inject, OnInit } from '@angular/core';
import { TitleWrapperComponent } from '../../../../shared/components/title-wrapper/title-wrapper.component';
import { ButtonModule } from 'primeng/button';
import { DataViewModule } from 'primeng/dataview';
import { AccountDTO } from '../../../../models/account';
import { NgClass, UpperCasePipe } from '@angular/common';
import { AccountDataViewItemsComponent } from '../../components/account-data-view-items/account-data-view-items.component';
import { AccountService } from '../../services/account.service';
import { AddAccountDialogComponent } from '../../components/add-account-dialog/add-account-dialog.component';
import { TransactionFooterComponent } from '../../../transaction/components/transaction-footer/transaction-footer.component';
import { CurrencyService } from '../../../../shared/services/currency.service';
import { CurrencySumPipe } from '../../../../shared/directives/sum.pipe';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-account-main-page',
  standalone: true,
  imports: [
    TitleWrapperComponent,
    ButtonModule,
    DataViewModule,
    NgClass,
    UpperCasePipe,
    AccountDataViewItemsComponent,
    AddAccountDialogComponent,
    TransactionFooterComponent,
    CurrencySumPipe,
  ],
  templateUrl: './account-main-page.component.html',
  styleUrl: './account-main-page.component.scss',
})
export class AccountMainPageComponent implements OnInit {
  accountService = inject(AccountService);
  currencyService = inject(CurrencyService);

  isLoading: boolean = false;
  defaultCurrency: string = '';
  visibilityOfAddDialog: boolean = false;
  accounts: AccountDTO[] = [];
  accounts$: Observable<AccountDTO[]>;

  constructor() {
    this.accounts$ = this.accountService.getAccountsObservable();
  }

  ngOnInit(): void {
    this.defaultCurrency = this.currencyService.getDefaultCurrency();
    this.accounts$.subscribe({
      next: (data) => {
        this.accounts = data;
      },
    });
  }

  openDialog() {
    this.visibilityOfAddDialog = true;
  }

  onDialogClose() {
    this.visibilityOfAddDialog = false;
  }
}
