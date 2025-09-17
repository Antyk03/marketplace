import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ModelloService } from "../modello.service";
import { Observable } from "rxjs";
import { Utente } from "../../model/utente";
import { C } from "../c";

@Injectable()
export class AuthTokenInterceptor implements HttpInterceptor {

  constructor(private modello: ModelloService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
      const utenteLogin = this.modello.getPersistentBean<Utente>(C.UTENTE_LOGIN);
      if (utenteLogin) {
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer ${utenteLogin.authToken}`
          }
        });
      }
      return next.handle(request);
  }

}
