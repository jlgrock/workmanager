package com.github.jlgrock.informatix.workmanager.domain.util
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

import java.time.Instant
import java.time.LocalDateTime
/**
 *
 */
class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_STRING) {
            String str = jp.getText().trim();
            return LocalDateTime.parse(str);
        }
        if (t == JsonToken.VALUE_NUMBER_INT) {
            return LocalDateTime.from(Instant.ofEpochSecond(jp.getLongValue()))
        }
        throw ctxt.mappingException(handledType());
    }
}

