import { Component } from '@angular/core';
import { ModelloService } from '../../service/modello.service';
import { Utente } from '../../model/utente';
import { C } from '../../service/c';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css'
})
export class FooterComponent {

  constructor(private modello: ModelloService) {}

  get utente(): Utente | undefined {
    return this.modello.getPersistentBean<Utente>(C.UTENTE_LOGIN);
  }

}
