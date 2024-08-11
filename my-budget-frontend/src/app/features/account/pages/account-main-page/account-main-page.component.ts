import { Component, inject, OnInit } from '@angular/core';
import { TitleWrapperComponent } from '../../../../shared/components/title-wrapper/title-wrapper.component';
import { ButtonModule } from 'primeng/button';
import { DataViewModule } from 'primeng/dataview';
import { AccountDTO } from '../../../../models/account-dto';
import { NgClass, UpperCasePipe } from '@angular/common';
import { AccountDataViewItemsComponent } from '../../components/account-data-view-items/account-data-view-items.component';
import { AccountService } from '../../services/account.service';

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
  ],
  templateUrl: './account-main-page.component.html',
  styleUrl: './account-main-page.component.scss',
})
export class AccountMainPageComponent implements OnInit {
  accountService = inject(AccountService);
  isLoading: boolean = false;
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
}
