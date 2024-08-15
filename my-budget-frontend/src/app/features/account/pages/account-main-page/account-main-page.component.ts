import {
  ChangeDetectionStrategy,
  Component,
  inject,
  OnInit,
} from '@angular/core';
import { TitleWrapperComponent } from '../../../../shared/components/title-wrapper/title-wrapper.component';
import { ButtonModule } from 'primeng/button';
import { DataViewModule } from 'primeng/dataview';
import {
  AccountDTO,
  AccountDTOWithConvertedCurrency,
} from '../../../../models/account';
import { NgClass, UpperCasePipe } from '@angular/common';
import { AccountDataViewItemsComponent } from '../../components/account-data-view-items/account-data-view-items.component';
import { AccountService } from '../../services/account.service';
import { AddAccountDialogComponent } from '../../components/add-account-dialog/add-account-dialog.component';
import { TransactionFooterComponent } from '../../../transaction/components/transaction-footer/transaction-footer.component';
import { TransactionDTO } from '../../../../models/transactions';
import { CurrencyService } from '../../../../shared/services/currency.service';
import { SumPipe } from '../../../../shared/directives/sum.pipe';

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
    SumPipe,
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
  accountsWithBalance: AccountDTOWithConvertedCurrency[] = [];

  ngOnInit(): void {
    this.defaultCurrency = this.currencyService.getDefaultCurrency();
    this.getPage();
  }

  getPage() {
    this.isLoading = true;
    this.accountService.findAll().subscribe({
      next: (data) => {
        if (!data) return;
        this.accounts = data;
        this.accountsWithBalance = data.map((el: AccountDTO) => {
          let converted = this.currencyService.convertAmount(
            el.balance,
            el.currency
          );
          return {
            account: el,
            convertedBalance: converted,
          };
        });
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  openDialog() {
    this.visibilityOfAddDialog = true;
  }

  onDialogClose() {
    this.visibilityOfAddDialog = false;
  }

  onSubmit(account: AccountDTO | null) {
    if (account) {
      this.accounts = [...this.accounts, account];
      this.accountsWithBalance = [
        ...this.accountsWithBalance,
        {
          account: account,
          convertedBalance: this.currencyService.convertAmount(
            account.balance,
            account.currency
          ),
        },
      ];
    }
  }

  //a better approach is to use global storage for the account instead of prop drilling
  onTransactionSubmit(transaction: TransactionDTO | null) {
    if (transaction && transaction?.account)
      // this.accounts = this.accounts.map((el) =>
      //   el.id != transaction?.account.id ? el : transaction?.account
      // );
      this.accountsWithBalance = this.accountsWithBalance.map((el) =>
        el.account.id != transaction?.account.id
          ? el
          : {
              account: transaction?.account,
              convertedBalance: this.currencyService.convertAmount(
                transaction?.account?.balance,
                transaction?.account?.currency
              ),
            }
      );
  }
}
