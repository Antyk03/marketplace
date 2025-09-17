import { EStatoUtente } from './../../model/enums/EStatoUtente';
import { ERuolo } from './../../model/enums/ERuolo';
import { Component, OnInit } from '@angular/core';
import { DatiUtente } from '../../model/dati_utente';
import { DaoDatiUtenteService } from '../../service/dao/dao-dati_utente.service';
import { MessaggiService } from '../../service/messaggi.service';
import { Prodotto } from '../../model/prodotto';
import { ModelloService } from '../../service/modello.service';
import { C } from '../../service/c';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit{

  datiUtente?: DatiUtente;
  errore?: string;
  prodotti?: Prodotto[];
  prodottiUtente?: Prodotto[];

  ERuolo = ERuolo;
  EStatoUtente = EStatoUtente;

  constructor(private daoDatiUtenteService: DaoDatiUtenteService, private messaggiService: MessaggiService, private modelloService:ModelloService) {}

  async ngOnInit() {
      try {
        this.datiUtente = this.modelloService.getBean(C.DATI_UTENTE_LOGIN);
        this.prodotti = await this.daoDatiUtenteService.getCatalogo();
        if (this.datiUtente?.ruolo != ERuolo.USER) {
          this.prodottiUtente = await this.daoDatiUtenteService.getProdottiUtente();
          this.modelloService.putBean(C.PRODOTTI_UTENTE,  this.prodottiUtente);
        }
        //console.log("Dati utente caricati: ", this.datiUtente);
      } catch (err) {
        console.error("Errore caricando i dati utente", err);
        this.messaggiService.mostraMessaggioErrore("Errore: " + err);
      }
  }

  get isBloccato(): boolean {
    return this.datiUtente?.statoUtente === EStatoUtente.BLOCCATO;
  }

  // helper per ruoli
  get ruoloUtente(): ERuolo | undefined {
    return this.datiUtente?.ruolo;
  }

  async onModificaProdotto(prodotto: Prodotto): Promise<void> {

  }

  async onEliminaProdotto(prodotto: Prodotto): Promise<void> {

  }


}
