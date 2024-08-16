import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal, WritableSignal } from '@angular/core';
import { environment } from '../../../environments/environments';
import {
  BehaviorSubject,
  catchError,
  first,
  map,
  Observable,
  of,
  tap,
} from 'rxjs';
import { AccountDTO, AccountRequestDTO } from '../../../models/account';
import { Page } from '../../../models/page';
import { MessageService } from 'primeng/api';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  client = inject(HttpClient);
  messageService = inject(MessageService);

  private accountsSubject = new BehaviorSubject<AccountDTO[]>([]);
  private loading: WritableSignal<number> = signal<number>(0);

  getAccountsObservable() {
    return this.accountsSubject.asObservable().pipe(takeUntilDestroyed());
  }

  fetchAll(): Observable<any> {
    this.enableLoader();
    return this.client.get(`${environment.baseUrl}/accounts`).pipe(
      first(),
      map((result: any) => {
        if (!result) return [];
        return result;
      }),
      tap((accounts) => {
        this.accountsSubject.next(accounts);
        this.disableLoader();
      }),
      catchError((error) => {
        this.disableLoader();
        throw error;
      })
    );
  }

  insert(request: AccountRequestDTO): Observable<AccountDTO> {
    return this.client
      .post<AccountDTO>(`${environment.baseUrl}/accounts`, request)
      .pipe(
        first(),
        tap((data: AccountDTO) => {
          this.accountsSubject.next([...this.accountsSubject.value, data]);
          this.messageService.add({
            severity: 'success',
            summary: 'Account successfully added',
          });
        })
      );
  }

  update(data: AccountDTO) {
    const tmp = this.accountsSubject.value.map((el) => {
      return el.id != data.id ? el : data;
    });
    this.accountsSubject.next(tmp);
  }

  deleteAllData() {
    return this.client.delete(`${environment.baseUrl}/accounts`).pipe(
      first(),
      tap(() => {
        this.accountsSubject.next([]);
        this.messageService.add({
          severity: 'success',
          summary: 'Data successfully reset',
        });
      })
    );
  }

  public enableLoader(): void {
    this.loading.set(this.loading() + 1);
  }

  public disableLoader(): void {
    if (this.loading() === 0) return;
    this.loading.set(this.loading() - 1);
  }

  public get isLoading(): boolean {
    return this.loading() > 0;
  }
}
