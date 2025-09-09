package antyk03.marketplaceserver.rest;

import antyk03.marketplaceserver.modello.dto.OrdineDTO;
import antyk03.marketplaceserver.modello.dto.ProdottoDTO;
import antyk03.marketplaceserver.modello.dto.ProdottoOrdineDTO;
import antyk03.marketplaceserver.service.ServiceDatiUtente;
import antyk03.marketplaceserver.service.ServiceOrdine;
import antyk03.marketplaceserver.service.ServiceUtenti;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;


@Path("/ordine")
@RequestScoped
@Slf4j
public class RisorsaOrdine {

    @Inject
    private ServiceDatiUtente serviceDatiUtente;
    @Inject
    private ServiceUtenti serviceUtenti;
    @Inject
    private ServiceOrdine serviceOrdine;

    @Context
    SecurityContext securityContext;

    //Utente aggiunge prodotto al carrello, un utente USER seleziona un prodotto e lo aggiunge al carrello (ORDINE STATUS.DRAFT)
    @POST
    @Path("/aggiungi/{idProdotto}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Long aggiungiAlCarrello(ProdottoOrdineDTO prodottoOrdineDTO) {
        String email = securityContext.getUserPrincipal().getName();
        return serviceOrdine.aggiungiAlCarrello(prodottoOrdineDTO, email);
    }

    @POST
    @Path("/acquista")
    @Produces(MediaType.APPLICATION_JSON)
    public OrdineDTO effettuaOrdine() {
        String email = securityContext.getUserPrincipal().getName();
        return serviceOrdine.effettuaOrdine(email);
    }

}
