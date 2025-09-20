import { lastValueFrom, of } from 'rxjs';
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ModelloService } from "../modello.service";
import { environment } from "../../../environments/environment";
import { Prodotto } from "../../model/prodotto";
import { C } from '../c';

@Injectable({
  providedIn:'root'
})

export class DAOProdotti {

  constructor (private httpClient:HttpClient, private modello: ModelloService) {}

  public aggiungiProdotto(prodotto: Prodotto): Promise<void> {
    const path = environment.backendUrl + 'me/aggiungi';
    console.log("PATH: " + path);
    const obs = this.httpClient.post<void>(path, prodotto);
    return lastValueFrom(obs);
  }

  public modificaProdotto(prodotto: Prodotto | null, idProdotto: number): Promise<void> {
    const path = environment.backendUrl + 'me/modifica/' + idProdotto;
    console.log("PATH: " + path);
    const obs = this.httpClient.post<void>(path, prodotto);
    return lastValueFrom(obs);
  }

  public getProdottoById(id: number): Prodotto | undefined {
    const prodotti: Prodotto[] = this.modello.getPersistentBean(C.PRODOTTI_UTENTE)!;
    for (var prodotto of prodotti) {
      if (prodotto.id! == id) {
        return prodotto;
      }
    }
    return undefined;
  }

}
