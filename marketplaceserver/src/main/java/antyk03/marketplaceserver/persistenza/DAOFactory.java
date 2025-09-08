package antyk03.marketplaceserver.persistenza;

import static antyk03.marketplaceserver.enums.EStrategiaPersistenza.DB_HIBERNATE;
import antyk03.marketplaceserver.modello.Configurazione;
import antyk03.marketplaceserver.persistenza.mock.DAODatiUtenteMock;
import antyk03.marketplaceserver.persistenza.mock.DAOProdottoMock;
import antyk03.marketplaceserver.persistenza.mock.DAOUtenteMock;
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

    private DAOFactory() {
        if (Configurazione.getInstance().getStrategiaDb().equals(DB_HIBERNATE)) {

        } else {
            daoUtente = new DAOUtenteMock();
            daoDatiUtente = new DAODatiUtenteMock();
            daoProdotto = new DAOProdottoMock();
        }
    }
}
