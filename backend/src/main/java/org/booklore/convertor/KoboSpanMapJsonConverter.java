package org.booklore.convertor;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.booklore.model.dto.kobo.KoboSpanPositionMap;
import tools.jackson.core.JacksonException;

@Converter
public class KoboSpanMapJsonConverter implements AttributeConverter<KoboSpanPositionMap, String> {

    @Override
    public String convertToDatabaseColumn(KoboSpanPositionMap attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return ConverterObjectMapperHolder.mapper().writeValueAsString(attribute);
        } catch (JacksonException e) {
            throw new IllegalStateException("Failed to serialize Kobo span map to JSON", e);
        }
    }

    @Override
    public KoboSpanPositionMap convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return ConverterObjectMapperHolder.mapper().readValue(dbData, KoboSpanPositionMap.class);
        } catch (JacksonException e) {
            throw new IllegalStateException("Failed to deserialize Kobo span map from JSON", e);
        }
    }
}
