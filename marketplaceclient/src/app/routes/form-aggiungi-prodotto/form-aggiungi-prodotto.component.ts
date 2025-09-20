import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Component } from '@angular/core';
import { MessaggiService } from '../../service/messaggi.service';
import { Prodotto } from '../../model/prodotto';
import { ModelloService } from '../../service/modello.service';
import { C } from '../../service/c';
import { Utente } from '../../model/utente';
import { DAOProdotti } from '../../service/dao/dao-prodotti.service';

@Component({
  selector: 'app-form-aggiungi-prodotto',
  templateUrl: './form-aggiungi-prodotto.component.html',
  styleUrl: './form-aggiungi-prodotto.component.css'
})
export class FormAggiungiProdottoComponent {

  constructor(private messaggi:MessaggiService, private modello: ModelloService, private daoProdotti:DAOProdotti) {}

  formAggiungi: FormGroup = new FormGroup ({
    nome: new FormControl("", [Validators.required]),
    descrizione: new FormControl("", [Validators.required]),
    prezzo: new FormControl(1, [Validators.required, Validators.min(0.01)]),
    quantita: new FormControl(1, [Validators.required, Validators.min(1)])
  });


  async onSubmit(): Promise<void> {
    const utentelog:Utente = this.modello.getPersistentBean<Utente>(C.UTENTE_LOGIN)!;
    const prodotto:Prodotto = new Prodotto(this.nomeFormControl.value, this.descrizioneFormControl.value, this.prezzoFormControl.value, this.quantitaFormControl.value, utentelog.id!);
    try {
      await this.daoProdotti.aggiungiProdotto(prodotto);
      this.formAggiungi.reset({prezzo: 1, quantita: 1});
      this.messaggi.mostraMessaggioInformazioni("Nuovo prodotto inserito.")
    } catch (ex) {
      console.error(ex);
      this.messaggi.mostraMessaggioErrore("Impossibile aggiungere il prodotto.");
    }
  }

  get nomeFormControl(): FormControl {
    return this.formAggiungi.get('nome') as FormControl;
  }

  get descrizioneFormControl(): FormControl {
    return this.formAggiungi.get('descrizione') as FormControl;
  }

  get prezzoFormControl(): FormControl {
    return this.formAggiungi.get('prezzo') as FormControl;
  }

  get quantitaFormControl(): FormControl {
    return this.formAggiungi.get('quantita') as FormControl;
  }
}
