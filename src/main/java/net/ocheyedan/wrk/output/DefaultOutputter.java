package net.ocheyedan.wrk.output;

import net.ocheyedan.wrk.cmd.trello.Cards;
import net.ocheyedan.wrk.ids.WrkIdsManager;
import net.ocheyedan.wrk.trello.*;

public class DefaultOutputter {

    private Boolean requiresNonNull(Object required) {
        if (required == null) {
            Output.print("^red^Invalid id or not found.^r^");
            return false;
        } else {
            return true;
        }
    }

    public Boolean requiresNonNullNorEmpty(java.util.List<?> list) {
        if ((list == null) || list.isEmpty()) {
            Output.print("  ^black^None^r^");
            return false;
        } else {
            return true;
        }
    }

    public void printList(String wrkId, List list) {
        String closed = ((list.getClosed() != null) && list.getClosed()) ? "^black^[closed] ^r^" : "^b^";
        Output.print("  %s%s^r^ ^black^| %s^r^ | %s", closed, list.getName(), wrkId, list.getId());
    }

    public void printLists(java.util.List<List> lists, WrkIdsManager idsManager) {
        if (requiresNonNull(lists)) {
            lists.forEach(list -> printList(idsManager.findByTrelloId(list).get(), list));
        }
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

    public void describeMember(Member member) {
        if (requiresNonNull(member)) {
            Output.print("  ^b^%s^r^ ^black^| %s^r^", member.getFullName(), member.getId());
            Output.print("    ^black^username^r^ %s", member.getUsername());
        }
    }

    public void printCard(String wrkId, Card card) {
        String labels = Cards.buildLabel(card.getLabels());
        String closed = ((card.getClosed() != null) && card.getClosed()) ? "^black^[closed] ^r^" : "^b^";
        Output.print("  %s%s^r^%s ^black^| %s^r^ | %s", closed, card.getName(), labels, wrkId, card.getId());
        Output.print("    ^black^%s^r^", Cards.getPrettyUrl(card));
    }


    public void printCards(java.util.List<Card> cards, WrkIdsManager idsManager) {
        if (requiresNonNull(cards)) {
            for (Card card : cards) {
                printCard(idsManager.findByTrelloId(card).get(), card);
            }
        }
    }

    public void printBoard(String wrkId, Board board) {
        if (requiresNonNull(board)) {
            String closed = ((board.getClosed() != null) && board.getClosed()) ? "^black^[closed] ^r^" : "^b^";
            Output.print("  %s%s^r^ ^black^| %s^r^ | %s", closed, board.getName(), wrkId, board.getId());
            Output.print("    ^black^%s^r^", board.getUrl());
        }
    }

    public void printBoards(java.util.List<Board> boards, WrkIdsManager idsManager) {
        if (requiresNonNull(boards)) {
            for (Board board : boards) {
                printBoard(idsManager.findByTrelloId(board).get(), board);
            }
        }
    }

    public void printOrgs(java.util.List<Organization> organizations, WrkIdsManager idsManager) {
        if (requiresNonNull(organizations)) {
            for (Organization organization : organizations) {
                printOrganization(idsManager.findByTrelloId(organization).get(), organization);
            }
        }
    }

    public void printOrganization(String wrkId, Organization organization) {
        Output.print("  ^b^%s^r^ ^black^| %s^r^ | %s", organization.getDisplayName(), wrkId, organization.getId());
        Output.print("    ^black^%s^r^", organization.getUrl());

    }

    public void printMembers(java.util.List<Member> members, WrkIdsManager idsManager) {
        if (requiresNonNull(members)) {
            members.forEach(m -> printMember(idsManager.findByTrelloId(m).get(), m));
        }
    }

    public void printMember(String wrkId, Member member) {
        Output.print("  ^b^%s^r^ ^black^| %s^r^", member.getFullName(), wrkId);
        Output.print("    ^black^username^r^ %s", member.getUsername());
    }

    public void describeList(List list) {
        if (requiresNonNull(list)) {
            String closed = ((list.getClosed() != null) && list.getClosed()) ? "^black^[closed] ^r^" : "^b^";
            Output.print("  %s%s^r^ ^black^| %s^r^", closed, list.getName(), list.getId());
        }

    }

    public void describeBoard(Board board) {
        if (requiresNonNull(board)) {
            String boardClosed = ((board.getClosed() != null) && board.getClosed()) ? "^black^[closed] ^r^" : "^b^";
            Output.print("  %s%s^r^ ^black^| %s^r^", boardClosed, board.getName(), board.getId());
            if (board.getDesc() != null && !board.getDesc().isEmpty()) {
                Output.print("    %s", board.getDesc());
            }
            Output.print("    ^black^%s^r^", board.getUrl());
        }

    }

    public void describeOrg(Organization org) {
        if (requiresNonNull(org)) {
            Output.print("  ^b^%s^r^ ^black^| %s^r^", org.getDisplayName(), org.getId());
            if (org.getDesc() != null && !org.getDesc().isEmpty()) {
                Output.print("    %s", org.getDesc());
            }
            Output.print("    ^black^%s^r^", org.getUrl());

        }
    }
}
