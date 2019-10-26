package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.domain.cards.search.AllMyCards;
import net.ocheyedan.wrk.domain.cards.search.CardsOfABoard;
import net.ocheyedan.wrk.domain.cards.search.CardsOfAList;
import net.ocheyedan.wrk.domain.cards.search.CardsQueryVisitor;
import net.ocheyedan.wrk.trello.Trello;

public class UrlCardsQueryVisitor implements CardsQueryVisitor {

    private String url;

    @Override
    public void visit(AllMyCards allMyCards) {
        url = Trello.url("https://trello.com/1/members/my/cards?filter=open&key=%s&token=%s", Trello.APP_DEV_KEY,
                Trello.USR_TOKEN);
    }

    @Override
    public void visit(CardsOfABoard cardsOfABoard) {
        url = Trello.url("https://trello.com/1/boards/%s/cards?filter=open&key=%s&token=%s", cardsOfABoard.getTrelloId().getId(),
                Trello.APP_DEV_KEY, Trello.USR_TOKEN);
    }

    @Override
    public void visit(CardsOfAList cardsOfAList) {
        url = Trello.url("https://trello.com/1/lists/%s/cards?filter=open&key=%s&token=%s", cardsOfAList.getTrelloId().getId(),
                Trello.APP_DEV_KEY, Trello.USR_TOKEN);
    }

    public String getUrl() {
        return url;
    }
}
