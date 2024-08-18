import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'accounts',
    loadComponent: () =>
      import(
        './features/account/pages/account-main-page/account-main-page.component'
      ).then((r) => r.AccountMainPageComponent),
  },
  {
    path: 'transactions',
    loadComponent: () =>
      import(
        './features/transaction/pages/transaction-main-page/transaction-main-page.component'
      ).then((r) => r.TransactionMainPageComponent),
  },
  {
    path: 'settings',
    loadComponent: () =>
      import(
        './features/settings/pages/settings-main-page/settings-main-page.component'
      ).then((r) => r.SettingsMainPageComponent),
  },
  {
    path: '**',
    redirectTo: 'accounts',
    pathMatch: 'full',
  },
];
