package com.github.jlgrock.informatix.workmanager.domain.util
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

import java.time.LocalDate
/**
 * Custom Jackson serializer for transforming a LocalDate object to JSON.
 */
class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken()
        if (t == JsonToken.VALUE_STRING) {
            String str = jp.getText().trim()
            return LocalDate.parse(str)
        }
        if (t == JsonToken.VALUE_NUMBER_INT) {
            return LocalDate.ofEpochDay(jp.longValue)
        }
        throw ctxt.mappingException(handledType())
    }
}
