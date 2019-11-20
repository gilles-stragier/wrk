package net.ocheyedan.wrk.trello;

import net.ocheyedan.wrk.cmd.trello.TrelloId;

import java.util.Arrays;
import java.util.stream.Collectors;

public interface TrelloObject {

    default TrelloId trelloId() {
        return new TrelloId(getId(), type());
    }

    enum Type {
        CARD("c"),
        BOARD("b"),
        ORG("o"),
        MEMBER("m"),
        LIST("l"),
        UNKNOWN("?"),
        ;

        private String prefix;

        Type(String prefix) {
            this.prefix = prefix;
        }

        public String prefix() {
            return prefix;
        }

        public String keyPrefix() {
            return prefix() + ":";
        }

        public static String arrayToString(Type... types) {
            return Arrays.stream(types).map(t -> t.name()).collect(Collectors.joining());
        }
    }

    Type type();

    String getId();

    String name();

}
