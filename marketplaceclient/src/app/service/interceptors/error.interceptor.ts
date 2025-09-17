import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor() {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError(httpError => {
        console.error("Errore durante la richiesta", httpError);
        if (httpError instanceof HttpErrorResponse && httpError.error) {
          if (httpError.error instanceof Object && httpError.error.error) {
            httpError = httpError.error.error;
          } else {
            let backEndError = JSON.parse(httpError.error);
            if (backEndError['error']) {
              httpError = backEndError['error'];
            }
          }
        }
        return throwError(() => httpError);
      })
    );
  }

}
