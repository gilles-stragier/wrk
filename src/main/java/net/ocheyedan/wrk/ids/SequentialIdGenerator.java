package net.ocheyedan.wrk.ids;

import net.ocheyedan.wrk.trello.TrelloObject;

import java.util.Collection;

public class SequentialIdGenerator implements IdGenerator {

    @Override
    public String generate(Collection<String> existingIds, TrelloObject object) {
        return "wrk" + (existingIds.size() + 1);
    }
}
