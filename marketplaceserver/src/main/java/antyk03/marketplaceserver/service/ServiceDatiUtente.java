package antyk03.marketplaceserver.service;

import antyk03.marketplaceserver.modello.DatiUtente;
import antyk03.marketplaceserver.modello.Utente;
import antyk03.marketplaceserver.modello.dto.DatiUtenteDTO;
import antyk03.marketplaceserver.persistenza.DAOFactory;
import antyk03.marketplaceserver.persistenza.IDAODatiUtente;
import antyk03.marketplaceserver.persistenza.IDAOUtente;
import antyk03.marketplaceserver.util.Mapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class ServiceDatiUtente {

    private IDAOUtente daoUtente = DAOFactory.getInstance().getDaoUtente();
    private IDAODatiUtente daoDatiUTente = DAOFactory.getInstance().getDaoDatiUtente();

    public DatiUtenteDTO getInformazioni(String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente == null) {
            log.info("Nessun utente trovato per {}", email);
            throw new IllegalArgumentException("Errore durante l'autenticazione con mail :" + email);
        }
        DatiUtente datiUtente = daoDatiUTente.findByIdUtente(utente.getId());
        if (datiUtente == null) {
            throw new IllegalArgumentException("Nessun dato trovato per " + email);
        }
        DatiUtenteDTO datiUtenteDTO = Mapper.map(datiUtente, DatiUtenteDTO.class);
        return datiUtenteDTO;
    }



}
