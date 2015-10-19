package com.github.jlgrock.informatix.workmanager.domain.util
import ch.qos.logback.classic.Level
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
/**
 * Custom Jackson serializer for transforming a Level object to JSON.
 */
class LevelDeserializer extends JsonDeserializer<Level> {

    @Override
    Level deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken()
        if (t == JsonToken.VALUE_STRING) {
            return Level.valueOf(jp.getText().trim())
        }
        throw ctxt.mappingException(handledType())
    }
}
