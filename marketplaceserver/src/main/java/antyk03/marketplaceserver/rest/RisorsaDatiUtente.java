package antyk03.marketplaceserver.rest;


import antyk03.marketplaceserver.modello.dto.DatiUtenteDTO;
import antyk03.marketplaceserver.modello.dto.ProdottoDTO;
import antyk03.marketplaceserver.service.ServiceDatiUtente;
import antyk03.marketplaceserver.service.ServiceUtenti;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;

import javax.print.attribute.standard.Media;
import java.util.List;

@Path("/me")
@RequestScoped
@Slf4j
public class RisorsaDatiUtente {

    @Inject
    private ServiceUtenti serviceUtenti;
    @Inject
    private ServiceDatiUtente serviceDatiUtente;

    @Context
    private SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public DatiUtenteDTO getInformazioni() {
        String email = securityContext.getUserPrincipal().getName();
        return serviceDatiUtente.getInformazioni(email);
    }

    @GET
    @Path("/prodotti")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProdottoDTO> getProdotti() {
        String email = securityContext.getUserPrincipal().getName();
        return serviceDatiUtente.getProdotti(email);
    }

    @POST
    @Path("/aggiungi")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public long aggiungiProdotto(@NotNull @Valid ProdottoDTO prodottoDTO) {
        String email = securityContext.getUserPrincipal().getName();
        return serviceDatiUtente.aggiungiProdotto(prodottoDTO, email);
    }

    @POST
    @Path("/modifica/{idProdotto}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public void modificaRimuoviProdotto(@NotNull @PathParam("idProdotto") Long idProdotto, ProdottoDTO prodottoDTO) {
        if (prodottoDTO != null) {
            if (idProdotto != prodottoDTO.getId()) {
                throw new IllegalArgumentException("Gli id non coincidono.");
            }
        }
        String email = securityContext.getUserPrincipal().getName();
        serviceDatiUtente.modificaRimuoviProdotto(idProdotto, prodottoDTO, email);
    }

}
