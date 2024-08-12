import { Component, inject, OnInit } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { Currency } from '../../../../models/currency';
import { CurrencyService } from '../../../../shared/services/currency.service';

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
  ],
  templateUrl: './add-account-dialog.component.html',
  styleUrl: './add-account-dialog.component.scss',
})
export class AddAccountDialogComponent implements OnInit {
  currencies: Currency[] = [];

  currencyService = inject(CurrencyService);

  ngOnInit(): void {
    this.currencyService.getCurrencies().subscribe({
      next: (data) => {
        this.currencies = data;
      },
    });
  }
}
