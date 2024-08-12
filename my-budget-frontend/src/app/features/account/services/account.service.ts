import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { catchError, first, map, Observable, of, tap } from 'rxjs';
import { AccountDTO, AccountRequestDTO } from '../../../models/account-dto';
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

  insert(request: AccountRequestDTO): Observable<any> {
    return this.client.post(`${environment.baseUrl}/accounts`, request).pipe(
      first(),
      tap(() => {
        this.messageService.add({
          severity: 'success',
          summary: 'Account successfully added',
        });
      }),
      catchError((error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Communication error',
          detail: 'Error communicating with the server',
        });
        throw error;
      })
    );
  }
}
