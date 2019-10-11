package net.ocheyedan.wrk.output;

import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.trello.*;

public interface Outputter {
    default Boolean requiresNonNull(Object required) {
        if (required == null) {
            Output.print("^red^Invalid id or not found.^r^");
            return false;
        } else {
            return true;
        }
    }

    Boolean requiresNonNullNorEmpty(java.util.List<?> list);

    void printList(IdsAliasingManager idsAliasingManager, List list);

    void printLists(java.util.List<List> lists, IdsAliasingManager idsManager);

    void describeCard(Card card);

    void describeMember(Member member);

    void printCard(IdsAliasingManager idsAliasingManager,  Card card);

    void printCards(java.util.List<Card> cards, IdsAliasingManager idsManager);

    void printBoard(IdsAliasingManager idsAliasingManager,  Board board);

    void printBoards(java.util.List<Board> boards, IdsAliasingManager idsManager);

    void printOrgs(java.util.List<Organization> organizations, IdsAliasingManager idsManager);

    void printOrganization(IdsAliasingManager idsAliasingManager, Organization organization);

    void printMembers(java.util.List<Member> members, IdsAliasingManager idsManager);

    void printMember(IdsAliasingManager idsAliasingManager,  Member member);

    void describeList(List list);

    void describeBoard(Board board);

    void describeOrg(Organization org);
}
