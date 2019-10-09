package net.ocheyedan.wrk.ids;

import net.ocheyedan.wrk.trello.TrelloObject;

import java.util.Set;

public class SequentiaByTypelIdGenerator implements IdGenerator {

    @Override
    public IdMapping generate(Set<IdMapping> existingIds, TrelloObject object) {
        return IdMapping.initiliaze(
                object.trelloId(),
                new Alias(
                        object.trelloId().getType().prefix()
                                + (countByType(existingIds, object.trelloId().getType()) + 1)
                )
        );
    }

    private long countByType(Set<IdMapping> existingIds, TrelloObject.Type type) {
        return existingIds.stream().filter(m -> m.getTrelloId().getType().equals(type)).count();
    }


}
