package net.ocheyedan.wrk.output;

import net.ocheyedan.wrk.cmd.trello.Cards;
import net.ocheyedan.wrk.ids.WrkIdsManager;
import net.ocheyedan.wrk.trello.*;

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


    public void printCards(java.util.List<Card> cards, WrkIdsManager idsManager) {
        for (Card card : cards) {
            printCard(idsManager.invertedIdsMap().get("c:" + card.getId()), card);
        }
    }

    public void printBoard(String wrkId, Board board) {
        String closed = ((board.getClosed() != null) && board.getClosed()) ? "^black^[closed] ^r^" : "^b^";
        Output.print("  %s%s^r^ ^black^| %s^r^ | %s", closed, board.getName(), wrkId, board.getId());
        Output.print("    ^black^%s^r^", board.getUrl());
    }

    public void printBoards(java.util.List<Board> boards, WrkIdsManager idsManager) {
        for (Board board : boards) {
            printBoard(idsManager.invertedIdsMap().get("b:" + board.getId()), board);
        }
    }

    public void printOrgs(java.util.List<Organization> organizations, WrkIdsManager idsManager) {
        for (Organization organization : organizations) {
            printOrganization(idsManager.invertedIdsMap().get("o:" + organization.getId()), organization);
        }
    }

    public void printOrganization(String wrkId, Organization organization) {
        Output.print("  ^b^%s^r^ ^black^| %s^r^ | %s", organization.getDisplayName(), wrkId, organization.getId());
        Output.print("    ^black^%s^r^", organization.getUrl());

    }

    public void printMembers(java.util.List<Member> members, WrkIdsManager idsManager) {
        members.stream().forEach(m -> printMember(idsManager.invertedIdsMap().get("m:" + m.getId()), m));
    }

    public void printMember(String wrkId, Member member) {
        Output.print("  ^b^%s^r^ ^black^| %s^r^", member.getFullName(), wrkId);
        Output.print("    ^black^username^r^ %s", member.getUsername());
    }

}
