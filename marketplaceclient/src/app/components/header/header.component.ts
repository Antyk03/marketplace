import { Router } from '@angular/router';
import { DaoUtenteService } from './../../service/dao/dao-utente.service';
import { Component, Input } from '@angular/core';
import { ModelloService } from '../../service/modello.service';
import { C } from '../../service/c';
import { Utente } from '../../model/utente';
import { DatiUtente } from '../../model/dati_utente';
import { ERuolo } from '../../model/enums/ERuolo';
import { MessaggiService } from '../../service/messaggi.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  @Input() public titolo?: string;

  constructor(private router:Router, private modello: ModelloService, private daoUtente: DaoUtenteService, readonly messaggiService:MessaggiService) {}

  menuVisibile = false;

  toggleMenu() {
    this.menuVisibile = !this.menuVisibile;
  }


  onAccountEliminato() {
    console.log('Account eliminato, redirect al login...');
    this.router.navigate(['/login']);
  }

  get utente(): Utente | undefined {
    return this.modello.getPersistentBean<Utente>(C.UTENTE_LOGIN);
  }

  get datiUtente(): DatiUtente | undefined {
    return this.modello.getPersistentBean<DatiUtente>(C.DATI_UTENTE_LOGIN);
  }


  get isAdmin(): boolean {
    return this.datiUtente?.ruolo == ERuolo.ADMIN;
  }

  get isUser(): boolean {
    return this.datiUtente?.ruolo == ERuolo.USER;
  }

  get isVendor(): boolean {
    return this.datiUtente?.ruolo == ERuolo.VENDOR;
  }

  get nomeScheda(): string {
    if (this.isAdmin) {
      return "admin";
    } else if (this.isUser) {
      return "carrello";
    } else {
      return "catalogo";
    }
  }

}
