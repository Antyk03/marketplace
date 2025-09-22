import { DatiUtente } from "./dati_utente";
import { Utente } from "./utente";

export class RegistrazioneUtente {
  constructor(
    public utenteDTO: { email: string; password: string },
    public datiUtenteDTO: { username: string; email: string; ruolo: string }
  ) {}
}
