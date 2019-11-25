package net.ocheyedan.wrk.domain.cards;

import net.ocheyedan.wrk.cmd.trello.Cards;
import net.ocheyedan.wrk.domain.Assembler;
import net.ocheyedan.wrk.domain.lists.FindById;
import net.ocheyedan.wrk.trello.Card;
import net.ocheyedan.wrk.trello.List;

public class CardViewAssembler implements Assembler<CardView, Card> {

    private final FindById findById;

    public CardViewAssembler(FindById findById) {
        this.findById = findById;
    }


    public CardView assemble(Card card) {

        Assembler<CardView.ListView, List> assembler = t -> new CardView.ListView(t.getId(), t.getName(), t.getPos());

        CardView.ListView list = findById.execute(card.getIdList(), assembler).as(CardView.ListView.class);

        String dueDate = card.getBadges() != null ? card.getBadges().getDue() : "/";

        return CardView.newBuilder()
                .labels(card.getLabels())
                .id(card.getId())
                .name(card.getName())
                .listView(list)
                .prettyUrl(Cards.getPrettyUrl(card))
                .pos(card.getPos())
                .due(dueDate)
                .build();
    }

}
