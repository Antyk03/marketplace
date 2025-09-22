import { DaoDatiUtenteService } from './../../service/dao/dao-dati_utente.service';
import { DAOOrdini } from './../../service/dao/dao-ordini.service';
import { Component, OnInit } from '@angular/core';
import { Prodotto } from '../../model/prodotto';
import { ModelloService } from '../../service/modello.service';
import { MessaggiService } from '../../service/messaggi.service';
import { C } from '../../service/c';
import { Ordine } from '../../model/ordine';
import { HomeComponent } from '../home/home.component';
import { CatalogoService } from '../../service/catalogo.service';

@Component({
  selector: 'app-carrello',
  templateUrl: './carrello.component.html',
  styleUrl: './carrello.component.css'
})
export class CarrelloComponent implements OnInit{
  carrello?: Ordine;
  storico: Ordine[] = [];

  constructor(private daoDatiUtente: DaoDatiUtenteService, private catalogoService:CatalogoService, private daoOrdini:DAOOrdini, private modelloService: ModelloService, private messaggiService: MessaggiService) {}

  async ngOnInit() {
      await this.caricaDati();
  }

  private async caricaDati() {
    try {
        const carrelloTmp = await this.daoOrdini.getCarrello();
        if (carrelloTmp && carrelloTmp.prodottiOrdineDTO.length > 0) {
          this.carrello = carrelloTmp;
        } else {
          this.carrello = undefined;
        }
        const storicoTmp = await this.daoOrdini.getStorico();
        this.storico = storicoTmp || [];
        console.log("CARRELLO: " + JSON.stringify(this.carrello));
      } catch (ex) {
        console.error(ex);
        this.messaggiService.mostraMessaggioErrore("Errore durante il caricamento del carrello: " + ex);
      }
  }

  async onClickAcquista() {
    try {
      if (!this.carrello || this.carrello.prodottiOrdineDTO.length === 0) {
        this.messaggiService.mostraMessaggioInformazioni("Il carrello è vuoto");
        return;
      }
      await this.daoOrdini.effettuaOrdine();
      this.messaggiService.mostraMessaggioInformazioni("Acquisto completato con successo");
      console.log("Sto acquistando.");
      await this.caricaDati();
      await this.catalogoService.aggiornaCatalogoDaServer();
    } catch(ex) {
      console.error(ex);
      this.messaggiService.mostraMessaggioErrore("Errore durante l'acquisto. " + ex);
    }
  }

  async onClickRimuovi(idProdotto: number) {
    try {
      await this.daoOrdini.rimuoviProdotto(idProdotto);
      this.messaggiService.mostraMessaggioInformazioni("Rimozione avvenuta con successo");
      console.log("Sto rimuovendo.");
      await this.caricaDati();
      await this.catalogoService.aggiornaCatalogoDaServer();
    } catch(ex) {
      console.error(ex);
      this.messaggiService.mostraMessaggioErrore("Errore durante la rimozione. " + ex);
    }
  }

  async onClickSvuota() {
    try {
      await this.daoDatiUtente.svuotaCarrello();
      this.messaggiService.mostraMessaggioInformazioni("Carrello svuotato con successo");
      console.log("Sto rimuovendo.");
      await this.caricaDati();
      await this.catalogoService.aggiornaCatalogoDaServer();
    } catch(ex) {
      console.error(ex);
      this.messaggiService.mostraMessaggioErrore("Errore durante l'operazione. " + ex);
    }
  }

}
