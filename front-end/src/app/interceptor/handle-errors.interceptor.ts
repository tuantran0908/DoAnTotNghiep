import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpResponse,
  HttpErrorResponse,
} from '@angular/common/http';
import { catchError, finalize, map, Observable, retry, throwError } from 'rxjs';
import { HandleErrorService } from '../services/handle-error.service';
import { AuthService } from '../services/auth/auth.service';

@Injectable()
export class HandleErrorsInterceptor implements HttpInterceptor {
  constructor(
    private error: HandleErrorService,
    private authService: AuthService
  ) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    // returning an observable to complete the request cycle
    let authReq = request;
    let user = this.authService.getAuthorization();

    if (user)
      authReq = request.clone({
        setHeaders: { Authorization: 'Bearer ' + user.token },
      });

    return new Observable((observer) => {
      next.handle(authReq).subscribe({
        next: (res) => {
          if (res instanceof HttpResponse) {
            observer.next(res);
          }
        },
        error: (err: HttpErrorResponse) => {
          this.error.handleError(err);
        },
      });
    });
  }
}
