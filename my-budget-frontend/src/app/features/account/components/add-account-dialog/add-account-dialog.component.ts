import {
  ChangeDetectionStrategy,
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
import { NgClass, UpperCasePipe } from '@angular/common';
import { AccountService } from '../../services/account.service';
import { AccountRequestDTO } from '../../../../models/account';
import { Observable } from 'rxjs';

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
  currencies$: Observable<Currency[]>;
  currencyLoading: boolean = false;
  addLoading: boolean = false;

  constructor() {
    this.currencies$ = this.currencyService.getCurrencies();
  }

  ngOnInit(): void {
    this.currencyLoading = true;
    this.currencies$.subscribe({
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
