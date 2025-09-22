import { Component } from '@angular/core';
import { ERuolo } from '../../model/enums/ERuolo';
import { MessaggiService } from '../../service/messaggi.service';
import { FormBuilder, FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { DaoUtenteService } from '../../service/dao/dao-utente.service';
import { RegistrazioneUtente } from '../../model/registrazione_utente';
import { DatiUtente } from '../../model/dati_utente';
import { Utente } from '../../model/utente';
import { EStatoUtente } from '../../model/enums/EStatoUtente';

@Component({
  selector: 'app-registrazione',
  templateUrl: './registrazione.component.html',
  styleUrl: './registrazione.component.css'
})
export class RegistrazioneComponent {
  formGroup!: FormGroup;
  ruoliDisponibili = [ERuolo.USER, ERuolo.VENDOR];

  constructor(private fb: FormBuilder, private messaggiService: MessaggiService, readonly daoUtenti: DaoUtenteService) {}

  ngOnInit(): void {
    this.formGroup = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      ruolo: [this.ruoliDisponibili[0], Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.formGroup.invalid) {
      this.messaggiService.mostraMessaggioErrore("Compila correttamente tutti i campi.");
      return;
    }

    const dati = this.formGroup.value;
    console.log("Dati registrazione:", dati);
    const registrazione = new RegistrazioneUtente(
      {
        email: this.emailFormControl.value,
        password: this.passwordFormControl.value
      },
      {
        username: this.usernameFormControl.value,
        email: this.emailFormControl.value,
        ruolo: this.ruoloFormControl.value
      }
    );
    this.daoUtenti.registraUtente(registrazione);
    this.messaggiService.mostraMessaggioInformazioni("Registrazione completata con successo!");
  }

  // Getter per i controlli
  get usernameFormControl(): FormControl {
    return this.formGroup.get('username') as FormControl;
  }

  get emailFormControl(): FormControl {
    return this.formGroup.get('email') as FormControl;
  }

  get ruoloFormControl(): FormControl {
    return this.formGroup.get('ruolo') as FormControl;
  }

  get passwordFormControl(): FormControl {
    return this.formGroup.get('password') as FormControl;
  }
}
