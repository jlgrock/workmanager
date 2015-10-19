package com.github.jlgrock.informatix.workmanager.domain.util
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 *
 */
class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Override
    void serialize(LocalDateTime value, JsonGenerator jgen, SerializerProvider arg2) throws IOException, JsonProcessingException {
        if (value == null) {
            jgen.writeNull()
        }
        jgen.writeString(value.format(formatter))
    }
}
