package net.ocheyedan.wrk.domain;

import net.ocheyedan.wrk.trello.TrelloObject;

public interface Assembler<T extends View, U extends TrelloObject> {
    T assemble(U trelloObject);
}
