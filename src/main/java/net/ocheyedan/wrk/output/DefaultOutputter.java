package net.ocheyedan.wrk.output;

import net.ocheyedan.wrk.trello.List;

public class DefaultOutputter {

    public void printList(String wrkId, List list) {
        String closed = ((list.getClosed() != null) && list.getClosed()) ? "^black^[closed] ^r^" : "^b^";
        Output.print("  %s%s^r^ ^black^| %s^r^ | %s", closed, list.getName(), wrkId, list.getId());
    }

}
