package net.ocheyedan.wrk.output;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.HorizontalAlign;
import net.ocheyedan.wrk.cmd.trello.TrelloId;
import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.trello.Card;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.ocheyedan.wrk.trello.TrelloObject.Type.LIST;

public class CompactOutputter extends DefaultOutputter {


    @Override
    public void printCards(List<Card> cards, IdsAliasingManager idsManager) {
        String table = AsciiTable.getTable(AsciiTable.NO_BORDERS, cards, Arrays.asList(
            new Column().header("id").with(c -> idsManager.findByTrelloId(c).get()),
            new Column().dataAlign(HorizontalAlign.LEFT).header("title").with(c -> c.getName()),
            new Column().dataAlign(HorizontalAlign.LEFT).header("labels").with(c -> c.getLabels().stream().map(l -> l.getName()).collect(Collectors.joining(","))),
            new Column().header("list").with(c -> idsManager.findByTrelloId(new TrelloId(c.getIdList(), LIST)).orElse(c.getIdList()))
        ));

        Output.print(table);
    }
}
