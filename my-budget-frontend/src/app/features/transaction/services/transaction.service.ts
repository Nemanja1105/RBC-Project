import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { first, map } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TransactionService {
  http = inject(HttpClient);

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
}
