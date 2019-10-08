package net.ocheyedan.wrk.ids;

import net.ocheyedan.wrk.trello.TrelloObject;

import java.util.Collection;

public interface IdGenerator {

    String generate (Collection<String> existingIds, TrelloObject object);

}
