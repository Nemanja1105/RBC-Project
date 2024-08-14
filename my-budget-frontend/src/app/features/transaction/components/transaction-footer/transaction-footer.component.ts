import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { AddTransactionDialogComponent } from '../add-transaction-dialog/add-transaction-dialog.component';
import { AccountDTO } from '../../../../models/account';
import { TransactionDTO } from '../../../../models/transactions';

@Component({
  selector: 'app-transaction-footer',
  standalone: true,
  imports: [ButtonModule, DividerModule, AddTransactionDialogComponent],
  templateUrl: './transaction-footer.component.html',
  styleUrl: './transaction-footer.component.scss',
})
export class TransactionFooterComponent {
  @Input()
  accounts: AccountDTO[] = [];
  @Output()
  onTransactionSubmit = new EventEmitter<TransactionDTO | null>();

  visibilityOfAddDialog: boolean = false;

  openDialog() {
    this.visibilityOfAddDialog = true;
  }

  onDialogClose() {
    this.visibilityOfAddDialog = false;
  }

  onSubmit(transaction: TransactionDTO | null) {
    this.onTransactionSubmit.emit(transaction);
  }
}
