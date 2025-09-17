import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { ModelloService } from "../service/modello.service";
import { Observable } from "rxjs";
import { Utente } from "../model/utente";
import { C } from "../service/c";

@Injectable({
  providedIn: 'root'
})

export class AuthGuard implements CanActivate {

  constructor(private router: Router, private modello: ModelloService) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      let utenteLogin = this.modello.getPersistentBean<Utente>(C.UTENTE_LOGIN);
      if (utenteLogin && utenteLogin.authToken) {
        return true;
      }
      console.log('Accesso non consentito alla pagina ', route.url[0].path);
      this.router.navigate(['/login']);
      return false;
  }

}
