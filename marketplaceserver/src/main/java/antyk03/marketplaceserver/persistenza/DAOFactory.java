package antyk03.marketplaceserver.persistenza;

import static antyk03.marketplaceserver.enums.EStrategiaPersistenza.DB_HIBERNATE;
import antyk03.marketplaceserver.modello.Configurazione;
import antyk03.marketplaceserver.persistenza.hibernate.*;
import antyk03.marketplaceserver.persistenza.mock.*;
import lombok.Getter;


public class DAOFactory {

    private static final DAOFactory singleton = new DAOFactory();

    public static DAOFactory getInstance() {
        return singleton;
    }

    @Getter
    private IDAOUtente daoUtente;
    @Getter
    private IDAODatiUtente daoDatiUtente;
    @Getter
    private IDAOProdotto daoProdotto;
    @Getter
    private IDAOOrdine daoOrdine;
    @Getter
    private IDAOProdottoOrdine daoProdottoOrdine;

    private DAOFactory() {
        if (Configurazione.getInstance().getStrategiaDb().equals(DB_HIBERNATE)) {
            daoUtente = new DAOUtenteHibernate();
            daoDatiUtente = new DAODatiUtenteHibernate();
            daoProdotto = new DAOProdottoHibernate();
            daoOrdine = new DAOOrdineHibernate();
            daoProdottoOrdine = new DAOProdottoOrdineHibernate();
        } else {
            daoUtente = new DAOUtenteMock();
            daoDatiUtente = new DAODatiUtenteMock();
            daoProdotto = new DAOProdottoMock();
            daoOrdine = new DAOOrdineMock();
            daoProdottoOrdine = new DAOProdottoOrdineMock();
        }
    }
}
