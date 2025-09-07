package antyk03.marketplaceserver.filtri;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.bytebuddy.asm.Advice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Provider
@Slf4j
public class LocalDateTimeParamConverter implements ParamConverterProvider {

    private static final String PATTERN = "yyyy-MM-dd";

    @Override
    @SuppressWarnings("unchecked")
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType.equals(LocalDateTime.class)) {
            return (ParamConverter<T>) new LocalDateTimeConverter();
        }
        return null;
    }

    class LocalDateTimeConverter implements ParamConverter<LocalDateTime> {
        @Override
        public LocalDateTime fromString(String value) {
            log.trace("Richiesta la conversione in LocalDateTime del valore String {}", value);
            if (value == null) {
                return null;
            }
            LocalDateTime localDateTime = LocalDateTime.parse(value, DateTimeFormatter.ofPattern(PATTERN));
            return  localDateTime;
        }

        @Override
        public String toString(LocalDateTime value) {
            log.trace("Richiesta la conversione in String del LocalDateTime", value);
            if (value == null) {
                return null;
            }
            return value.format(DateTimeFormatter.ofPattern(PATTERN));
        }
    }

}
