import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { DaoDatiUtenteService } from './../../service/dao/dao-dati_utente.service';
import { DatiUtente } from '../../model/dati_utente';
import { DaoUtenteService } from '../../service/dao/dao-utente.service';

@Component({
  selector: 'app-modal-conferma',
  templateUrl: './modal-conferma.component.html',
  styleUrls: ['./modal-conferma.component.css']
})
export class ModalConfermaComponent implements OnInit {

  datiUtente?: DatiUtente;
  showModal = false;
  usernameConferma = '';
  erroreModal = '';

  @Output() accountEliminato = new EventEmitter<void>();

  constructor(private daoUtente: DaoUtenteService, private daoDatiUtenti: DaoDatiUtenteService) {}

  async ngOnInit(): Promise<void> {
      try {
        this.datiUtente = await this.daoDatiUtenti.getDatiUtente();
      } catch(ex) {
        console.error(ex);
      }
  }

  apriModalEliminaAccount() {
    this.usernameConferma = '';
    this.erroreModal = '';
    this.showModal = true;
  }

  chiudiModal() {
    this.showModal = false;
    this.erroreModal = '';
  }

  async confermaEliminaAccount() {
    this.datiUtente = await this.daoDatiUtenti.getDatiUtente();
    if (this.usernameConferma !== this.datiUtente?.username) {
      this.erroreModal = 'Username non corretto.';
      return;
    }

    try {
      await this.daoUtente.eliminaAccount(); // chiama il servizio per eliminare account
      this.showModal = false;
      this.accountEliminato.emit(); // notifica il parent
    } catch(ex) {
      console.error(ex);
      this.erroreModal = 'Errore durante l\'eliminazione: ' + ex;
    }
  }

}
