import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { first, map, Observable, tap } from 'rxjs';
import {
  TransactionDTO,
  TransactionRequestDTO,
} from '../../../models/transactions';
import { MessageService } from 'primeng/api';
import { AccountService } from '../../account/services/account.service';

@Injectable({
  providedIn: 'root',
})
export class TransactionService {
  http = inject(HttpClient);
  messageService = inject(MessageService);
  accountService = inject(AccountService);

  findAll() {
    return this.http.get(`${environment.baseUrl}/transactions`).pipe(
      first(),
      map((result: any) => {
        if (!result) return [];
        return result;
      })
    );
  }

  findAllByAccountId(id: number) {
    return this.http
      .get(`${environment.baseUrl}/accounts/${id}/transactions`)
      .pipe(
        first(),
        map((result: any) => {
          if (!result) return [];
          return result;
        })
      );
  }
  insert(accountId: number, request: TransactionRequestDTO): Observable<any> {
    return this.http
      .post<TransactionDTO>(
        `${environment.baseUrl}/accounts/${accountId}/transactions`,
        request
      )
      .pipe(
        first(),
        tap((data) => {
          this.accountService.update(data.account);
          this.messageService.add({
            severity: 'success',
            summary: 'Transaction successfully created.',
          });
        })
      );
  }
}
