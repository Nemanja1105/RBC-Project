import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { catchError, first, map, Observable, of } from 'rxjs';
import { AccountDTO } from '../../../models/account-dto';
import { Page } from '../../../models/page';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  client = inject(HttpClient);
  messageService = inject(MessageService);

  findAll(): Observable<any> {
    return this.client.get(`${environment.baseUrl}/accounts`).pipe(
      first(),
      catchError((error) => {
        console.log(error);
        this.messageService.add({
          severity: 'error',
          summary: 'Communication error',
          detail: 'Error communicating with the server',
        });
        return of([]);
      }),
      map((result: any) => {
        if (!result) return [];
        return result;
      })
    );
  }
}
