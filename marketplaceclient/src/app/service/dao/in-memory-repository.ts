import { InMemoryDbService } from "angular-in-memory-web-api";
import { ERuolo } from "../../model/enums/ERuolo";
import { EStatoUtente } from "../../model/enums/EStatoUtente";
import { RequestInfo } from 'angular-in-memory-web-api';


export class InMemoryRepository extends InMemoryDbService {

  createDb() {
    console.log('Inizializzato db');
    let utenti = [
      { id: 1, email: "admin@admin.it", password: "Admin!"},
      { id: 2, email: "utente@utente.it", password: "Utente!"},
    ];
    let me = [
      { id: 3, idUtente: 1, username: "admin", email: "admin@admin.it", ruolo: ERuolo.ADMIN, stato: EStatoUtente.ATTIVO},
      { id: 4, idUtente: 2, username: "username", email: "utente@utente.it", ruolo: ERuolo.USER, stato: EStatoUtente.ATTIVO}
    ]
    return {utenti, me};
  }
/*
  get(reqInfo: RequestInfo) {
    if (reqInfo.collectionName === 'me') {
      // 👇 Qui puoi decidere come rispondere: ad esempio restituisco sempre l'admin
      const utente = {
        id: 1,
        username: "admin",
        email: "admin@admin.it",
        ruolo: ERuolo.ADMIN,
        stato: EStatoUtente.ATTIVO
      };
      return reqInfo.utils.createResponse$(() => {
        return {
          body: utente,
          status: 200
        };
      });
    }
    // Default → comportamento normale
    return undefined;
  }
*/
}
