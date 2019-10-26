package net.ocheyedan.wrk.domain.cards.search;

import net.ocheyedan.wrk.RestTemplate;
import net.ocheyedan.wrk.cmd.TypeReferences;
import net.ocheyedan.wrk.cmd.trello.UrlCardsQueryVisitor;
import net.ocheyedan.wrk.domain.cards.CardSummaryAssembler;
import net.ocheyedan.wrk.domain.cards.CardView;
import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.trello.Card;

import java.util.List;
import java.util.stream.Collectors;

public class SearchCards {

    private final RestTemplate restTemplate;
    private final TypeReferences typeReferences;
    private final IdsAliasingManager idsAliasingManager;
    private final CardSummaryAssembler cardSummaryAssembler;

    public SearchCards(
            RestTemplate restTemplate,
            TypeReferences typeReferences,
            IdsAliasingManager idsAliasingManager,
            CardSummaryAssembler cardSummaryAssembler
    ) {
        this.restTemplate = restTemplate;
        this.typeReferences = typeReferences;
        this.idsAliasingManager = idsAliasingManager;
        this.cardSummaryAssembler = cardSummaryAssembler;
    }

    public List<CardView> execute(CardsQuery cardsQuery) {
        UrlCardsQueryVisitor urlCardsQueryVisitor = new UrlCardsQueryVisitor();
        cardsQuery.accept(urlCardsQueryVisitor);

        List<Card> cards = restTemplate.get(urlCardsQueryVisitor.getUrl(), typeReferences.cardListType);
        idsAliasingManager.registerTrelloIds(cards);

        return cards.stream().map(c -> cardSummaryAssembler.assemble(c)).collect(Collectors.toList());
    }

}
