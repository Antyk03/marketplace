import { DaoDatiUtenteService } from './../../service/dao/dao-dati_utente.service';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ModelloService } from '../../service/modello.service';
import { DaoUtenteService } from '../../service/dao/dao-utente.service';
import { MessaggiService } from '../../service/messaggi.service';
import { Router } from '@angular/router';
import { C } from '../../service/c';
import { DatiUtente } from '../../model/dati_utente';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})


export class LoginComponent implements OnInit{

  formGroup: FormGroup = new FormGroup ({
    email: new FormControl("", [Validators.required, Validators.email]),
    password: new FormControl("", [Validators.required])
  });

  constructor(
    private modello:ModelloService,
    private daoUtente:DaoUtenteService,
    private messaggi:MessaggiService,
    private router:Router,
    private daoDatiUtente: DaoDatiUtenteService,
  ) {}

  ngOnInit(): void {
      this.modello.clear();
  }



  async onSubmit(): Promise<void> {
    try {
      const utente = await this.daoUtente.login(this.emailFormControl.value, this.passwordFormControl.value);
      this.modello.putPersistentBean(C.UTENTE_LOGIN, utente);
      const datiUtente = await this.daoDatiUtente.getDatiUtente();
      this.modello.putPersistentBean(C.DATI_UTENTE_LOGIN, datiUtente);
      this.router.navigate(['/home']);
    } catch (ex) {
      console.error('Errore ', ex);
      this.messaggi.mostraMessaggioErrore("Impossibile accedere: controllare se le credenziali sono corrette.");
    }
  }

  get emailFormControl():FormControl {
    return this.formGroup.get('email') as FormControl;
  }

  get passwordFormControl(): FormControl {
    return this.formGroup.get('password') as FormControl;
  }

}
