export class Prodotto {
  public id?: number;


  constructor(readonly nome: string, readonly descrizione: string, readonly prezzo: number, public quantita: number, readonly idVenditore: number) {}

  public set setId(id:number) {
    this.id = id;
  }


}
