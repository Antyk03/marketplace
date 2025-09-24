package antyk03.marketplaceserver.modello;

import antyk03.marketplaceserver.enums.EStrategiaPersistenza;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class Configurazione {
    private static Configurazione singleton = new Configurazione();
    @Getter
    private final EntityManagerFactory emf;


    public static Configurazione getInstance() {
        return singleton;
    }

    //private EStrategiaPersistenza strategiaDb = EStrategiaPersistenza.DB_MOCK;
    private EStrategiaPersistenza strategiaDb = EStrategiaPersistenza.DB_HIBERNATE;
    @Getter
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

            Map<String, String> overrides = new HashMap<>();
            overrides.put("jakarta.persistence.jdbc.url", properties.getProperty("db.url"));
            overrides.put("jakarta.persistence.jdbc.user", properties.getProperty("db.username"));
            overrides.put("jakarta.persistence.jdbc.password", properties.getProperty("db.password"));

            this.emf = Persistence.createEntityManagerFactory("myPU", overrides);

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

}
