import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Component } from '@angular/core';
import { MessaggiService } from '../../service/messaggi.service';
import { Prodotto } from '../../model/prodotto';
import { ModelloService } from '../../service/modello.service';
import { C } from '../../service/c';
import { Utente } from '../../model/utente';
import { DAOProdotti } from '../../service/dao/dao-prodotti.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-form-modifica-prodotto',
  templateUrl: './form-modifica-prodotto.component.html',
  styleUrl: './form-modifica-prodotto.component.css'
})
export class FormModificaProdottoComponent {
  prodotto!: Prodotto;

  constructor(private messaggi:MessaggiService, private modello: ModelloService, private daoProdotti:DAOProdotti, private route: ActivatedRoute) {}

  formModifica: FormGroup = new FormGroup ({
    nome: new FormControl("", [Validators.required]),
    descrizione: new FormControl("", [Validators.required]),
    prezzo: new FormControl(1, [Validators.required, Validators.min(0.01)]),
    quantita: new FormControl(1, [Validators.required, Validators.min(1)])
  });


  async onSubmit(): Promise<void> {
    this.prodotto = this.route.snapshot.data['prod'];
    console.error(JSON.stringify(this.prodotto));
    const utentelog:Utente = this.modello.getPersistentBean<Utente>(C.UTENTE_LOGIN)!;
    const prodotto:Prodotto = new Prodotto(this.nomeFormControl.value, this.descrizioneFormControl.value, this.prezzoFormControl.value, this.quantitaFormControl.value, utentelog.id!);
    prodotto.setId = this.prodotto.id!;
    try {
      await this.daoProdotti.modificaProdotto(prodotto, this.prodotto.id!);
      this.formModifica.reset({prezzo: 1, quantita: 1});
      this.messaggi.mostraMessaggioInformazioni("Prodotto modificato.")
    } catch (ex) {
      console.error(ex);
      this.messaggi.mostraMessaggioErrore("Impossibile aggiungere il prodotto.");
    }
  }

  get nomeFormControl(): FormControl {
    return this.formModifica.get('nome') as FormControl;
  }

  get descrizioneFormControl(): FormControl {
    return this.formModifica.get('descrizione') as FormControl;
  }

  get prezzoFormControl(): FormControl {
    return this.formModifica.get('prezzo') as FormControl;
  }

  get quantitaFormControl(): FormControl {
    return this.formModifica.get('quantita') as FormControl;
  }
}
