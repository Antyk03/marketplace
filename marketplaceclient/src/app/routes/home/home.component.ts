import { DAOOrdini } from './../../service/dao/dao-ordini.service';
import { EStatoUtente } from './../../model/enums/EStatoUtente';
import { ERuolo } from './../../model/enums/ERuolo';
import { Component, OnInit } from '@angular/core';
import { DatiUtente } from '../../model/dati_utente';
import { DaoDatiUtenteService } from '../../service/dao/dao-dati_utente.service';
import { MessaggiService } from '../../service/messaggi.service';
import { Prodotto } from '../../model/prodotto';
import { ModelloService } from '../../service/modello.service';
import { C } from '../../service/c';
import { DAOProdotti } from '../../service/dao/dao-prodotti.service';
import { ProdottoOrdine } from '../../model/prodotto_ordine';

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
  quantitaSelezionata: { [id: number]: number } = {};
  prodottoCarrello?: ProdottoOrdine;

  ERuolo = ERuolo;
  EStatoUtente = EStatoUtente;

  constructor(private daoOrdini:DAOOrdini, private daoProdotti:DAOProdotti, private daoDatiUtenteService: DaoDatiUtenteService, private messaggiService: MessaggiService, private modelloService:ModelloService) {}

  async ngOnInit() {
      try {
        this.datiUtente = this.modelloService.getPersistentBean(C.DATI_UTENTE_LOGIN);
        this.prodotti = await this.daoDatiUtenteService.getCatalogo();
        //this.prodotti = this.modelloService.getPersistentBean(C.CATALOGO) || [];
        if (this.prodotti.length == 0) {
          this.prodotti = await this.daoDatiUtenteService.getCatalogo();
        }
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

  async onEliminaProdotto(prodotto: Prodotto): Promise<void> {
    try {
      await this.daoProdotti.modificaProdotto(null, prodotto.id!);
      this.prodotti = await this.daoDatiUtenteService.getCatalogo();
      this.prodottiUtente = await this.daoDatiUtenteService.getProdottiUtente();
      this.modelloService.putBean(C.PRODOTTI_UTENTE,  this.prodottiUtente);
      this.modelloService.putPersistentBean(C.CATALOGO,  this.prodotti);
      this.messaggiService.mostraMessaggioInformazioni("Prodotto eliminato con successo.");
    } catch (ex) {
      console.error("Errore: " + ex);
      this.messaggiService.mostraMessaggioErrore("Impossibile eiliminare il prodotto: " + ex);
    }
  }

  async onAggiungiAlCarrello(prodotto: Prodotto): Promise<void> {
    try {
      let qta = this.quantitaSelezionata[prodotto.id!] || 1;
      qta = Number(qta);
      console.log("quantità selezionata: " + qta);
      prodotto.quantita -= qta;
      if (prodotto.quantita < 0) {
        prodotto.quantita = 0;
      }
      this.prodottoCarrello = new ProdottoOrdine(prodotto.id!, qta, prodotto.prezzo, null);
      console.log("Quantità prodottoCarrello: " + this.prodottoCarrello.quantita);
      this.quantitaSelezionata[prodotto.id!] = 1;
      console.log("Prodotto aggiunto al carrello: "+ JSON.stringify(this.prodottoCarrello));
      this.daoOrdini.aggiungiAlCarrello(this.prodottoCarrello, prodotto.id!);
      this.messaggiService.mostraMessaggioInformazioni("Prodotto aggiunto al carrello con successo.");
    } catch (ex) {
      console.error("Errore: " + ex);
      this.messaggiService.mostraMessaggioErrore("Impossibile aggiungere il prodotto al carrello: " + ex);
    }
  }

  quantitaMassima(qta: number): number {
    return Math.min(qta, 20);
  }


}
