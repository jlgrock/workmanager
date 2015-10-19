package com.github.jlgrock.informatix.workmanager.web.rest
import org.springframework.http.MediaType

import java.nio.charset.Charset
/**
 * Utility class for testing REST controllers.
 */
public class TestUtil {

    /** MediaType for JSON UTF8 */
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    /**
     * Convert an object to JSON byte array.
     *
     * @param object
     *            the object to convert
     * @return the JSON byte array
     * @throws IOException
     */
    public static byte[] convertObjectToJsonBytes(Object object)
            throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        JodaModule module = new JodaModule();
//        module.addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer());
//        module.addSerializer(LocalDate.class, new asdCustomLocalDateSerializer());
//        mapper.registerModule(module);
//        return mapper.writeValueAsBytes(object);
        return null
    }

    /**
     * Create a byte array with a specific size filled with specified data
     *
     * @param size
     * @param data
     * @return
     */
    public static byte[] createByteArray(int size, String data) {
        byte[] byteArray = new byte[size];
        for (int i = 0; i < size; i++) {
            byteArray[i] = Byte.parseByte(data, 2);
        }
        return byteArray;
    }
}
