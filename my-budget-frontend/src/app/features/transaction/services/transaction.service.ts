import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { first, map, Observable, tap } from 'rxjs';
import { TransactionRequestDTO } from '../../../models/transactions';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root',
})
export class TransactionService {
  http = inject(HttpClient);
  messageService = inject(MessageService);

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
      .post(
        `${environment.baseUrl}/accounts/${accountId}/transactions`,
        request
      )
      .pipe(
        first(),
        tap(() => {
          this.messageService.add({
            severity: 'success',
            summary: 'Transaction successfully created.',
          });
        })
      );
  }
}
