package net.ocheyedan.wrk.domain.cards.search;

public class AllMyCards implements CardsQuery {

    @Override
    public void accept(CardsQueryVisitor visitor) {
        visitor.visit(this);
    }
}
