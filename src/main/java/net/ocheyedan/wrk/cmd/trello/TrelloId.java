package net.ocheyedan.wrk.cmd.trello;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.ocheyedan.wrk.trello.TrelloObject;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TrelloId {

    private final String id;
    private final TrelloObject.Type type;

    @JsonCreator
    public TrelloId(
            @JsonProperty("id") String id,
            @JsonProperty("type") TrelloObject.Type type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public TrelloObject.Type getType() {
        return type;
    }

    public String formatAsLegacy() {
        return getType().keyPrefix() + getId();
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("type", type)
                .toString();
    }

    public static TrelloId fromLegacyString(String wrkId) {
        if (wrkId.startsWith(TrelloObject.Type.BOARD.keyPrefix())) {
            return new TrelloId(wrkId.substring(2), TrelloObject.Type.BOARD);
        } else if (wrkId.startsWith(TrelloObject.Type.CARD.keyPrefix())) {
            return new TrelloId(wrkId.substring(2), TrelloObject.Type.CARD);
        } else if (wrkId.startsWith(TrelloObject.Type.LIST.keyPrefix())) {
            return new TrelloId(wrkId.substring(2), TrelloObject.Type.LIST);
        } else if (wrkId.startsWith(TrelloObject.Type.ORG.keyPrefix())) {
            return new TrelloId(wrkId.substring(2), TrelloObject.Type.ORG);
        } else if (wrkId.startsWith(TrelloObject.Type.MEMBER.keyPrefix())) {
            return new TrelloId(wrkId.substring(2), TrelloObject.Type.MEMBER);
        } else {
            throw new IllegalArgumentException("Unknox prefix for id " + wrkId);
        }
    }
}
