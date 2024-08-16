import {
  ChangeDetectionStrategy,
  Component,
  inject,
  OnInit,
} from '@angular/core';
import { TitleWrapperComponent } from '../../../../shared/components/title-wrapper/title-wrapper.component';
import { DropdownItem, DropdownModule } from 'primeng/dropdown';
import { ButtonModule } from 'primeng/button';
import { CurrencyService } from '../../../../shared/services/currency.service';
import { debounceTime, distinctUntilChanged, Observable } from 'rxjs';
import { Currency, ExchangeRates } from '../../../../models/currency';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { DatePipe } from '@angular/common';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';
import { AccountService } from '../../../account/services/account.service';
import { SettingsFooterComponent } from '../../components/settings-footer/settings-footer.component';

@Component({
  selector: 'app-settings-main-page',
  standalone: true,
  imports: [
    TitleWrapperComponent,
    DropdownModule,
    ButtonModule,
    ReactiveFormsModule,
    DatePipe,
    ConfirmDialogModule,
    SettingsFooterComponent,
  ],
  templateUrl: './settings-main-page.component.html',
  styleUrl: './settings-main-page.component.scss',
})
export class SettingsMainPageComponent implements OnInit {
  currencyService = inject(CurrencyService);
  accountService = inject(AccountService);
  confirmationService = inject(ConfirmationService);

  currencies$: Observable<Currency[]>;
  exchangeRates$: Observable<ExchangeRates | null>;
  formChanged$: Observable<any>;
  currencies: Currency[] = [];
  exchangeRates: ExchangeRates | null = null;

  currencyForm = new FormControl<Currency | null>(null);

  constructor() {
    this.currencies$ = this.currencyService.getCurrencies();
    this.exchangeRates$ = this.currencyService.getExchangeRatesObservable();
    this.formChanged$ = this.currencyForm.valueChanges.pipe(
      debounceTime(400),
      distinctUntilChanged(),
      takeUntilDestroyed()
    );
  }

  ngOnInit(): void {
    this.currencies$.subscribe({
      next: (data) => {
        this.currencies = data;
        const default_currency_code = this.currencyService.getDefaultCurrency();
        const defaultCurrency = data.find(
          (currency) => currency.code === default_currency_code
        );
        this.currencyForm.patchValue(defaultCurrency!);
      },
    });
    this.formChanged$.subscribe(() => {
      this.currencyService.changeCurrency(this.currencyForm.value!);
    });
    this.exchangeRates$.subscribe((data) => {
      this.exchangeRates = data;
    });
  }

  deleteAllData() {
    this.confirmationService.confirm({
      message: 'Do you want to delete all data in system?',
      header: 'Delete Confirmation',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: ' p-button-text',
      rejectButtonStyleClass: 'p-button-danger p-button-text',
      acceptIcon: 'none',
      rejectIcon: 'none',
      accept: () => {
        this.accountService.deleteAllData().subscribe();
      },
      reject: () => {},
    });
  }
}
