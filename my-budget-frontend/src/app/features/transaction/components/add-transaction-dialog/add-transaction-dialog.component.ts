import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import {
  FormBuilder,
  FormControl,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { NgClass, UpperCasePipe } from '@angular/common';
import {
  TransactionDTO,
  TransactionRequestDTO,
} from '../../../../models/transactions';
import { AccountDTO } from '../../../../models/account';
import { positiveNumberValidator } from '../../../../shared/directives/positive-number.validator';
import { sufficientFundsValidator } from '../../../../shared/directives/sufficient-funds.validator';
import { TransactionService } from '../../services/transaction.service';

@Component({
  selector: 'app-add-transaction-dialog',
  standalone: true,
  imports: [
    DialogModule,
    ButtonModule,
    FormsModule,
    InputGroupAddonModule,
    InputGroupModule,
    InputTextModule,
    ReactiveFormsModule,
    DropdownModule,
    NgClass,
    UpperCasePipe,
  ],
  templateUrl: './add-transaction-dialog.component.html',
  styleUrl: './add-transaction-dialog.component.scss',
})
export class AddTransactionDialogComponent {
  @Input()
  visible: boolean = false;
  @Input()
  accounts: AccountDTO[] = [];
  @Output()
  onClose = new EventEmitter<void>();
  @Output()
  afterSubmit = new EventEmitter<TransactionDTO | null>();

  transactionTypes = [
    { label: 'Income', value: 1 },
    { label: 'Expense', value: 0 },
  ];
  addLoading: boolean = false;

  formBuilder = inject(FormBuilder);
  transactionService = inject(TransactionService);

  formData = this.formBuilder.group({
    description: new FormControl<string>('', [
      Validators.required,
      Validators.maxLength(255),
    ]),
    type: new FormControl<number | null>(null, [Validators.required]),
    amount: new FormControl<number | null>(1, [
      Validators.required,
      positiveNumberValidator(),
      sufficientFundsValidator(),
    ]),
    account: new FormControl<AccountDTO | null>(null, [Validators.required]),
  });

  handleOnHide() {
    this.formData.reset();
    this.onClose.emit();
  }

  isFieldInvalid(field: string) {
    return (
      (this.formData.get(field)?.dirty || this.formData.get(field)?.touched) &&
      this.formData.get(field)?.invalid
    );
  }

  doesFieldHaveError(field: string, error: string) {
    return (
      (this.formData.get(field)?.dirty || this.formData.get(field)?.touched) &&
      this.formData.get(field)?.hasError(error)
    );
  }

  addTransaction() {
    this.addLoading = true;
    let request: TransactionRequestDTO = {
      description: this.formData.value.description!,
      type: this.formData.value.type!,
      amount: this.formData.value.amount!,
    };
    if (!this.formData.value.account?.id) return;
    this.transactionService
      .insert(this.formData.value.account?.id, request)
      .subscribe({
        next: (transaction) => {
          this.addLoading = false;
          this.afterSubmit.emit(transaction);
          this.handleOnHide();
        },
        error: () => {
          this.addLoading = false;
          this.handleOnHide();
        },
      });
  }
}
