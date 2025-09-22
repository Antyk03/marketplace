import { DAOProdotti } from './../../service/dao/dao-prodotti.service';
import { Component, OnInit } from '@angular/core';
import { Utente } from '../../model/utente';
import { DaoUtenteService } from '../../service/dao/dao-utente.service';
import { MessaggiService } from '../../service/messaggi.service';
import { Prodotto } from '../../model/prodotto';
import { DatiUtente } from '../../model/dati_utente';
import { DaoDatiUtenteService } from '../../service/dao/dao-dati_utente.service';
import { EStatoUtente } from '../../model/enums/EStatoUtente';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent implements OnInit{

  datiUtenti?: DatiUtente[];
  catalogo?: Prodotto[];

  constructor(readonly daoUtenti: DaoUtenteService, readonly daoProdoti: DAOProdotti, readonly daoDatiUtente: DaoDatiUtenteService, readonly messaggiService:MessaggiService) {}

  async ngOnInit() {
    try {
      this.datiUtenti = await this.daoDatiUtente.getUtenti();
      this.catalogo = await this.daoUtenti.getProdotti();
      console.log(JSON.stringify(this.datiUtenti));
    } catch (ex) {
      console.error(ex);
      this.messaggiService.mostraMessaggioErrore("Errore durante la richiesta degli utenti: " + ex);
    }
  }

  async eliminaProdotto(idProdotto: number) {
    await this.daoProdoti.eliminaProdotto(idProdotto);
    this.catalogo = await this.daoUtenti.getProdotti();
  }

  async toggleStatoUtente(idUtente: number) {
  try {
    // Trova l'utente nell'array locale
    const index = this.datiUtenti!.findIndex(u => u.idUtente === idUtente);
    if (index === -1) return;

    // Aggiornamento lato client immediato (ottimistico)
    const utente = this.datiUtenti![index];
    utente.statoUtente = utente.statoUtente === EStatoUtente.ATTIVO ? EStatoUtente.BLOCCATO : EStatoUtente.ATTIVO;

    // Chiama il servizio lato server (non ritorna nulla)
    await this.daoUtenti.bloccaUtente(idUtente);

    this.messaggiService.mostraMessaggioInformazioni(
      `Utente ${utente.username} ${utente.statoUtente === 'ATTIVO' ? 'sbloccato' : 'bloccato'} con successo.`
    );
  } catch (ex) {
    console.error(ex);
    // Se fallisce, ripristina lo stato precedente
    const utente = this.datiUtenti!.find(u => u.idUtente === idUtente);
    if (utente) {
      utente.statoUtente = utente.statoUtente === EStatoUtente.ATTIVO ? EStatoUtente.BLOCCATO : EStatoUtente.ATTIVO;
    }
    this.messaggiService.mostraMessaggioErrore("Errore durante l'aggiornamento dello stato dell'utente.");
  }
}


}
