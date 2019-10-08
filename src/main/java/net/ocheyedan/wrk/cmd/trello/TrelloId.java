package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.trello.TrelloObject;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class TrelloId {

    private final String id;
    private final TrelloObject.Type type;

    public TrelloId(String id, TrelloObject.Type type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TrelloId trelloId = (TrelloId) o;

        return new EqualsBuilder()
                .append(id, trelloId.id)
                .append(type, trelloId.type)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(type)
                .toHashCode();
    }
}
