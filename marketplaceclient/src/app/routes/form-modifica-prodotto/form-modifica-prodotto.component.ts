import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { MessaggiService } from '../../service/messaggi.service';
import { Prodotto } from '../../model/prodotto';
import { ModelloService } from '../../service/modello.service';
import { C } from '../../service/c';
import { Utente } from '../../model/utente';
import { DAOProdotti } from '../../service/dao/dao-prodotti.service';
import { ActivatedRoute } from '@angular/router';
import { HomeComponent } from '../home/home.component';
import { CatalogoService } from '../../service/catalogo.service';

@Component({
  selector: 'app-form-modifica-prodotto',
  templateUrl: './form-modifica-prodotto.component.html',
  styleUrls: ['./form-modifica-prodotto.component.css']
})
export class FormModificaProdottoComponent implements OnInit {
  prodotto!: Prodotto;

  formModifica: FormGroup = new FormGroup({
    nome: new FormControl("", [Validators.required]),
    descrizione: new FormControl("", [Validators.required]),
    prezzo: new FormControl(1, [Validators.required, Validators.min(0.01)]),
    quantita: new FormControl(1, [Validators.required, Validators.min(1)])
  });

  constructor(
    private messaggi: MessaggiService,
    private modello: ModelloService,
    private daoProdotti: DAOProdotti,
    private route: ActivatedRoute,
    private catalogoService: CatalogoService
  ) {}

  ngOnInit(): void {
    // Recupero del prodotto dai dati risolti dalla route (resolver)
    this.prodotto = this.route.snapshot.data['prod'];

    // Popolo il form con i valori esistenti
    if (this.prodotto) {
      this.formModifica.setValue({
        nome: this.prodotto.nome,
        descrizione: this.prodotto.descrizione,
        prezzo: this.prodotto.prezzo,
        quantita: this.prodotto.quantita
      });
    }
  }

  async onSubmit(): Promise<void> {
    const utenteLog: Utente = this.modello.getPersistentBean<Utente>(C.UTENTE_LOGIN)!;

    const prodottoModificato: Prodotto = new Prodotto(
      this.nomeFormControl.value,
      this.descrizioneFormControl.value,
      this.prezzoFormControl.value,
      this.quantitaFormControl.value,
      utenteLog.id!
    );
    prodottoModificato.setId = this.prodotto.id!;

    try {
      await this.daoProdotti.modificaProdotto(prodottoModificato, this.prodotto.id!);
      this.messaggi.mostraMessaggioInformazioni("Prodotto modificato con successo.");
      await this.catalogoService.aggiornaCatalogoDaServer();
    } catch (ex) {
      console.error(ex);
      this.messaggi.mostraMessaggioErrore("Impossibile modificare il prodotto.");
    }
  }

  // getter per i form control
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
