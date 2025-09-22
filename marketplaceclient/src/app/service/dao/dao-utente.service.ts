import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ModelloService } from "../modello.service";
import { Utente } from "../../model/utente";
import { environment } from "../../../environments/environment";
import { last, lastValueFrom, map, tap } from "rxjs";
import { RegistrazioneUtente } from "../../model/registrazione_utente";
import { Prodotto } from "../../model/prodotto";

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


  public bloccaUtente(idUtente: number): Promise<void> {
    const path = environment.backendUrl + 'admin/'+idUtente+"/blocca";
    console.log("PATH: " + path);
    const obs = this.httpClient.post<void>(path, idUtente);
    return lastValueFrom(obs);
  }

  public registraUtente(registrazioneUtente:RegistrazioneUtente): Promise<void> {
    const path = environment.backendUrl + 'utenti/registra';
    console.log("PATH: " + path);
    const obs = this.httpClient.post<void>(path, registrazioneUtente);
    return lastValueFrom(obs);
  }

  public eliminaAccount(): Promise<void> {
    const path = environment.backendUrl + 'utenti/elimina';
    console.log("PAHT: " + path);
    const obs = this.httpClient.post<void>(path, null);
    return lastValueFrom(obs);
  }

  public getProdotti(): Promise<Prodotto[]> {
      const path = environment.backendUrl + 'admin/prodotti';
      console.log("PAth: " + path );
      const obs = this.httpClient.get<Prodotto[]>(path);
      return lastValueFrom(obs);
    }

}
