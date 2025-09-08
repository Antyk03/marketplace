package antyk03.marketplaceserver.persistenza.mock;

import antyk03.marketplaceserver.enums.ERuolo;
import antyk03.marketplaceserver.modello.DatiUtente;
import antyk03.marketplaceserver.modello.Utente;

public class RepositoryMock extends RepositoryGenericoMock{

    private static final RepositoryMock singleton = new RepositoryMock();

    private RepositoryMock() {
        Utente u1 = new Utente("jbezos@amazon.com", "jeff");
        saveOrUpdate(u1);
        Utente u2 = new Utente("emusk@x.com", "twitter");
        saveOrUpdate(u2);
        DatiUtente du1 = new DatiUtente(1L, "Jeff", "jbezos@amazon.com", ERuolo.VENDOR);
        DatiUtente du2 = new DatiUtente(2L, "Elon", "emusk@x.com", ERuolo.USER);
        saveOrUpdate(du1);
        saveOrUpdate(du2);
    }

    public static RepositoryMock getInstance() {
        return singleton;
    }

}
