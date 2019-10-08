package net.ocheyedan.wrk.trello;

public interface TrelloObject {

    enum Type {
        CARD("c"),
        BOARD("b"),
        ORG("o"),
        MEMBER("m"),
        LIST("l");

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
    }

    Type type();

    String getId();

    String name();

}
