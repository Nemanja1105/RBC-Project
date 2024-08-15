import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderNavbarComponent } from './shared/components/header/header-navbar/header-navbar.component';
import { ToastModule } from 'primeng/toast';
import { CurrencyService } from './shared/services/currency.service';
import { environment } from './environments/environments';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderNavbarComponent, ToastModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  currencyService = inject(CurrencyService);

  constructor() {
    this.initializeCurrency();
    this.currencyService.fetchCurrencies().subscribe();
    this.currencyService.fetchExchangeRate().subscribe();
  }

  initializeCurrency(): void {
    const currency = this.currencyService.getDefaultCurrency();
    if (!currency) {
      this.currencyService.setDefaultCurrency(environment.defaultCurrency);
    }
  }
}
