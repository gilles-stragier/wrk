package net.ocheyedan.wrk.ids;

import net.ocheyedan.wrk.cmd.trello.TrelloId;

import java.util.*;

public class IdMapping {

    private final TrelloId trelloId;

    private final Set<Alias> aliases;

    public static IdMapping initiliaze(TrelloId id, Alias alias) {
        return new IdMapping(id, alias);
    }

    public static IdMapping enrich(IdMapping idMapping, Alias alias) {
        return new IdMapping(alias, idMapping);
    }

    private IdMapping(TrelloId id, Alias alias) {
        this.trelloId = id;
        this.aliases = Collections.singleton(alias);
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

    public TrelloId getTrelloId() {
        return trelloId;
    }
}
