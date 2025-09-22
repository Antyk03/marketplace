import { CatalogoService } from './../../service/catalogo.service';
import { DAOOrdini } from './../../service/dao/dao-ordini.service';
import { EStatoUtente } from './../../model/enums/EStatoUtente';
import { ERuolo } from './../../model/enums/ERuolo';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { DatiUtente } from '../../model/dati_utente';
import { DaoDatiUtenteService } from '../../service/dao/dao-dati_utente.service';
import { MessaggiService } from '../../service/messaggi.service';
import { Prodotto } from '../../model/prodotto';
import { ModelloService } from '../../service/modello.service';
import { C } from '../../service/c';
import { DAOProdotti } from '../../service/dao/dao-prodotti.service';
import { ProdottoOrdine } from '../../model/prodotto_ordine';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit, OnDestroy {

  datiUtente?: DatiUtente;
  prodotti: Prodotto[] = [];
  prodottiUtente?: Prodotto[];
  quantitaSelezionata: { [id: number]: number } = {};
  prodottoCarrello?: ProdottoOrdine;
  private sub?: Subscription;

  ERuolo = ERuolo;
  EStatoUtente = EStatoUtente;

  constructor(
    private daoOrdini: DAOOrdini,
    private daoProdotti: DAOProdotti,
    private daoDatiUtenteService: DaoDatiUtenteService,
    private messaggiService: MessaggiService,
    private modelloService: ModelloService,
    private catalogoService: CatalogoService
  ) {}

  async ngOnInit() {
    try {
      // Sottoscrizione al catalogo
      this.sub = this.catalogoService.prodotti$.subscribe(p => {
        this.prodotti = p;
      });

      this.datiUtente = this.modelloService.getPersistentBean(C.DATI_UTENTE_LOGIN);

      if (this.datiUtente?.ruolo !== ERuolo.USER) {
        this.prodottiUtente = await this.daoDatiUtenteService.getProdottiUtente();
        this.modelloService.putBean(C.PRODOTTI_UTENTE, this.prodottiUtente);
      }

      // Primo caricamento dal server (solo qui!)
      await this.catalogoService.aggiornaCatalogoDaServer();
    } catch (err) {
      console.error("Errore caricando i dati utente", err);
      this.messaggiService.mostraMessaggioErrore("Errore: " + err);
    }
  }

  ngOnDestroy() {
    this.sub?.unsubscribe();
  }

  get isBloccato(): boolean {
    return this.datiUtente?.statoUtente === EStatoUtente.BLOCCATO;
  }

  get ruoloUtente(): ERuolo | undefined {
    return this.datiUtente?.ruolo;
  }

  async onEliminaProdotto(prodotto: Prodotto): Promise<void> {
    try {
      await this.daoProdotti.modificaProdotto(null, prodotto.id!);

      // Aggiorna SOLO i prodotti dell'utente
      this.prodottiUtente = await this.daoDatiUtenteService.getProdottiUtente();
      this.modelloService.putBean(C.PRODOTTI_UTENTE, this.prodottiUtente);
      await this.catalogoService.aggiornaCatalogoDaServer();
      this.messaggiService.mostraMessaggioInformazioni("Prodotto eliminato con successo.");
    } catch (ex) {
      console.error("Errore: " + ex);
      this.messaggiService.mostraMessaggioErrore("Impossibile eliminare il prodotto: " + ex);
    }
  }

  async onAggiungiAlCarrello(prodotto: Prodotto): Promise<void> {
    try {
      const qta = this.quantitaSelezionata[prodotto.id!] || 1;

      // Aggiorna localmente la disponibilità (feedback immediato lato client)
      const index = this.prodotti.findIndex(p => p.id === prodotto.id);
      if (index !== -1) {
        this.prodotti[index].quantita = Math.max(0, this.prodotti[index].quantita - qta);
      }

      // Aggiungi al carrello sul server
      this.prodottoCarrello = new ProdottoOrdine(prodotto.id!, qta, prodotto.prezzo, null);
      await this.daoOrdini.aggiungiAlCarrello(this.prodottoCarrello, prodotto.id!);

      // (Opzionale) puoi decidere di NON richiamare subito il server
      // await this.catalogoService.aggiornaCatalogoDaServer();

      this.quantitaSelezionata[prodotto.id!] = 1;
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
