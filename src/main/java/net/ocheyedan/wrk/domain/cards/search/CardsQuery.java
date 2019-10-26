package net.ocheyedan.wrk.domain.cards.search;

public interface CardsQuery {

    void accept(CardsQueryVisitor visitor);

}
