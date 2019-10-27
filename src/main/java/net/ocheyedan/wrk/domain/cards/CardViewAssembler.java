package net.ocheyedan.wrk.domain.cards;

import net.ocheyedan.wrk.cmd.trello.Cards;
import net.ocheyedan.wrk.domain.Assembler;
import net.ocheyedan.wrk.domain.lists.FindListById;
import net.ocheyedan.wrk.domain.lists.ListView;
import net.ocheyedan.wrk.trello.Card;

public class CardViewAssembler implements Assembler<CardView, Card> {

    private final FindListById findById;

    public CardViewAssembler(FindListById findById) {
        this.findById = findById;
    }

    public CardView assemble(Card card) {

        ListView listView = findById.execute(card.getIdList());
        CardView.ListView list = new CardView.ListView(listView.getId(), listView.getName(), listView.getPos());

        return CardView.newBuilder()
                .labels(card.getLabels())
                .id(card.getId())
                .name(card.getName())
                .listView(
                        list
                )
                .prettyUrl(Cards.getPrettyUrl(card))
                .pos(card.getPos())
                .build();
    }

}
