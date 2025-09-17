import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ModelloService } from "../modello.service";
import { finalize, Observable } from "rxjs";
import { C } from "../c";

@Injectable()
export class LoadingInterceptor implements HttpInterceptor {

  constructor(private modello: ModelloService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
      this.modello.putBean(C.CARICAMENTO, true);
      return next.handle(request).pipe(
        finalize(() => this.modello.removeBean(C.CARICAMENTO))
      );
  }

}
