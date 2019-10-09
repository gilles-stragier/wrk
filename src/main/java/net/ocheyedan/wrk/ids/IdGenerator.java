package net.ocheyedan.wrk.ids;

import net.ocheyedan.wrk.trello.TrelloObject;

import java.util.Collection;
import java.util.Set;

public interface IdGenerator {

    IdMapping generate (Set<IdMapping> existingIds, TrelloObject object);

}
