package org.booklore.convertor;

import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

/**
 * Bridges the Spring-managed primary {@link ObjectMapper} to JPA AttributeConverters.
 *
 * <p>Converters must be default-constructible for Spring/Hibernate AOT: build-time metamodel
 * generation instantiates them directly, without the container (see the AOT bootstrap-timing
 * shift). So they can't use constructor injection. Instead they fetch the same primary mapper
 * here, at call time, keeping the exact JSON format of persisted columns.
 *
 * <p>static holder over a hand-rolled static mapper — zero format drift, and fetching
 * at call time (not in a field) sidesteps any converter-vs-holder startup ordering.
 */
@Component
public class ConverterObjectMapperHolder {

    private static ObjectMapper mapper;

    public ConverterObjectMapperHolder(ObjectMapper mapper) {
        ConverterObjectMapperHolder.mapper = mapper;
    }

    public static ObjectMapper mapper() {
        return mapper;
    }
}
