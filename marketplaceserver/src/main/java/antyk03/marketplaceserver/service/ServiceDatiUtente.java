package antyk03.marketplaceserver.service;

import antyk03.marketplaceserver.enums.ERuolo;
import antyk03.marketplaceserver.enums.EStatoUtente;
import antyk03.marketplaceserver.enums.EStatus;
import antyk03.marketplaceserver.modello.*;
import antyk03.marketplaceserver.modello.dto.DatiUtenteDTO;
import antyk03.marketplaceserver.modello.dto.ProdottoDTO;
import antyk03.marketplaceserver.modello.dto.ProdottoOrdineDTO;
import antyk03.marketplaceserver.persistenza.*;
import antyk03.marketplaceserver.util.Mapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Slf4j
public class ServiceDatiUtente {

    private IDAOUtente daoUtente = DAOFactory.getInstance().getDaoUtente();
    private IDAODatiUtente daoDatiUtente = DAOFactory.getInstance().getDaoDatiUtente();
    private IDAOProdotto daoProdotto = DAOFactory.getInstance().getDaoProdotto();
    private IDAOOrdine daoOrdine = DAOFactory.getInstance().getDaoOrdine();

    public DatiUtenteDTO getInformazioni(String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente == null) {
            log.info("getInformazioni/ Nessun utente trovato per {}", email);
            throw new IllegalArgumentException("Errore durante l'autenticazione con mail :" + email);
        }
        DatiUtente datiUtente = daoDatiUtente.findByIdUtente(utente.getId());
        if (datiUtente == null) {
            throw new IllegalArgumentException("Nessun dato trovato per " + email);
        }
        if (datiUtente.getStatoUtente() == EStatoUtente.BLOCCATO) {
            throw new IllegalArgumentException("Utente bloccato.");
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
        DatiUtente datiUtente = daoDatiUtente.findByIdUtente(utente.getId());
        if (datiUtente == null) {
            log.info("findByEmail/ Nessun dato utente trovato");
            throw new IllegalArgumentException("Nessun dato trovato per " + email);
        }
        if (datiUtente.getStatoUtente() == EStatoUtente.BLOCCATO) {
            throw new IllegalArgumentException("Utente bloccato.");
        }
        if (prodottoDTO.getIdVenditore() != null && prodottoDTO.getIdVenditore() != datiUtente.getIdUtente()) {
            throw new IllegalArgumentException("Id degli utenti non coincidenti.");
        }
        if (datiUtente.getRuolo() == ERuolo.USER) {
            log.info("findByEmail/ Semplice utente, non venditore.");
            throw new IllegalArgumentException("Non hai il permesso per vendere.");
        }
        Prodotto prodotto = Mapper.map(prodottoDTO, Prodotto.class);
        datiUtente.getProdotti().add(prodotto);
        prodotto.setIdVenditore(datiUtente.getIdUtente());
        daoDatiUtente.makePersistent(datiUtente);
        daoProdotto.makePersistent(prodotto);
        return prodotto.getId();
    }

    public List<ProdottoDTO> getProdotti(String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente == null) {
            log.info("getProdotti/ Nessun utente trovato per {}", email);
            throw new IllegalArgumentException("Errore durante l'autenticazione con mail :" + email);
        }
        DatiUtente datiUtente = daoDatiUtente.findByIdUtente(utente.getId());
        if (datiUtente == null) {
            log.info("getProdotti/ Nessun dato utente trovato");
            throw new IllegalArgumentException("Nessun dato trovato per " + email);
        }
        if (datiUtente.getStatoUtente() == EStatoUtente.BLOCCATO) {
            throw new IllegalArgumentException("Utente bloccato.");
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
        DatiUtente datiUtente = daoDatiUtente.findByIdUtente(utente.getId());
        if (datiUtente == null) {
            throw new IllegalArgumentException("Impossibile trovare dati per utente");
        }
        if (datiUtente.getStatoUtente() == EStatoUtente.BLOCCATO) {
            throw new IllegalArgumentException("Utente bloccato.");
        }
        if (datiUtente.getRuolo() == ERuolo.USER) {
            throw new IllegalArgumentException("Non hai il permesso per vendere.");
        }
        Prodotto p = daoProdotto.findById(idProdotto);
        if (p == null) {
            throw new IllegalArgumentException("Nessun prodotto trovato con l'id inserito");
        }
        if (datiUtente.getProdottoById(idProdotto) == null) {
            throw new IllegalArgumentException("Nessun prodotto trovato tra quelli posseduti.");
        }
        if (prodottoDTO != null && prodottoDTO.getIdVenditore() != p.getIdVenditore() && prodottoDTO.getIdVenditore() != null) {
            throw new IllegalArgumentException("Impossibile cambiare l'id del venditore.");
        }
        if (prodottoDTO == null) {
            datiUtente.getProdotti().remove(p);
            daoProdotto.makeTransient(p);
        } else {
            Prodotto nuovoProdotto = Mapper.map(prodottoDTO, Prodotto.class);
            if (nuovoProdotto.getIdVenditore() == null) {
                nuovoProdotto.setIdVenditore(datiUtente.getIdUtente());
            }
            datiUtente.getProdotti().remove(p);
            datiUtente.getProdotti().add(nuovoProdotto);
            daoProdotto.makeTransient(p);
            daoProdotto.makePersistent(nuovoProdotto);
        }
        daoDatiUtente.makePersistent(datiUtente);
    }

    public List<ProdottoDTO> visualizzaCatalogo() {
        List<Prodotto> prodotti = daoProdotto.findAllBuyable();
        List<ProdottoDTO> prodottiDTO = new ArrayList<>();
        for (Prodotto p : prodotti) {
            prodottiDTO.add(Mapper.map(p, ProdottoDTO.class));
        }
        if (prodottiDTO == null) {
            throw new IllegalArgumentException("Nessun prodotto nel catalogo.");
        }
        return prodottiDTO;
    }

    public void svuotaCarrello(String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }
        Long idUtente = utente.getId();
        DatiUtente datiUtente = daoDatiUtente.findByIdUtente(idUtente);
        if (datiUtente == null) {
            throw new IllegalArgumentException("Nessun dato trovato per l'utente");
        }
        if (datiUtente.getStatoUtente() == EStatoUtente.BLOCCATO) {
            throw new IllegalArgumentException("Utente bloccato.");
        }
        if (datiUtente.getRuolo() == ERuolo.VENDOR) {
            throw new IllegalArgumentException("Non hai il permesso di acquistare o aggiungere al carrello dei prodotti.");
        }
        Ordine ordine = daoOrdine.findCarrelloUtente(idUtente);
        if (ordine == null) {
            throw new IllegalArgumentException("Carrello già vuoto.");
        }
        ordine.getProdotti().clear();
    }


}
