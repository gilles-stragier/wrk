package net.ocheyedan.wrk;

import java.util.List;

public class Collections {
    public static String asAString(List<String> args) {
        StringBuilder buffer = new StringBuilder();
        boolean first = true;
        for (String arg : args) {
            if (!first) {
                buffer.append(' ');
            }
            buffer.append(arg);
            first = false;
        }
        return buffer.toString();
    }
}
