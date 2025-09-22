import { DAOProdotti } from './../../service/dao/dao-prodotti.service';
import { Component } from '@angular/core';
import { DaoDatiUtenteService } from '../../service/dao/dao-dati_utente.service';
import { ModelloService } from '../../service/modello.service';
import { Prodotto } from '../../model/prodotto';
import { C } from '../../service/c';

@Component({
  selector: 'app-catalogo',
  templateUrl: './catalogo.component.html',
  styleUrl: './catalogo.component.css'
})
export class CatalogoComponent {

  prodotti: Prodotto[] = [];

  constructor(readonly daoProdotti: DAOProdotti, readonly modello:ModelloService) {}

  async ngOnInit(): Promise<void> {
    this.prodotti = await this.daoProdotti.getCatalogo();
    this.modello.putPersistentBean(C.CATALOGO, this.prodotti);
  }

}
