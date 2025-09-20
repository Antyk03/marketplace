import { DAOProdotti } from './../service/dao/dao-prodotti.service';
import { inject } from "@angular/core";
import { ResolveFn } from "@angular/router";
import { Prodotto } from "../model/prodotto";


export const prodottiResolver: ResolveFn<Prodotto> = (route, state) => {
  const daoProdotti = inject(DAOProdotti);
  const id =+ route.params['idProdotto'];
  const prodotto = daoProdotti.getProdottoById(id);
  if (!prodotto) {
    throw new Error(`Prodotto con id ${id} non trovato`);
  }
  return prodotto;
};
