package net.ocheyedan.wrk.domain.cards.search;

import net.ocheyedan.wrk.cmd.trello.TrelloId;
import net.ocheyedan.wrk.trello.TrelloObject;

public class CardsOfAList implements CardsQuery {

    private final TrelloId trelloId;

    public CardsOfAList(TrelloId trelloId) {
        if (!trelloId.getType().equals(TrelloObject.Type.LIST)) {
            throw new IllegalArgumentException("Only lists id are accepted here : " + trelloId);
        }
        this.trelloId = trelloId;
    }

    public TrelloId getTrelloId() {
        return trelloId;
    }

    @Override
    public void accept(CardsQueryVisitor visitor) {
        visitor.visit(this);
    }
}
