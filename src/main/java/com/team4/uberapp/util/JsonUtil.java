package com.team4.uberapp.util;

import com.fasterxml.jackson.databind.*;
import java.io.*;

/**
 * Created by hectorguo on 2016/11/2.
 */
public class JsonUtil {
    public static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOEXception while mapping object to JSON");
        }
    }
}

