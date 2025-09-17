import { EStatus } from "./enums/EStatus";
import { ProdottoOrdine } from "./prodotto_ordine";

export class Ordine {
  public id?: number;

  constructor(readonly idUtente: number, readonly status: EStatus, readonly dataCreazione: string, readonly valuta: string, readonly totale: number, readonly prodottiOrdineDTO: ProdottoOrdine[]) {}


}
