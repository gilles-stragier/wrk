package net.ocheyedan.wrk.output;

import net.ocheyedan.wrk.cmd.trello.Cards;
import net.ocheyedan.wrk.cmd.trello.TrelloId;
import net.ocheyedan.wrk.domain.cards.CardView;
import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.trello.*;

public class DefaultOutputter implements Outputter {

    @Override
    public Boolean requiresNonNullNorEmpty(java.util.List<?> list) {
        if ((list == null) || list.isEmpty()) {
            Output.print("  ^black^None^r^");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void printList(IdsAliasingManager idsAliasingManager, List list) {
        String closed = ((list.getClosed() != null) && list.getClosed()) ? "^black^[closed] ^r^" : "^b^";
        Output.print("  %s%s^r^ ^black^| %s^r^ | %s", closed, list.getName(), idsAliasingManager.findByTrelloId(list).get(), list.getId());
    }

    @Override
    public void printLists(java.util.List<List> lists, IdsAliasingManager idsManager) {
        if (requiresNonNull(lists)) {
            lists.forEach(list -> printList(idsManager, list));
        }
    }

    @Override
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

    @Override
    public void describeMember(Member member) {
        if (requiresNonNull(member)) {
            Output.print("  ^b^%s^r^ ^black^| %s^r^", member.getFullName(), member.getId());
            Output.print("    ^black^username^r^ %s", member.getUsername());
        }
    }

    @Override
    public void printCard(IdsAliasingManager idsAliasingManager, Card card) {
        String labels = Cards.buildLabel(card.getLabels());
        String closed = ((card.getClosed() != null) && card.getClosed()) ? "^black^[closed] ^r^" : "^b^";
        String listId = idsAliasingManager.findByTrelloId(new TrelloId(card.getIdList(), TrelloObject.Type.LIST)).orElse(card.getIdList());
        Output.print("  %s%s^r^%s ^black^| %s^r^ | %s | l:%s", closed, card.getName(), labels, idsAliasingManager.findByTrelloId(card).get(), card.getId(), listId);
        Output.print("    ^black^%s^r^", Cards.getPrettyUrl(card));
    }


    @Override
    public void printCards(java.util.List<Card> cards, IdsAliasingManager idsManager) {
        if (requiresNonNull(cards)) {
            for (Card card : cards) {
                printCard(idsManager, card);
            }
        }
    }

    @Override
    public void printBoard(IdsAliasingManager idsAliasingManager, Board board) {
        if (requiresNonNull(board)) {
            String closed = ((board.getClosed() != null) && board.getClosed()) ? "^black^[closed] ^r^" : "^b^";
            Output.print("  %s%s^r^ ^black^| %s^r^ | %s", closed, board.getName(), idsAliasingManager.findByTrelloId(board).get(), board.getId());
            Output.print("    ^black^%s^r^", board.getUrl());
        }
    }

    @Override
    public Boolean requiresNonNull(Object required) {
        if ((required == null)) {
            Output.print("  ^black^None^r^");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void printBoards(java.util.List<Board> boards, IdsAliasingManager idsManager) {
        if (requiresNonNull(boards)) {
            for (Board board : boards) {
                printBoard(idsManager, board);
            }
        }
    }

    @Override
    public void printOrgs(java.util.List<Organization> organizations, IdsAliasingManager idsManager) {
        if (requiresNonNull(organizations)) {
            for (Organization organization : organizations) {
                printOrganization(idsManager, organization);
            }
        }
    }

    @Override
    public void printOrganization(IdsAliasingManager idsAliasingManager, Organization organization) {
        Output.print("  ^b^%s^r^ ^black^| %s^r^ | %s", organization.getDisplayName(), idsAliasingManager.findByTrelloId(organization).get(), organization.getId());
        Output.print("    ^black^%s^r^", organization.getUrl());

    }

    @Override
    public void printMembers(java.util.List<Member> members, IdsAliasingManager idsManager) {
        if (requiresNonNull(members)) {
            members.forEach(m -> printMember(idsManager, m));
        }
    }

    @Override
    public void printMember(IdsAliasingManager idsAliasingManager, Member member) {
        Output.print("  ^b^%s^r^ ^black^| %s^r^", member.getFullName(), idsAliasingManager.findByTrelloId(member).get());
        Output.print("    ^black^username^r^ %s", member.getUsername());
    }

    @Override
    public void describeList(List list) {
        if (requiresNonNull(list)) {
            String closed = ((list.getClosed() != null) && list.getClosed()) ? "^black^[closed] ^r^" : "^b^";
            Output.print("  %s%s^r^ ^black^| %s^r^", closed, list.getName(), list.getId());
        }

    }

    @Override
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

    @Override
    public void describeOrg(Organization org) {
        if (requiresNonNull(org)) {
            Output.print("  ^b^%s^r^ ^black^| %s^r^", org.getDisplayName(), org.getId());
            if (org.getDesc() != null && !org.getDesc().isEmpty()) {
                Output.print("    %s", org.getDesc());
            }
            Output.print("    ^black^%s^r^", org.getUrl());

        }
    }

    @Override
    public void printCardViews(java.util.List<CardView> cards, IdsAliasingManager idsManager) {
        cards.forEach(c -> {
            String labels = Cards.buildLabel(c.getLabels());
            String listId = idsManager.findByTrelloId(new TrelloId(c.getListView().getId(), TrelloObject.Type.LIST)).orElse(c.getListView().getId());
            Output.print("  %s^r^%s ^black^| %s^r^ | %s | l:%s", c.name(), labels, idsManager.findByTrelloId(c).get(), c.getId(), listId);
            Output.print("    ^black^%s^r^", c.getPrettyUrl());
        });

    }
}
