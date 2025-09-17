export class Prodotto {
  public id?: number;

  constructor(readonly nome: string, readonly descrizione: string, readonly prezzo: number, readonly quantita: number, readonly idVenditore: number) {}

}
