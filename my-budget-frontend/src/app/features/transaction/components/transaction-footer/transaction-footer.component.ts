import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { AddTransactionDialogComponent } from '../add-transaction-dialog/add-transaction-dialog.component';
import { TransactionDTO } from '../../../../models/transactions';
import { DecimalPipe, UpperCasePipe } from '@angular/common';

@Component({
  selector: 'app-transaction-footer',
  standalone: true,
  imports: [
    ButtonModule,
    DividerModule,
    AddTransactionDialogComponent,
    DecimalPipe,
    UpperCasePipe,
  ],
  templateUrl: './transaction-footer.component.html',
  styleUrl: './transaction-footer.component.scss',
})
export class TransactionFooterComponent {
  @Output()
  onTransactionSubmit = new EventEmitter<TransactionDTO | null>();
  @Input()
  availableBalance: number = 0;
  @Input()
  currency: string = '';

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
