export class ProdottoOrdine {
  public id?: number;

  constructor(readonly idProdotto: number, public quantita: number, readonly prezzoUnitario: number, readonly totale: number | null) {}

  public set setQuantita(quantita: number) {
    this.quantita = quantita;
  }


}
