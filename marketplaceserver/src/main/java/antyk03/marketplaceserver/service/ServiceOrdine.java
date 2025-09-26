package antyk03.marketplaceserver.service;


import antyk03.marketplaceserver.enums.ERuolo;
import antyk03.marketplaceserver.enums.EStatoUtente;
import antyk03.marketplaceserver.enums.EStatus;
import antyk03.marketplaceserver.enums.EStrategiaPersistenza;
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
import org.glassfish.jersey.servlet.WebConfig;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ApplicationScoped
@Slf4j
public class ServiceOrdine {

    private IDAOUtente daoUtente = DAOFactory.getInstance().getDaoUtente();
    private IDAODatiUtente daoDatiUtente = DAOFactory.getInstance().getDaoDatiUtente();
    private IDAOProdotto daoProdotto = DAOFactory.getInstance().getDaoProdotto();
    private IDAOProdottoOrdine daoProdottoOrdine = DAOFactory.getInstance().getDaoProdottoOrdine();
    private IDAOOrdine daoOrdine = DAOFactory.getInstance().getDaoOrdine();

    public OrdineDTO visualizzaCarrello(String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente==null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }
        Long idUtente = utente.getId();
        DatiUtente datiUtente = daoDatiUtente.findByIdUtente(idUtente);
        if (datiUtente == null) {
            throw new IllegalArgumentException("Nessun informazione trovata.");
        }
        if (datiUtente.getStatoUtente() == EStatoUtente.BLOCCATO) {
            throw new IllegalArgumentException("Utente bloccato.");
        }
        if (datiUtente.getRuolo() == ERuolo.VENDOR) {
            throw new IllegalArgumentException("Utente VENDOR non può avere un carrello");
        }
        Ordine carrello = daoOrdine.findCarrelloUtente(idUtente);
        OrdineDTO carrelloDTO = new OrdineDTO();
        if (carrello == null) {
            return carrelloDTO;
        }
        List<ProdottoOrdineDTO> prodottiDTO = new ArrayList<>();
        List<ProdottoOrdine> prodottiCarrello;
        if (Configurazione.getInstance().getStrategiaDb().equals(EStrategiaPersistenza.DB_HIBERNATE)) {
            prodottiCarrello = daoProdottoOrdine.findByIdOrdineAndOrdineStatus(carrello.getId(), EStatus.DRAFT);
        } else {
            prodottiCarrello = carrello.getProdotti();
        }
        double totale = 0;
        for (ProdottoOrdine po: prodottiCarrello) {
            po.setTotale(po.getPrezzoUnitario().multiply(BigDecimal.valueOf(po.getQuantita())));
            totale +=  po.getTotale().doubleValue();
            ProdottoOrdineDTO poDTO = Mapper.map(po, ProdottoOrdineDTO.class);
            prodottiDTO.add(poDTO);
        }
        carrelloDTO.setId(carrello.getId());
        carrelloDTO.setStatus(carrello.getStatus());
        carrelloDTO.setValuta(carrello.getValuta());
        carrello.setTotale(BigDecimal.valueOf(totale));
        log.info(String.valueOf(carrello.getTotale()));
        carrelloDTO.setTotale(carrello.getTotale());
        carrelloDTO.setDataCreazione(carrello.getDataCreazione());
        carrelloDTO.setProdottiOrdineDTO(prodottiDTO);
        return carrelloDTO;
    }

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
        if (datiUtente.getStatoUtente() == EStatoUtente.BLOCCATO) {
            throw new IllegalArgumentException("Utente bloccato.");
        }
        if (datiUtente.getRuolo() == ERuolo.VENDOR) {
            throw new IllegalArgumentException("Non hai il permesso di acquistare o aggiungere al carrello dei prodotti.");
        }
        Ordine ordine = daoOrdine.findCarrelloUtente(idUtente);
        if (ordine == null) {
            ordine = new Ordine(idUtente, EStatus.DRAFT, LocalDateTime.now(), "EUR");
            daoOrdine.makePersistent(ordine);
        }
        Prodotto prodotto = daoProdotto.findById(prodottoOrdineDTO.getIdProdotto());
        if (prodotto == null) {
            throw new IllegalArgumentException("Prodotto con id " + prodottoOrdineDTO.getIdProdotto() + " non esistente.");
        }
        if (prodotto.getPrezzo()== null) {
            throw new IllegalArgumentException("Prodott senza prezzo");
        }
        if (prodottoOrdineDTO.getQuantita() > prodotto.getQuantita()) {
            throw new IllegalArgumentException("Impossibile aggiungere più di " + prodotto.getQuantita() + " al carrello.");
        }
        ProdottoOrdine vecchioProdottoOrdine = null;
        if (Configurazione.getInstance().getStrategiaDb().equals(EStrategiaPersistenza.DB_HIBERNATE)) {
            List<ProdottoOrdine> lista = daoProdottoOrdine.findByIdOrdineAndOrdineStatus(ordine.getId(), EStatus.DRAFT);
            if (lista == null) {
                lista = java.util.Collections.emptyList();
            }
            for (ProdottoOrdine po: lista) {
                if (Long.valueOf(po.getIdProdotto()).equals(prodotto.getId())) {
                    vecchioProdottoOrdine = po;
                    break;
                }
            }
        } else {
            vecchioProdottoOrdine = ordine.cercaProdottoOrdine(prodotto.getId());
        }
        if (vecchioProdottoOrdine != null) {
            int quantita = vecchioProdottoOrdine.getQuantita() + prodottoOrdineDTO.getQuantita();
            if (quantita > prodotto.getQuantita()) {
                throw new IllegalArgumentException("Impossibile superare il limite di magazzino del prodotto.");
            }
            vecchioProdottoOrdine.setQuantita(quantita);
            vecchioProdottoOrdine.setPrezzoUnitario(prodottoOrdineDTO.getPrezzoUnitario());
            vecchioProdottoOrdine.calcolaTotale();
            daoProdottoOrdine.makePersistent(vecchioProdottoOrdine);
            return vecchioProdottoOrdine.getId();
        } else {
            ProdottoOrdine prodottoOrdine = Mapper.map(prodottoOrdineDTO, ProdottoOrdine.class);
            prodottoOrdine.setIdOrdine(ordine.getId());
            prodottoOrdine.calcolaTotale();
            if (!Configurazione.getInstance().getStrategiaDb().equals(EStrategiaPersistenza.DB_HIBERNATE)) {
                ordine.getProdotti().add(prodottoOrdine);
            }
            ordine.setIdUtente(idUtente);
            daoProdottoOrdine.makePersistent(prodottoOrdine);
            daoOrdine.makePersistent(ordine);
            return prodottoOrdine.getId();
        }
    }

    public OrdineDTO effettuaOrdine(String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }
        DatiUtente datiUtente = daoDatiUtente.findByIdUtente(utente.getId());
        if (datiUtente == null) {
            throw new IllegalArgumentException("Nessuna informazione sull'utente");
        }
        if (datiUtente.getRuolo().equals(ERuolo.VENDOR)) {
            throw new IllegalArgumentException("Impossibile ordinare da venditore.");
        }
        Ordine carrello = daoOrdine.findCarrelloUtente(utente.getId());
        if (carrello == null) {
            throw new IllegalArgumentException("Nessun carrello, impossibile completare l'ordine");
        }
        List<ProdottoOrdineDTO> prodottiDTO = new ArrayList<>();
        Iterator<ProdottoOrdine> it;
        if (Configurazione.getInstance().getStrategiaDb().equals(EStrategiaPersistenza.DB_HIBERNATE)) {
            it = daoProdottoOrdine.findByIdOrdineAndOrdineStatus(carrello.getId(), EStatus.DRAFT).iterator();
        } else {
             it = carrello.getProdotti().iterator();
        }
        double totale = 0;
        while (it.hasNext()) {
            ProdottoOrdine po = it.next();
            totale += po.getTotale().doubleValue();
            Prodotto p = daoProdotto.findById(po.getIdProdotto());
            if (p == null || p.getQuantita() <= 0) {
                // esaurito → tolgo dal carrello
                if (Configurazione.getInstance().getStrategiaDb().equals(EStrategiaPersistenza.DB_HIBERNATE)) {
                    daoProdottoOrdine.makeTransient(po);
                } else {
                    it.remove();
                }
                continue;
            }
            // Se la quantità richiesta è maggiore della disponibile → riduco
            int acquistabili = Math.min(po.getQuantita(), p.getQuantita());
            if (acquistabili <= 0) {
                if (Configurazione.getInstance().getStrategiaDb().equals(EStrategiaPersistenza.DB_HIBERNATE)) {
                    daoProdottoOrdine.makeTransient(po);
                } else {
                    it.remove();
                }
                continue;
            }
            // aggiorno stock prodotto
            p.setQuantita(p.getQuantita() - acquistabili);
            daoProdotto.makePersistent(p);
            // aggiorno quantità effettiva dell’ordine
            po.setQuantita(acquistabili);
            ProdottoOrdineDTO poDTO = Mapper.map(po, ProdottoOrdineDTO.class);
            prodottiDTO.add(poDTO);
        }
        if (prodottiDTO.isEmpty()) {
            // nessun prodotto valido
            daoOrdine.makePersistent(carrello); // salva eventuali modifiche al carrello
            throw new IllegalArgumentException("Carrello vuoto o prodotti non disponibili");
        }
        carrello.setStatus(EStatus.PAID);
        carrello.setTotale(BigDecimal.valueOf(totale));
        daoOrdine.makePersistent(carrello);
        OrdineDTO ordineDTO = new OrdineDTO();
        ordineDTO.setId(carrello.getId());
        ordineDTO.setProdottiOrdineDTO(prodottiDTO);
        ordineDTO.setStatus(EStatus.PAID);
        ordineDTO.setDataCreazione(carrello.getDataCreazione());
        ordineDTO.setValuta(carrello.getValuta());
        ordineDTO.setTotale(carrello.getTotale());
        return ordineDTO;
    }


    public List<OrdineDTO> visualizzaStorico (String email) {
        Utente utente = daoUtente.findByEmail(email);
        if (utente == null) {
            throw new IllegalArgumentException("Utente non autorizzato.");
        }
        DatiUtente datiUtente = daoDatiUtente.findByIdUtente(utente.getId());
        if (datiUtente == null) {
            throw new IllegalArgumentException("Nessun dato registrato sull'utente");
        }
        if (datiUtente.getStatoUtente() == EStatoUtente.BLOCCATO) {
            throw new IllegalArgumentException("Utente bloccato.");
        }
        if (datiUtente.getRuolo() == ERuolo.VENDOR) {
            throw new IllegalArgumentException("Non puoi vedere lo storico, non hai il permesso");
        }
        List<OrdineDTO> ordiniDTO = new ArrayList<>();
        List<Ordine> ordiniUtente = daoOrdine.findByIdUtente(utente.getId());
        for (Ordine o: ordiniUtente) {
            List<ProdottoOrdineDTO> listaProdotti = new ArrayList<>();
            for (ProdottoOrdine po: o.getProdotti()) {
                ProdottoOrdineDTO poDTO = Mapper.map(po, ProdottoOrdineDTO.class);
                listaProdotti.add(poDTO);
            }
            OrdineDTO ordineDTO = Mapper.map(o, OrdineDTO.class);
            ordineDTO.setProdottiOrdineDTO(listaProdotti);
            ordiniDTO.add(ordineDTO);
        }
        return ordiniDTO;
    }

    public void rimuoviProdotto(Long idProdotto, String email) {
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
            throw new IllegalArgumentException("Carrello vuoto.");
        }
        if (Configurazione.getInstance().getStrategiaDb().equals(EStrategiaPersistenza.DB_HIBERNATE)) {
            List<ProdottoOrdine> prodotti = daoProdottoOrdine.findByIdOrdineAndOrdineStatus(ordine.getId(), EStatus.DRAFT);
            ProdottoOrdine toRemove = prodotti.stream().filter(p -> p.getIdProdotto().equals(idProdotto)).findFirst().orElse(null);
            if (toRemove == null) {
                throw new IllegalArgumentException("Prodotto non trovato nel carrello");
            }
        } else {
            boolean removed = ordine.getProdotti().removeIf(p -> p.getIdProdotto().equals(idProdotto));
            if (!removed) {
                throw new IllegalArgumentException("Prodotto non trovato nel carrello");
            }
        }
        daoOrdine.makePersistent(ordine);
    }

}
