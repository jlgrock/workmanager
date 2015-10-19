package com.github.jlgrock.informatix.workmanager.domain.util
import ch.qos.logback.classic.Level
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
/**
 * Custom Jackson serializer for transforming a LocalDate object to JSON.
 */
class LevelSerializer extends JsonSerializer<Level> {

    @Override
    void serialize(Level value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(value.toString())
    }
}
