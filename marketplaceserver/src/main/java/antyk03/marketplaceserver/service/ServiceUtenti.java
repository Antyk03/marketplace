package antyk03.marketplaceserver.service;


import antyk03.marketplaceserver.enums.ERuolo;
import antyk03.marketplaceserver.enums.EStatoUtente;
import antyk03.marketplaceserver.modello.DatiUtente;
import antyk03.marketplaceserver.modello.Prodotto;
import antyk03.marketplaceserver.modello.Utente;
import antyk03.marketplaceserver.modello.dto.DatiUtenteDTO;
import antyk03.marketplaceserver.modello.dto.ProdottoDTO;
import antyk03.marketplaceserver.modello.dto.UtenteDTO;
import antyk03.marketplaceserver.persistenza.DAOFactory;
import antyk03.marketplaceserver.persistenza.IDAODatiUtente;
import antyk03.marketplaceserver.persistenza.IDAOProdotto;
import antyk03.marketplaceserver.persistenza.IDAOUtente;
import antyk03.marketplaceserver.util.JWTUtil;
import antyk03.marketplaceserver.util.Mapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Slf4j
public class ServiceUtenti {

    private final IDAOUtente daoUtente = DAOFactory.getInstance().getDaoUtente();
    private final IDAODatiUtente daoDatiUtente = DAOFactory.getInstance().getDaoDatiUtente();
    private final IDAOProdotto daoProdotto = DAOFactory.getInstance().getDaoProdotto();

    public String login(UtenteDTO utenteDTO) {
        Utente utente = daoUtente.findByEmail(utenteDTO.getEmail());
        if (utente == null) {
            throw new IllegalArgumentException("Utente con email " + utenteDTO.getEmail() + " sconosciuto");
        }
        if (!utente.getPassword().equalsIgnoreCase(utenteDTO.getPassword())) {
            throw new IllegalArgumentException("Password scorretta");
        }
        log.debug("L'utente {} ha effettuato il login", utente.getEmail());
        return JWTUtil.generaToken(utenteDTO.getEmail());
    }

