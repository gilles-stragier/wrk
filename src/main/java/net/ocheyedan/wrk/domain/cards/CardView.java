package net.ocheyedan.wrk.domain.cards;

import net.ocheyedan.wrk.domain.View;
import net.ocheyedan.wrk.trello.Label;
import net.ocheyedan.wrk.trello.TrelloObject;

import java.util.List;

import static net.ocheyedan.wrk.trello.TrelloObject.Type.CARD;

public class CardView implements TrelloObject, View {

    public static class ListView implements View {
        private String id;
        private String name;
        private Integer pos;

        ListView(String id, String name, Integer pos) {
            this.id = id;
            this.name = name;
            this.pos = pos;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public Integer getPos() {
            return pos;
        }
    }

    private String id;
    private String name;
    private List<Label> labels;
    private ListView listView;
    private Integer pos;
    private String prettyUrl;

    private CardView(Builder builder) {
        id = builder.id;
        name = builder.name;
        labels = builder.labels;
        listView = builder.listView;
        pos = builder.pos;
        prettyUrl = builder.prettyUrl;
    }

    static Builder newBuilder() {
        return new Builder();
    }

    public ListView getListView() {
        return listView;
    }

    public String getPrettyUrl() {
        return prettyUrl;
    }

    @Override
    public Type type() {
        return CARD;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public Integer getPos() {
        return pos;
    }

    public static final class Builder {
        private String id;
        private String name;
        private List<Label> labels;
        private ListView listView;
        private Integer pos;
        private String prettyUrl;

        private Builder() {
        }


        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder labels(List<Label> val) {
            labels = val;
            return this;
        }

        public Builder listView(ListView val) {
            listView = val;
            return this;
        }

        public Builder pos(Integer val) {
            pos = val;
            return this;
        }

        public Builder prettyUrl(String val) {
            prettyUrl = val;
            return this;
        }

        public CardView build() {
            return new CardView(this);
        }
    }
}
