package com.github.jlgrock.informatix.workmanager.config.converters

import org.springframework.http.MediaType

import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 *
 */
@Converter(autoApply = true)
class MediaTypePersistenceConverter implements AttributeConverter<MediaType, String> {
    @Override
    String convertToDatabaseColumn(MediaType attribute) {
        if (attribute == null) {
            return "null"
        }
        attribute.toString()
    }

    @Override
    MediaType convertToEntityAttribute(String dbData) {
        if ("null".equalsIgnoreCase(dbData) || dbData.trim().isEmpty()) {
            return null
        }
        MediaType.valueOf(dbData)
    }
}
