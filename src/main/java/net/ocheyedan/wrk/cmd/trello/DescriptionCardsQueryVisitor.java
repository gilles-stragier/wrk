package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.domain.cards.search.AllMyCards;
import net.ocheyedan.wrk.domain.cards.search.CardsOfABoard;
import net.ocheyedan.wrk.domain.cards.search.CardsOfAList;
import net.ocheyedan.wrk.domain.cards.search.CardsQueryVisitor;

public class DescriptionCardsQueryVisitor implements CardsQueryVisitor {
    private String description;

    @Override
    public void visit(AllMyCards allMyCards) {
        this.description = "Open cards assigned to you:";
    }

    @Override
    public void visit(CardsOfABoard cardsOfABoard) {
        this.description = String.format("Open cards for board ^b^%s^r^:", cardsOfABoard.getTrelloId());
    }

    @Override
    public void visit(CardsOfAList cardsOfAList) {
        this.description = String.format("Open cards for list ^b^%s^r^:", cardsOfAList.getTrelloId());
    }

    public String getDescription() {
        return description;
    }
}
