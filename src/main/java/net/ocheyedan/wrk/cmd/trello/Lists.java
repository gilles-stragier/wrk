package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.Trello;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: blangel
 * Date: 7/1/12
 * Time: 10:08 AM
 */
public final class Lists extends IdCommand {

    private final String url;

    private final String description;

    public Lists(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
        if ((args.args.size() == 2) && "in".equals(args.args.get(0))) {
            TrelloId boardId = parseWrkId(args.args.get(1), boardsPrefix);
            url = Trello.url("https://trello.com/1/boards/%s/lists?filter=open&key=%s&token=%s", boardId.id,
                    Trello.APP_DEV_KEY, Trello.USR_TOKEN);
            description = String.format("Open lists for board ^b^%s^r^:", boardId.id);
        } else {
            url = description = null;
        }
    }

    @Override protected Map<String, String> _run() {
        Output.print(description);
        List<net.ocheyedan.wrk.trello.List> lists = applicationContext.restTemplate.get(url, applicationContext.typeReferences.listsListType);
        if ((lists == null) || lists.isEmpty()) {
            Output.print("  ^black^None^r^");
            return Collections.emptyMap();
        }
        return printLists(lists, 1);
    }

    @Override protected boolean valid() {
        return (url != null);
    }

    @Override protected String getCommandName() {
        return "lists";
    }

    private Map<String, String> printLists(List<net.ocheyedan.wrk.trello.List> lists, int baseIndex) {
        Map<String, String> wrkIds = new HashMap<>(lists.size());
        for (net.ocheyedan.wrk.trello.List list : lists) {
            String wrkId = "wrk" + baseIndex++;
            wrkIds.put(wrkId, String.format("l:%s", list.getId()));

            applicationContext.defaultOutputter.printList(wrkId, list);
        }
        return wrkIds;
    }

}
