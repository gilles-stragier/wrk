package net.ocheyedan.wrk.output;

import net.ocheyedan.wrk.cmd.trello.Cards;
import net.ocheyedan.wrk.trello.Board;
import net.ocheyedan.wrk.trello.Card;
import net.ocheyedan.wrk.trello.List;

public class DefaultOutputter {

    public Boolean requiresNonNull(Object required) {
        if (required == null) {
            Output.print("^red^Invalid id or not found.^r^");
            return false;
        } else {
            return true;
        }
    }

    public void printList(String wrkId, List list) {
        String closed = ((list.getClosed() != null) && list.getClosed()) ? "^black^[closed] ^r^" : "^b^";
        Output.print("  %s%s^r^ ^black^| %s^r^ | %s", closed, list.getName(), wrkId, list.getId());
    }

    public void describeCard(Card card) {
        if (requiresNonNull(card)) {
            String labels = Cards.buildLabel(card.getLabels());
            String cardClosed = ((card.getClosed() != null) && card.getClosed()) ? "^black^[closed] ^r^" : "^b^";
            Output.print("  %s%s^r^%s ^black^| %s^r^", cardClosed, card.getName(), labels, card.getId());
            if (card.getDesc() != null && !card.getDesc().isEmpty()) {
                Output.print("    %s", card.getDesc());
            }
            Output.print("    ^black^%s^r^", Cards.getPrettyUrl(card));
        }
    }

    public void printCard(String wrkId, Card card) {
        String labels = Cards.buildLabel(card.getLabels());
        String closed = ((card.getClosed() != null) && card.getClosed()) ? "^black^[closed] ^r^" : "^b^";
        Output.print("  %s%s^r^%s ^black^| %s^r^ | %s", closed, card.getName(), labels, wrkId, card.getId());
        Output.print("    ^black^%s^r^", Cards.getPrettyUrl(card));
    }

    public void printBoard(String wrkId, Board board) {
        String closed = ((board.getClosed() != null) && board.getClosed()) ? "^black^[closed] ^r^" : "^b^";
        Output.print("  %s%s^r^ ^black^| %s^r^ | %s", closed, board.getName(), wrkId, board.getId());
        Output.print("    ^black^%s^r^", board.getUrl());
    }

}
