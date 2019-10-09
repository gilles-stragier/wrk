package net.ocheyedan.wrk.ids;

import net.ocheyedan.wrk.trello.TrelloObject;

import java.util.Collection;
import java.util.Set;

public class SequentialIdGenerator implements IdGenerator {

    @Override
    public IdMapping generate(Set<IdMapping> existingIds, TrelloObject object) {
        return IdMapping.initiliaze(
                object.trelloId(),
                new Alias("wrk" + (existingIds.size() + 1))
        );
    }


}
