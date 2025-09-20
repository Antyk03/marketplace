import { lastValueFrom } from 'rxjs';
import { ModelloService } from './../modello.service';
import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { Ordine } from '../../model/ordine';
import { environment } from '../../../environments/environment';
import { Prodotto } from '../../model/prodotto';
import { ProdottoOrdine } from '../../model/prodotto_ordine';

@Injectable({
  providedIn: 'root'
})

export class DAOOrdini {

  constructor(private httpClient: HttpClient, private modelloService: ModelloService) {}

  public getCarrello(): Promise<Ordine> {
    const path = environment.backendUrl + 'ordine/visualizza';
    console.log("PATH: " + path);
    const obs = this.httpClient.get<Ordine>(path);
    return lastValueFrom(obs);
  }

  public aggiungiAlCarrello(prodotto: ProdottoOrdine, idProdotto:number): Promise<void> {
    const path = environment.backendUrl + 'ordine/aggiungi/'+idProdotto;
    console.log("PATH:" + path);
    const obs = this.httpClient.post<void>(path, prodotto);
    return lastValueFrom(obs);
  }

  public effettuaOrdine(): Promise<void> {
    const path = environment.backendUrl + 'ordine/acquista';
    console.log("PATH: " + path);
    const obs = this.httpClient.post<void>(path, null);
    return lastValueFrom(obs);
  }

  public getStorico(): Promise<Ordine[]> {
    const path = environment.backendUrl + 'ordine/storico';
    console.log("PATH: " + path);
    const obs = this.httpClient.get<Ordine[]>(path);
    return lastValueFrom(obs);
  }


}
