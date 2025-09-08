package antyk03.marketplaceserver.service;

import antyk03.marketplaceserver.enums.ERuolo;
import antyk03.marketplaceserver.modello.DatiUtente;
import antyk03.marketplaceserver.modello.Prodotto;
import antyk03.marketplaceserver.modello.Utente;
import antyk03.marketplaceserver.modello.dto.DatiUtenteDTO;
import antyk03.marketplaceserver.modello.dto.ProdottoDTO;
import antyk03.marketplaceserver.persistenza.DAOFactory;
import antyk03.marketplaceserver.persistenza.IDAODatiUtente;
import antyk03.marketplaceserver.persistenza.IDAOProdotto;
import antyk03.marketplaceserver.persistenza.IDAOUtente;
import antyk03.marketplaceserver.util.Mapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Slf4j
public class ServiceDatiUtente {

    private IDAOUtente daoUtente = DAOFactory.getInstance().getDaoUtente();
    private IDAODatiUtente daoDatiUTente = DAOFactory.getInstance().getDaoDatiUtente();
    private IDAOProdotto daoProdotto = DAOFactory.getInstance().getDaoProdotto();

    public DatiUtenteDTO getInformazioni(String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente == null) {
            log.info("getInformazioni/ Nessun utente trovato per {}", email);
            throw new IllegalArgumentException("Errore durante l'autenticazione con mail :" + email);
        }
        DatiUtente datiUtente = daoDatiUTente.findByIdUtente(utente.getId());
        if (datiUtente == null) {
            throw new IllegalArgumentException("Nessun dato trovato per " + email);
        }
        DatiUtenteDTO datiUtenteDTO = Mapper.map(datiUtente, DatiUtenteDTO.class);
        return datiUtenteDTO;
    }

    public long aggiungiProdotto (ProdottoDTO prodottoDTO, String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente == null) {
            log.info("findByEmail/ Nessun utente trovato per {}", email);
            throw new IllegalArgumentException("Errore durante l'autenticazione con mail :" + email);
        }
        DatiUtente datiUtente = daoDatiUTente.findByIdUtente(utente.getId());
        if (datiUtente == null) {
            log.info("findByEmail/ Nessun dato utente trovato");
            throw new IllegalArgumentException("Nessun dato trovato per " + email);
        }
        if (datiUtente.getRuolo() == ERuolo.USER) {
            log.info("findByEmail/ Semplice utente, non venditore.");
            throw new IllegalArgumentException("Non hai il permesso per vendere.");
        }
        Prodotto prodotto = Mapper.map(prodottoDTO, Prodotto.class);
        datiUtente.getProdotti().add(prodotto);
        daoDatiUTente.makePersistent(datiUtente);
        daoProdotto.makePersistent(prodotto);
        return prodotto.getId();
    }

    public List<ProdottoDTO> getProdotti(String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente == null) {
            log.info("getProdotti/ Nessun utente trovato per {}", email);
            throw new IllegalArgumentException("Errore durante l'autenticazione con mail :" + email);
        }
        DatiUtente datiUtente = daoDatiUTente.findByIdUtente(utente.getId());
        if (datiUtente == null) {
            log.info("getProdotti/ Nessun dato utente trovato");
            throw new IllegalArgumentException("Nessun dato trovato per " + email);
        }
        if (datiUtente.getRuolo() == ERuolo.USER) {
            log.info("getProdotti/ Semplice utente, non venditore.");
            throw new IllegalArgumentException("Non puoi avere dei prodotti, sei un utente semplice.");
        }
        List<ProdottoDTO> prodottiDTO = new ArrayList<>();
        for (Prodotto p: datiUtente.getProdotti()) {
            ProdottoDTO prodottoDTO = Mapper.map(p, ProdottoDTO.class);
            prodottiDTO.add(prodottoDTO);
        }
        return prodottiDTO;
    }

    public void modificaRimuoviProdotto(Long idProdotto, ProdottoDTO prodottoDTO, String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente == null) {
            throw new IllegalArgumentException(" Nessun utente autenticato");
        }
        DatiUtente datiUtente = daoDatiUTente.findByIdUtente(utente.getId());
        if (datiUtente == null) {
            throw new IllegalArgumentException("Impossibile trovare dati per utente");
        }
        Prodotto p = daoProdotto.findById(idProdotto);
        if (p == null) {
            throw new IllegalArgumentException("Nessun prodotto trovato con l'id inserito");
        }
        if (datiUtente.getProdottoById(idProdotto) == null) {
            throw new IllegalArgumentException("Nessun prodotto trovato tra quelli posseduti.");
        }
        if (prodottoDTO == null) {
            datiUtente.getProdotti().remove(p);
            daoProdotto.makeTransient(p);
        } else {
            Prodotto nuovoProdotto = Mapper.map(prodottoDTO, Prodotto.class);
            datiUtente.getProdotti().remove(p);
            datiUtente.getProdotti().add(nuovoProdotto);
            daoProdotto.makeTransient(p);
            daoProdotto.makePersistent(nuovoProdotto);
        }
        daoDatiUTente.makePersistent(datiUtente);
    }


}
