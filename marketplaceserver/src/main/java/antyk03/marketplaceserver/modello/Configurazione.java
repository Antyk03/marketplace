package antyk03.marketplaceserver.modello;

import antyk03.marketplaceserver.enums.EStrategiaPersistenza;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class Configurazione {
    private static Configurazione singleton = new Configurazione();

    public static Configurazione getInstance() {
        return singleton;
    }

    private EStrategiaPersistenza strategiaDb = EStrategiaPersistenza.DB_MOCK;
    //private EStrategiaPersistenza strategiaDb = EStrategiaPersistenza.DB_HIBERNATE;
    private String jwtSecret;
    @Getter
    @Setter
    private SessionFactory sessionFactory;

    private Configurazione() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config/secrets.properties")) {
            if (input == null) {
                throw new RuntimeException("File secrets.properties non trovato!");
            }
            properties.load(input);
            this.jwtSecret = properties.getProperty("jwtSecret");

            String dbUrl = properties.getProperty("db.url");
            String dbUser = properties.getProperty("db.username");
            String dbPass = properties.getProperty("db.password");

            log.info("DB URL: " +dbUrl + "\nDB USER: " + dbUser + "\nDB PASSWORD: " + dbPass + "\n");

            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.setProperty("hibernate.connection.url", dbUrl);
            configuration.setProperty("hibernate.connection.username", dbUser);
            configuration.setProperty("hibernate.connection.password", dbPass);

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
