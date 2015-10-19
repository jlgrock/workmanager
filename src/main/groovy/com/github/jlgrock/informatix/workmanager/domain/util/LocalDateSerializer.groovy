package com.github.jlgrock.informatix.workmanager.domain.util
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

import java.time.LocalDate
import java.time.format.DateTimeFormatter
/**
 * Custom Jackson serializer for transforming a LocalDate object to JSON.
 */
class LocalDateSerializer extends JsonSerializer<LocalDate> {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @Override
    void serialize(LocalDate value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(value.format(formatter))
    }
}
