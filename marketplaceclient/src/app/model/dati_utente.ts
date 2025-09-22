import { ERuolo } from "./enums/ERuolo";
import { EStatoUtente } from "./enums/EStatoUtente";

export class DatiUtente {
  public id?: number;

  constructor(readonly idUtente: number | null, readonly username: string, readonly email:string, readonly ruolo:ERuolo, public statoUtente: EStatoUtente) {}

}