    public List<DatiUtenteDTO> getUtenti(String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente == null) {
            throw new IllegalArgumentException("Errore durante l'autenticazione.");
        }
        DatiUtente datiUtente = daoDatiUtente.findByIdUtente(utente.getId());
        if (datiUtente == null) {
            throw new IllegalArgumentException("Nessun informazione trovata sull'utente");
        }
        if (!datiUtente.getRuolo().equals(ERuolo.ADMIN)) {
            throw new IllegalArgumentException("Non sei autorizzato.");
        }
        List<DatiUtenteDTO> datiUtentiDTO = new ArrayList<>();
        for (Utente u : daoUtente.findAll()) {
            DatiUtente du = daoDatiUtente.findByIdUtente(u.getId());
            DatiUtenteDTO duDTO = Mapper.map(du, DatiUtenteDTO.class);
            datiUtentiDTO.add(duDTO);
        }
        return datiUtentiDTO;
    }

    public List<ProdottoDTO> getProdotti(String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente == null) {
            throw new IllegalArgumentException("Errore durante l'autenticazione.");
        }
        DatiUtente datiUtente = daoDatiUtente.findByIdUtente(utente.getId());
        if (datiUtente == null) {
            throw new IllegalArgumentException("Nessun informazione trovata sull'utente");
        }
        if (!datiUtente.getRuolo().equals(ERuolo.ADMIN)) {
            throw new IllegalArgumentException("Non sei autorizzato.");
        }
        List<ProdottoDTO> prodottiDTO = new ArrayList<>();
        for (Prodotto p : daoProdotto.findAll()) {
            ProdottoDTO pDTO = Mapper.map(p, ProdottoDTO.class);
            prodottiDTO.add(pDTO);
        }
        return prodottiDTO;
    }

    public DatiUtenteDTO bloccaOSbloccaUtente (Long idUtente, String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente == null) {
            throw new IllegalArgumentException("Errore durante l'autenticazione.");
        }
        if (utente.getId() == idUtente) {
            throw new IllegalArgumentException("Non puoi bloccare te stesso.");
        }
        DatiUtente utenteLog = daoDatiUtente.findByIdUtente(utente.getId());
        if (utenteLog == null) {
            throw new IllegalArgumentException("Nessuna informazione sull'utente loggato.");
        }
        if (!utenteLog.getRuolo().equals(ERuolo.ADMIN)) {
            throw new IllegalArgumentException("Non hai il permesso.");
        }
        DatiUtente datiUtente = daoDatiUtente.findByIdUtente(idUtente);
        if (datiUtente == null) {
            throw new IllegalArgumentException("Nessuna informazione trovata sull'utente con id: "+ idUtente);
        }
        if (datiUtente.getRuolo().equals(ERuolo.ADMIN)) {
            throw new IllegalArgumentException("Non puoi bloccare un altro admin.");
        }
        if (datiUtente.getStatoUtente().equals(EStatoUtente.ATTIVO)) {
            datiUtente.setStatoUtente(EStatoUtente.BLOCCATO);
        } else if (datiUtente.getStatoUtente().equals(EStatoUtente.BLOCCATO)) {
            datiUtente.setStatoUtente(EStatoUtente.ATTIVO);
        }
        daoDatiUtente.makePersistent(datiUtente);
        DatiUtenteDTO datiUtenteDTO = Mapper.map(datiUtente, DatiUtenteDTO.class);
        return  datiUtenteDTO;
    }

    public void rimuoviProdotto(Long idProdotto, String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente == null) {
            throw new IllegalArgumentException("Errore durante l'autenticazione.");
        }
        DatiUtente utenteLog = daoDatiUtente.findByIdUtente(utente.getId());
        if (utenteLog == null) {
            throw new IllegalArgumentException("Nessuna informazione sull'utente loggato.");
        }
        Prodotto prodotto = daoProdotto.findById(idProdotto);
        if (prodotto == null) {
            throw new IllegalArgumentException("Nessun prodotto trovato con id: " + idProdotto);
        }
        if (!utenteLog.getRuolo().equals(ERuolo.ADMIN)) {
            throw new IllegalArgumentException("Non hai il permesso di eliminare questo prodotto.");
        }
        DatiUtente venditore = daoDatiUtente.findByIdUtente(prodotto.getIdVenditore());
        if (venditore != null) {
            venditore.getProdotti().remove(prodotto);
            daoDatiUtente.makePersistent(venditore);
            daoProdotto.makeTransient(prodotto);
        }
    }

    public String registraUtente(UtenteDTO utenteDTO, DatiUtenteDTO datiUtenteDTO) {
        if (daoUtente.findByEmail(utenteDTO.getEmail()) != null) {
            throw new IllegalArgumentException("Email già registrata.");
        }
        if (datiUtenteDTO.getIdUtente() != null || datiUtenteDTO.getId() != null) {
            throw new IllegalArgumentException("L'id deve essere nullo.");
        }
        DatiUtente datiUtente = Mapper.map(datiUtenteDTO, DatiUtente.class);
        if (datiUtente.getStatoUtente() == null) {
            datiUtente.setStatoUtente(EStatoUtente.ATTIVO);
        }
        if (datiUtenteDTO.getRuolo().equals(ERuolo.ADMIN)) {
            throw new IllegalArgumentException("Ruolo non concesso.");
        }
        Utente utente = new Utente(utenteDTO.getEmail(), utenteDTO.getPassword());
        daoUtente.makePersistent(utente);
        log.info("Utente: " + utenteDTO.getEmail() + " Username: " + datiUtenteDTO.getUsername());
        datiUtente.setIdUtente(utente.getId());
        daoDatiUtente.makePersistent(datiUtente);
        return utente.getId().toString();
    }

    public void eliminaUtente(String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente == null) {
            throw new IllegalArgumentException("Errore durante l'autenticazione.");
        }
        DatiUtente datiUtente = daoDatiUtente.findByIdUtente(utente.getId());
        if (datiUtente == null) {
            daoUtente.makeTransient(utente);
            return;
        }
        if (datiUtente.getRuolo().equals(ERuolo.USER)) {
            daoDatiUtente.makeTransient(datiUtente);
            daoUtente.makeTransient(utente);
        }
        for (Prodotto p: datiUtente.getProdotti()) {
            daoProdotto.makeTransient(p);
        }
        daoDatiUtente.makeTransient(datiUtente);
        daoUtente.makeTransient(utente);
    }

}
