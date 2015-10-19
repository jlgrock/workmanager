package com.github.jlgrock.informatix.workmanager.config.converters
import javax.persistence.AttributeConverter
import javax.persistence.Converter
import java.time.LocalDate
/**
 *
 */
@Converter(autoApply = true)
class LocalDatePersistenceConverter implements AttributeConverter<LocalDate, java.sql.Date> {
    @Override
    java.sql.Date convertToDatabaseColumn(LocalDate date) {
        if (date == null) {
            return null
        }
        java.sql.Date.valueOf(date)
    }

    @Override
    LocalDate convertToEntityAttribute(java.sql.Date date) {
        if (date == null) {
            return null
        }
        date.toLocalDate()
    }
}
