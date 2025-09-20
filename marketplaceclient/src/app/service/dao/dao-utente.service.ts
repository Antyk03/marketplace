import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ModelloService } from "../modello.service";
import { Utente } from "../../model/utente";
import { environment } from "../../../environments/environment";
import { lastValueFrom, map, tap } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DaoUtenteService {

  constructor(private httpClient: HttpClient, private modello: ModelloService) {}

  login(email: string, password: string): Promise<Utente> {
    if (environment.backendStrategy === 'MOCK') {
      return this.gestisciLoginMock(email, password);
    }
    let apiUrl = environment.backendUrl + 'utenti/login';
    //console.log("URL API: ", apiUrl);
    return lastValueFrom (
      this.httpClient.post(apiUrl, { email, password }, { responseType: 'text' }) // nessun <Utente>
      .pipe(
        map((tokenString: string) => {
          return new Utente(email, tokenString); // ora TS sa che tokenString è stringa
        }),
        //tap(utente => console.log('Ricevuto utente con token', utente))
      )
  );
  }

  private gestisciLoginMock(email: string, password: string): Promise<Utente> {
    let apiUrl = environment.backendUrl + '/utenti';
    return lastValueFrom(this.httpClient.get<Utente[]>(apiUrl).pipe(
      map(utenti => this.cercaUtente(utenti, email, password))
    ));
  }

  private cercaUtente(utenti: Utente[], email: string, password: string): Utente {
    let utente = utenti.find(u => u.email === email && (u as any).password === password);
    if (!utente) {
      throw new Error('Credenziali scorrette');
    }
    let utenteLogin = new Utente(utente.email, 'tokenMarketplace-' + email);
    utenteLogin.id = utente.id;
    return utenteLogin;
  }

}
