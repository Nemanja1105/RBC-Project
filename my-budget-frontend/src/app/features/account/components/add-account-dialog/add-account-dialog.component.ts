import {
  Component,
  EventEmitter,
  inject,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import {
  FormBuilder,
  FormControl,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { Currency } from '../../../../models/currency';
import { CurrencyService } from '../../../../shared/services/currency.service';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { JsonPipe, NgClass, UpperCasePipe } from '@angular/common';
import { AccountService } from '../../services/account.service';
import { AccountDTO, AccountRequestDTO } from '../../../../models/account';

@Component({
  selector: 'app-add-account-dialog',
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
  templateUrl: './add-account-dialog.component.html',
  styleUrl: './add-account-dialog.component.scss',
})
export class AddAccountDialogComponent implements OnInit {
  @Input()
  visible: boolean = false;
  @Output()
  onClose = new EventEmitter<void>();
  @Output()
  afterSubmit = new EventEmitter<AccountDTO | null>();

  currencyService = inject(CurrencyService);
  accountService = inject(AccountService);
  formBuilder = inject(FormBuilder);

  formData = this.formBuilder.group({
    name: new FormControl<string>('', [
      Validators.required,
      Validators.maxLength(255),
    ]),
    currency: new FormControl<Currency | null>(null, [Validators.required]),
    balance: new FormControl<number>(0.0, [
      Validators.required,
      Validators.min(0.0),
    ]),
  });

  currencies: Currency[] = [];
  currencyLoading: boolean = false;
  addLoading: boolean = false;

  ngOnInit(): void {
    this.currencyLoading = true;
    this.currencyService.getCurrencies().subscribe({
      next: (data) => {
        this.currencies = data;
        this.currencyLoading = false;
      },
      error: () => {
        this.currencyLoading = false;
      },
    });
  }

  addAccount() {
    this.addLoading = true;
    let request: AccountRequestDTO = {
      name: this.formData.value.name!,
      currency: this.formData.value.currency?.code!,
      balance: this.formData.value.balance!,
    };
    this.accountService.insert(request).subscribe({
      next: (account) => {
        this.addLoading = false;
        this.afterSubmit.emit(account);
        this.handleOnHide();
      },
      error: () => {
        this.addLoading = false;
        this.handleOnHide();
      },
    });
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

  handleOnHide() {
    this.formData.reset();
    this.onClose.emit();
  }
}
