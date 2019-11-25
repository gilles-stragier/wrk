package net.ocheyedan.wrk.cmd.trello;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.TrelloObject;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.Optional;

import static net.ocheyedan.wrk.trello.TrelloObject.Type.arrayToString;

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

    String formatAsLegacy() {
        return getType().keyPrefix() + getId();
    }

    static TrelloId parseTrelloId(String id, IdsAliasingManager idsAliasingManager, TrelloObject.Type... acceptedTypes) {
        Optional<TrelloId> trelloOptional = idsAliasingManager.findByWrkId(id);
        if (!trelloOptional.isPresent()) {
            return new TrelloId(id, acceptedTypes.length == 1 ? acceptedTypes[0] : TrelloObject.Type.UNKNOWN);
        } else if (!Arrays.asList(acceptedTypes).contains(trelloOptional.get().type)) {
            Output.print("The wrk-id [ ^b^%s^r^ ] is for ^red^%s^r^ but the command is for %s.", id, trelloOptional.get().getType(), arrayToString(acceptedTypes));
            System.exit(1);
            return null;
        } else {
            return trelloOptional.get();
        }
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

}
