package antyk03.marketplaceserver.persistenza;

import static antyk03.marketplaceserver.enums.EStrategiaPersistenza.DB_HIBERNATE;
import antyk03.marketplaceserver.modello.Configurazione;
import antyk03.marketplaceserver.persistenza.mock.DAOUtenteMock;
import lombok.Getter;


public class DAOFactory {

    private static final DAOFactory singleton = new DAOFactory();

    public static DAOFactory getInstance() {
        return singleton;
    }

    @Getter
    private IDAOUtente daoUtente;

    private DAOFactory() {
        if (Configurazione.getInstance().getStrategiaDb().equals(DB_HIBERNATE)) {

        } else {
            daoUtente = new DAOUtenteMock();
        }
    }
}
