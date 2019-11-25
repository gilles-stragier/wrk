package net.ocheyedan.wrk.output;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.HorizontalAlign;
import net.ocheyedan.wrk.cmd.trello.TrelloId;
import net.ocheyedan.wrk.domain.cards.CardView;
import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.trello.Board;
import net.ocheyedan.wrk.trello.Card;
import net.ocheyedan.wrk.trello.Organization;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.freva.asciitable.AsciiTable.NO_BORDERS;
import static net.ocheyedan.wrk.trello.TrelloObject.Type.LIST;
import static net.ocheyedan.wrk.trello.TrelloObject.Type.ORG;

public class CompactOutputter extends DefaultOutputter {

    @Override
    public void printOrgs(List<Organization> organizations, IdsAliasingManager idsManager) {
        String table = AsciiTable.getTable(NO_BORDERS, organizations, Arrays.asList(
                new Column().header("id").with(o -> idsManager.findByTrelloId(o).get()),
                new Column().dataAlign(HorizontalAlign.LEFT).header("title").with(Organization::getName),
                new Column().dataAlign(HorizontalAlign.LEFT).header("desc").with(Organization::getDisplayName)
        ));

        Output.print(table);
    }

    @Override
    public void printBoards(List<Board> boards, IdsAliasingManager idsManager) {
        String table = AsciiTable.getTable(NO_BORDERS, boards, Arrays.asList(
                new Column().header("id").with(l -> idsManager.findByTrelloId(l).get()),
                new Column().dataAlign(HorizontalAlign.LEFT).header("title").with(Board::getName),
                new Column().dataAlign(HorizontalAlign.LEFT).header("desc").with(Board::getDesc),
                new Column().header("board").with(b -> idsManager.findByTrelloId(new TrelloId(b.getIdOrganization(), ORG)).orElse(b.getIdOrganization()))
        ));

        Output.print(table);
    }

    @Override
    public void printLists(List<net.ocheyedan.wrk.trello.List> lists, IdsAliasingManager idsManager) {
        String table = AsciiTable.getTable(NO_BORDERS, lists, Arrays.asList(
                new Column().header("id").with(l -> idsManager.findByTrelloId(l).get()),
                new Column().dataAlign(HorizontalAlign.LEFT).header("title").with(c -> c.getName())
        ));

        Output.print(table);
    }

    @Override
    public void printCards(List<Card> cards, IdsAliasingManager idsManager) {
        String table = AsciiTable.getTable(NO_BORDERS, cards, Arrays.asList(
                new Column().header("id").with(c -> idsManager.findByTrelloId(c).get()),
                new Column().dataAlign(HorizontalAlign.LEFT).header("title").with(c -> c.getName()),
                new Column().dataAlign(HorizontalAlign.LEFT).header("labels").with(c -> c.getLabels().stream().map(l -> l.getName()).collect(Collectors.joining(","))),
                new Column().header("list").with(c -> idsManager.findByTrelloId(new TrelloId(c.getIdList(), LIST)).orElse(c.getIdList()))
        ));

        Output.print(table);
    }

    @Override
    public void printCardViews(List<CardView> cards, IdsAliasingManager idsManager) {
        String table = AsciiTable.getTable(NO_BORDERS, cards, Arrays.asList(
                new Column().header("id").with(c -> idsManager.findByTrelloId(c).get()),
                new Column().dataAlign(HorizontalAlign.LEFT).header("title").with(c -> c.name()),
                new Column().dataAlign(HorizontalAlign.LEFT).header("labels").with(c -> c.getLabels().stream().map(l -> l.getName()).collect(Collectors.joining(","))),
                new Column().header("list").with(c -> {
                    String alias = idsManager.findByTrelloId(new TrelloId(c.getListView().getId(), LIST)).get();
                    return c.getListView().getName() + " (" + alias + ")";
                })
        ));

        Output.print(table);
    }

}
