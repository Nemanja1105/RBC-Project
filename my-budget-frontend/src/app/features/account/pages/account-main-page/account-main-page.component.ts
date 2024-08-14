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
import { TransactionDTO } from '../../../../models/transactions';

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
  ],
  templateUrl: './account-main-page.component.html',
  styleUrl: './account-main-page.component.scss',
})
export class AccountMainPageComponent implements OnInit {
  accountService = inject(AccountService);
  isLoading: boolean = false;
  visibilityOfAddDialog: boolean = false;
  accounts: AccountDTO[] = [];

  ngOnInit(): void {
    this.getPage();
  }

  getPage() {
    this.isLoading = true;
    this.accountService.findAll().subscribe({
      next: (data) => {
        if (!data) return;
        this.accounts = data;
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
    if (account) this.accounts = [...this.accounts, account];
  }

  //a better approach is to use global storage for the account instead of prop drilling
  onTransactionSubmit(transaction: TransactionDTO | null) {
    if (transaction && transaction?.account)
      this.accounts = this.accounts.map((el) =>
        el.id != transaction?.account.id ? el : transaction?.account
      );
  }
}
