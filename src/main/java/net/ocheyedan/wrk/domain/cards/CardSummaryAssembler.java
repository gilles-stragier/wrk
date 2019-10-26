package net.ocheyedan.wrk.domain.cards;

import net.ocheyedan.wrk.cmd.trello.Cards;
import net.ocheyedan.wrk.trello.Card;

public class CardSummaryAssembler {

    public CardView assemble(Card card) {

        CardView.ListView list = new CardView.ListView(card.getIdList(), "Fake list", 0);

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
