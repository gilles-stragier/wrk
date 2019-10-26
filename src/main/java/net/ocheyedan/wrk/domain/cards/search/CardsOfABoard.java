package net.ocheyedan.wrk.domain.cards.search;

import net.ocheyedan.wrk.cmd.trello.TrelloId;
import net.ocheyedan.wrk.trello.TrelloObject;

public class CardsOfABoard implements CardsQuery {

    private final TrelloId trelloId;

    public CardsOfABoard(TrelloId trelloId) {
        if (!trelloId.getType().equals(TrelloObject.Type.BOARD)) {
            throw new IllegalArgumentException("Only boards id are accepted here : " + trelloId);
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
