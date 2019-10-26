package net.ocheyedan.wrk.domain.cards.search;

import net.ocheyedan.wrk.RestTemplate;
import net.ocheyedan.wrk.cmd.TypeReferences;
import net.ocheyedan.wrk.cmd.trello.UrlCardsQueryVisitor;
import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.trello.Card;

import java.util.List;

public class SearchCards {

    private final RestTemplate restTemplate;
    private final TypeReferences typeReferences;
    private final IdsAliasingManager idsAliasingManager;

    public SearchCards(RestTemplate restTemplate, TypeReferences typeReferences, IdsAliasingManager idsAliasingManager) {
        this.restTemplate = restTemplate;
        this.typeReferences = typeReferences;
        this.idsAliasingManager = idsAliasingManager;
    }

    public List<Card> execute(CardsQuery cardsQuery) {
        UrlCardsQueryVisitor urlCardsQueryVisitor = new UrlCardsQueryVisitor();
        cardsQuery.accept(urlCardsQueryVisitor);

        List<Card> cards = restTemplate.get(urlCardsQueryVisitor.getUrl(), typeReferences.cardListType);
        idsAliasingManager.registerTrelloIds(cards);

        return cards;
    }

}
