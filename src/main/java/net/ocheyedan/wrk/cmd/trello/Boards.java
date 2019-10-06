package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.ids.WrkIdsManager;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.Board;
import net.ocheyedan.wrk.trello.Trello;

import java.util.List;
import java.util.Map;

/**
 * User: blangel
 * Date: 6/29/12
 * Time: 9:24 PM
 */
public final class Boards extends IdCommand {

    private final String description;

    private final String url;

    public Boards(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
        if ((args.args.size() == 2) && "in".equals(args.args.get(0))) {
            TrelloId orgId = parseWrkId(args.args.get(1), orgPrefix);
            url = Trello.url("https://trello.com/1/organization/%s/boards?filter=open&key=%s&token=%s", orgId.id,
                    Trello.APP_DEV_KEY, Trello.USR_TOKEN);
            description = String.format("Open boards for organization ^b^%s^r^:", orgId.id);
        } else if (args.args.isEmpty()) {
            url = Trello.url("https://trello.com/1/members/my/boards?filter=open&key=%s&token=%s",
                    Trello.APP_DEV_KEY, Trello.USR_TOKEN);
            description = "Open boards you've created:";
        } else {
            url = description = null;
        }
    }

    @Override protected Map<String, String> _run() {
        Output.print(description);
        List<Board> boards = applicationContext.restTemplate.get(url, applicationContext.typeReferences.boardListType);

        WrkIdsManager idsManager = new WrkIdsManager();
        idsManager.registerTrelloIds(boards);
        applicationContext.defaultOutputter.printBoards(boards, idsManager);

        return idsManager.idsMap();
    }

    @Override protected boolean valid() {
        return (url != null);
    }

    @Override protected String getCommandName() {
        return "boards";
    }

}
