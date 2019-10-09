package net.ocheyedan.wrk.ids;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.ocheyedan.wrk.cmd.trello.TrelloId;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

public class IdMapping {

    private final TrelloId trelloId;

    private final Set<Alias> aliases;

    public static IdMapping empty(TrelloId id) {
        return new IdMapping(id, Collections.emptySet());
    }

    public static IdMapping initiliaze(TrelloId id, Alias alias) {
        return new IdMapping(id, Collections.singleton(alias));
    }

    public static IdMapping enrich(IdMapping idMapping, Alias alias) {
        return new IdMapping(alias, idMapping);
    }

    @JsonCreator
    private IdMapping(
            @JsonProperty("trelloId") TrelloId trelloId,
            @JsonProperty("aliases") Set<Alias> aliases) {
        this.trelloId = trelloId;
        this.aliases = aliases;
    }


    private IdMapping(Alias alias, IdMapping idMapping) {

        Set<Alias> aliases = new HashSet<>();
        aliases.add(alias);
        aliases.addAll(idMapping.aliases);

        this.aliases = Collections.unmodifiableSet(aliases);
        this.trelloId = idMapping.trelloId;
    }

    public Set<Alias> getAliases() {
        return aliases;
    }

    public Alias oneAlias() {
        if (aliases.size() > 1) {
            throw new IllegalArgumentException("There is more than one alias");
        }
        return aliases.iterator().next();
    }

    public TrelloId getTrelloId() {
        return trelloId;
    }

    public Boolean contains(Alias alias) {
        return getAliases().stream().anyMatch(a -> a.equals(alias));
    }

    public Boolean contains(String alias) {
        return contains(new Alias(alias));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        IdMapping idMapping = (IdMapping) o;

        return new EqualsBuilder()
                .append(trelloId, idMapping.trelloId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(trelloId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("trelloId", trelloId)
                .append("aliases", aliases)
                .toString();
    }
}
