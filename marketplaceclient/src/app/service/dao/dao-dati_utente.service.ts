import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { ModelloService } from '../modello.service';
import { DatiUtente } from '../../model/dati_utente';
import { environment } from '../../../environments/environment';
import { lastValueFrom } from 'rxjs';
import { Prodotto } from '../../model/prodotto';

@Injectable({
  providedIn: 'root'
})
export class DaoDatiUtenteService {

  constructor(private httpClient: HttpClient, private modello: ModelloService) {}

  public getDatiUtente(): Promise<DatiUtente> {
    const path = environment.backendUrl + 'me';
    console.log("PAth: " + path );
    const obs = this.httpClient.get<DatiUtente>(path);
    return lastValueFrom(obs);
  }


  public getProdottiUtente(): Promise<Prodotto[]> {
    const path = environment.backendUrl + 'me/prodotti';
    console.log("PATH: " + path);
    const obs = this.httpClient.get<Prodotto[]>(path);
    console.info(lastValueFrom(obs));
    return lastValueFrom(obs);
  }

  public getUtenti (): Promise<DatiUtente[]> {
    const path = environment.backendUrl + 'admin/utenti';
    console.log("PATH: " + path);
    const obs = this.httpClient.get<DatiUtente[]>(path);
    return lastValueFrom(obs);
  }

  public svuotaCarrello(): Promise<void> {
    const path = environment.backendUrl +'me/svuota';
    console.log("PATH: " + path);
    const obs = this.httpClient.post<void>(path, null);
    return lastValueFrom(obs);
  }

}
