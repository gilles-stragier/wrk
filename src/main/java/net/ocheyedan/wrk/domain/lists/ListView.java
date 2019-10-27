package net.ocheyedan.wrk.domain.lists;

import net.ocheyedan.wrk.domain.View;

public class ListView implements View {

    private String name;
    private String id;
    private Integer pos;

    public ListView(String name, String id, Integer pos) {
        this.name = name;
        this.id = id;
        this.pos = pos;
    }

    private ListView(Builder builder) {
        name = builder.name;
        id = builder.id;
        pos = builder.pos;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Integer getPos() {
        return pos;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }


    public static final class Builder {
        private String name;
        private String id;
        private Integer pos;

        private Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder pos(Integer val) {
            pos = val;
            return this;
        }

        public ListView build() {
            return new ListView(this);
        }
    }
}
