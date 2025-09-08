package antyk03.marketplaceserver;

import antyk03.marketplaceserver.rest.RisorsaUtenti;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
@Slf4j
public class Applicazione extends Application {

    public Applicazione() {
        log.info("Applicazione creata...");
    }

}
