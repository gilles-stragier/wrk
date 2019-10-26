package net.ocheyedan.wrk.domain.cards.search;

public interface CardsQueryVisitor {

    void visit(AllMyCards allMyCards);

    void visit(CardsOfABoard cardsOfABoard);

    void visit(CardsOfAList cardsOfAList);

}
