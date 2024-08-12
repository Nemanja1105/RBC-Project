import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environments';
import { catchError, first, map, of } from 'rxjs';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root',
})
export class CurrencyService {
  http = inject(HttpClient);
  messageService = inject(MessageService);

  getCurrencies() {
    return this.http.get(`${environment.currencyApiUrl}/currencies.json`).pipe(
      first(),
      map((el: any) => {
        return Object.keys(el)
          .map((k) => {
            return { code: k, name: el[k] };
          })
          .filter((t) => t.name != '');
      }),
      catchError((error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Communication error',
          detail: 'Error communicating with the server',
        });
        return of([]);
      })
    );
  }
}
