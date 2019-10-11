package net.ocheyedan.wrk.output;

import net.ocheyedan.wrk.Json;
import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.trello.Board;
import net.ocheyedan.wrk.trello.Card;
import net.ocheyedan.wrk.trello.Member;
import net.ocheyedan.wrk.trello.Organization;

import java.io.IOException;
import java.util.List;

public class JsonOutputter implements Outputter {

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
    public Boolean requiresNonNullNorEmpty(java.util.List<?> list) {
        if ((list == null) || list.isEmpty()) {
            Output.print("  ^black^None^r^");
            return false;
        } else {
            return true;
        }
    }

    private void serializeToOutput(Object obj) {
        try {
            Json.mapper().writerWithDefaultPrettyPrinter().writeValue(System.out, obj);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to serialize to json", e);
        }
    }

    @Override
    public void printList(IdsAliasingManager idsAliasingManager, net.ocheyedan.wrk.trello.List list) {
        serializeToOutput(list);
    }

    @Override
    public void printLists(List<net.ocheyedan.wrk.trello.List> lists, IdsAliasingManager idsManager) {
        serializeToOutput(lists);
    }

    @Override
    public void describeCard(Card card) {
        serializeToOutput(card);
    }

    @Override
    public void describeMember(Member member) {
        serializeToOutput(member);
    }

    @Override
    public void printCard(IdsAliasingManager idsAliasingManager, Card card) {
        serializeToOutput(card);
    }

    @Override
    public void printCards(List<Card> cards, IdsAliasingManager idsManager) {
        serializeToOutput(cards);
    }

    @Override
    public void printBoard(IdsAliasingManager idsAliasingManager, Board board) {
        serializeToOutput(board);
    }

    @Override
    public void printBoards(List<Board> boards, IdsAliasingManager idsManager) {
        serializeToOutput(boards);
    }

    @Override
    public void printOrgs(List<Organization> organizations, IdsAliasingManager idsManager) {
        serializeToOutput(organizations);
    }

    @Override
    public void printOrganization(IdsAliasingManager idsAliasingManager, Organization organization) {
        serializeToOutput(organization);
    }

    @Override
    public void printMembers(List<Member> members, IdsAliasingManager idsManager) {
        serializeToOutput(members);
    }

    @Override
    public void printMember(IdsAliasingManager idsAliasingManager, Member member) {
        serializeToOutput(member);
    }

    @Override
    public void describeList(net.ocheyedan.wrk.trello.List list) {
        serializeToOutput(list);
    }

    @Override
    public void describeBoard(Board board) {
        serializeToOutput(board);
    }

    @Override
    public void describeOrg(Organization org) {
        serializeToOutput(org);
    }
}
