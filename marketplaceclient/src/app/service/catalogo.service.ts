import { Injectable } from '@angular/core';
import { DAOProdotti } from './dao/dao-prodotti.service';
import { DaoDatiUtenteService } from './dao/dao-dati_utente.service';
import { ModelloService } from './modello.service';
import { C } from './c';
import { Prodotto } from '../model/prodotto';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CatalogoService {

  private prodottiSubject = new BehaviorSubject<Prodotto[]>([]);
  prodotti$ = this.prodottiSubject.asObservable();

  constructor(
    private daoProdotti: DAOProdotti,
    private daoDatiUtente: DaoDatiUtenteService,
    private modelloService: ModelloService
  ) {}

  async aggiornaCatalogoDaServer() {
    const nuoviProdotti = await this.daoProdotti.getCatalogo();
    const filtrati = nuoviProdotti.filter(p => p.quantita > 0);

    this.prodottiSubject.next(filtrati);
    this.modelloService.putPersistentBean(C.CATALOGO, filtrati);
  }
}
