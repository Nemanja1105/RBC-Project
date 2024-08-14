import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { MessageService } from 'primeng/api';
import { catchError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const messageService = inject(MessageService);
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error?.status != 0) {
        messageService.add({ severity: 'error', summary: error.error });
      } else {
        messageService.add({
          severity: 'error',
          summary: 'Communication error',
          detail: 'Error communicating with the server',
        });
      }
      throw error;
    })
  );
};
