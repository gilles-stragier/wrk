package net.ocheyedan.wrk;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * User: blangel
 * Date: 6/30/12
 * Time: 9:34 AM
 */
public final class Json {

    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper mapper() {
        return mapper;
    }

    private Json() { }

}
