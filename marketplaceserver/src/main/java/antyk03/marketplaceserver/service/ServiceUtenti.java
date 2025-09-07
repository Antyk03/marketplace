package antyk03.marketplaceserver.service;


import antyk03.marketplaceserver.modello.Utente;
import antyk03.marketplaceserver.modello.dto.UtenteDTO;
import antyk03.marketplaceserver.persistenza.DAOFactory;
import antyk03.marketplaceserver.persistenza.IDAOUtente;
import antyk03.marketplaceserver.util.JWTUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class ServiceUtenti {

    private final IDAOUtente daoUtente = DAOFactory.getInstance().getDaoUtente();

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

}
