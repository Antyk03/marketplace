package antyk03.marketplaceserver.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
public class JWTUtil {

    private static final String SECRET_KEY = System.getProperty("jwtSecret");

    public static String generaToken(String email) {
        try {
            int dayToExpire = 1;
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTCreator.Builder builder = JWT.create().withIssuer("").withSubject(email);
            LocalDateTime l = LocalDateTime.now().plusDays(dayToExpire);
            ZonedDateTime z = l.atZone(ZoneId.systemDefault());
            builder.withExpiresAt(z.toInstant());
            return builder.sign(algorithm);
        } catch (Exception ex) {
            log.warn("Token non valido {}", ex.getMessage(), ex);
            throw new IllegalArgumentException("Token non valido " + ex);
        }
    }

    public static String verificaToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("").build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (Exception ex) {
            log.warn("Token non valido {}", ex.getMessage(), ex);
            throw new IllegalArgumentException("Token non valido " + ex);
        }
    }
}
