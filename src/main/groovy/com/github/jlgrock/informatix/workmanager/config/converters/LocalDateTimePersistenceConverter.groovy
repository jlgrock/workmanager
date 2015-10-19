package com.github.jlgrock.informatix.workmanager.config.converters
import javax.persistence.AttributeConverter
import javax.persistence.Converter
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 *
 */
@Converter(autoApply = true)
class LocalDateTimePersistenceConverter implements AttributeConverter<LocalDateTime, Timestamp> {
    @Override
    Timestamp convertToDatabaseColumn(LocalDateTime date) {
        if (date == null) {
            return null
        }
        Instant instant = date.atZone(ZoneId.systemDefault()).toInstant()
        Timestamp.from(instant)
    }

    @Override
    LocalDateTime convertToEntityAttribute(Timestamp date) {
        if (date == null) {
            return null
        }
        LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
    }
}
