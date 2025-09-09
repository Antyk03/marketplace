package antyk03.marketplaceserver.service;


import antyk03.marketplaceserver.enums.ERuolo;
import antyk03.marketplaceserver.enums.EStatus;
import antyk03.marketplaceserver.modello.*;
import antyk03.marketplaceserver.modello.dto.OrdineDTO;
import antyk03.marketplaceserver.modello.dto.ProdottoDTO;
import antyk03.marketplaceserver.modello.dto.ProdottoOrdineDTO;
import antyk03.marketplaceserver.persistenza.*;
import antyk03.marketplaceserver.persistenza.mock.DAOProdottoOrdineMock;
import antyk03.marketplaceserver.persistenza.mock.DAOUtenteMock;
import antyk03.marketplaceserver.util.Mapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Slf4j
public class ServiceOrdine {

    private IDAOUtente daoUtente = DAOFactory.getInstance().getDaoUtente();
    private IDAODatiUtente daoDatiUtente = DAOFactory.getInstance().getDaoDatiUtente();
    private IDAOProdotto daoProdotto = DAOFactory.getInstance().getDaoProdotto();
    private IDAOProdottoOrdine daoProdottoOrdine = DAOFactory.getInstance().getDaoProdottoOrdine();
    private IDAOOrdine daoOrdine = DAOFactory.getInstance().getDaoOrdine();

    public Long aggiungiAlCarrello(ProdottoOrdineDTO prodottoOrdineDTO, String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }
        Long idUtente = utente.getId();
        DatiUtente datiUtente = daoDatiUtente.findByIdUtente(idUtente);
        if (datiUtente == null) {
            throw new IllegalArgumentException("Nessun dato trovato per l'utente");
        }
        if (datiUtente.getRuolo() == ERuolo.VENDOR) {
            throw new IllegalArgumentException("Non hai il permesso di acquistare o aggiungere al carrello dei prodotti.");
        }
        Ordine ordine = daoOrdine.findCarrelloUtente(idUtente);
        if (ordine == null) {
            ordine = new Ordine(idUtente, EStatus.DRAFT, LocalDateTime.now(), "EUR");
        }
        Prodotto prodotto = daoProdotto.findById(prodottoOrdineDTO.getIdProdotto());
        if (prodotto == null) {
            throw new IllegalArgumentException("Prodotto con id " + prodottoOrdineDTO.getIdProdotto() + " non esistente.");
        }
        if (prodottoOrdineDTO.getQuantita() > prodotto.getQuantita()) {
            throw new IllegalArgumentException("Impossibile aggiungere più di " + prodotto.getQuantita() + " al carrello.");
        }
        ProdottoOrdine prodottoOrdine = Mapper.map(prodottoOrdineDTO, ProdottoOrdine.class);
        prodottoOrdine.calcolaTotale();
        ProdottoOrdine vecchioProdottoOrdine = ordine.cercaProdottoOrdine(prodotto.getId());
        if (vecchioProdottoOrdine != null) {
            int quantita = vecchioProdottoOrdine.getQuantita() + prodottoOrdine.getQuantita();
            if (quantita > prodotto.getQuantita()) {
                throw new IllegalArgumentException("Impossibile superare il limite di magazzino del prodotto.");
            }
            vecchioProdottoOrdine.setQuantita(quantita);
            vecchioProdottoOrdine.calcolaTotale();
            daoProdottoOrdine.makePersistent(vecchioProdottoOrdine);
            return vecchioProdottoOrdine.getId();
        } else {
            ordine.getProdotti().add(prodottoOrdine);
            ordine.setIdUtente(idUtente);
            daoProdottoOrdine.makePersistent(prodottoOrdine);
            daoOrdine.makePersistent(ordine);
            return prodottoOrdine.getId();
        }
    }

    public OrdineDTO effettuaOrdine (String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }
        DatiUtente datiUtente = daoDatiUtente.findByIdUtente(utente.getId());
        if (datiUtente == null) {
            throw new IllegalArgumentException("Nessun informazione sull'utente");
        }
        if (datiUtente.getRuolo().equals(ERuolo.VENDOR)) {
            throw new IllegalArgumentException("Impossibile ordinare da venditore.");
        }
        Ordine carrello = daoOrdine.findCarrelloUtente(utente.getId());
        if (carrello == null) {
            throw new IllegalArgumentException("Nessun carrello, impossibile completare l'ordine");
        }
        List<ProdottoOrdineDTO> prodottiDTO = new ArrayList<>();
        for (ProdottoOrdine po: carrello.getProdotti()) {
            Prodotto p = daoProdotto.findById(po.getIdProdotto());
            if (po.getQuantita() > p.getQuantita()) {
                throw new IllegalArgumentException("Stock insufficiente per il prodotto: " + p.getNome());
            }
            p.setQuantita(p.getQuantita() - po.getQuantita());
            if (p.getQuantita() == 0) {
                daoProdotto.makeTransient(p);
            }
            ProdottoOrdineDTO poDTO = Mapper.map(po, ProdottoOrdineDTO.class);
            prodottiDTO.add(poDTO);
        }
        OrdineDTO ordineDTO = new OrdineDTO();
        ordineDTO.setId(carrello.getId());
        ordineDTO.setProdottiOrdineDTO(prodottiDTO);
        ordineDTO.setStatus(EStatus.PAID);
        ordineDTO.setDataCreazione(carrello.getDataCreazione());
        ordineDTO.setValuta(carrello.getValuta());
        carrello.calcolaTotale();
        ordineDTO.setTotale(carrello.getTotale());
        carrello.setStatus(EStatus.PAID);
        daoOrdine.makePersistent(carrello);
        return ordineDTO;
    }

}
