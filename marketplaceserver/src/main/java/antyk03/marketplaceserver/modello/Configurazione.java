package antyk03.marketplaceserver.modello;

import antyk03.marketplaceserver.enums.EStrategiaPersistenza;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configurazione {
    private static Configurazione singleton = new Configurazione();

    public static Configurazione getInstance() {
        return singleton;
    }

    private EStrategiaPersistenza strategiaDb = EStrategiaPersistenza.DB_MOCK;
    //private EStrategiaPersistenza strategiaDb = EstrategiaPersistenza.DB_HIBERNATE;
    private String jwtSecret;

    private Configurazione() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config/secrets.properties")) {
            if (input == null) {
                throw new RuntimeException("File secrets.properties non trovato!");
            }
            properties.load(input);
            this.jwtSecret = properties.getProperty("jwtSecret");
        } catch (IOException e) {
            throw new RuntimeException("Impossibile leggere secrets.properties", e);
        }
    }

    public static void setSingleton (Configurazione singleton) {
        Configurazione.singleton = singleton;
    }

    public EStrategiaPersistenza getStrategiaDb () {
        return strategiaDb;
    }

    public void setStrategiaDb (EStrategiaPersistenza strategiaDb) {
        this.strategiaDb = strategiaDb;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

}
