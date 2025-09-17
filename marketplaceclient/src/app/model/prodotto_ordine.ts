export class ProdottoOrdine {
  public id?: number;

  constructor(readonly idProdotto: number, readonly quantita: number, readonly prezzounitario: number, readonly totale: number) {}


}
