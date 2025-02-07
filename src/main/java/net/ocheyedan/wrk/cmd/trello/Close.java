package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.Trello;

import java.util.Map;

/**
 * User: blangel
 * Date: 6/30/12
 * Time: 9:07 PM
 */
public final class Close extends IdCommand {

    private final String url;

    private final String description;

    public Close(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
        if (args.args.size() == 1) {
            LegacyTrelloId id = parseWrkId(args.args.get(0), boardsListsCardsPrefix);
            if (id.idWithTypePrefix.startsWith("b:")) {
                String boardId = id.idWithTypePrefix.substring(2);
                url = Trello.url("https://trello.com/1/boards/%s/closed?value=true&key=%s&token=%s", boardId,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
                description = String.format("Closing board ^b^%s^r^:", boardId);
            } else if (id.idWithTypePrefix.startsWith("l:")) {
                String listId = id.idWithTypePrefix.substring(2);
                url = Trello.url("https://trello.com/1/lists/%s/closed?value=true&key=%s&token=%s", listId,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
                description = String.format("Closing list ^b^%s^r^:", listId);
            } else if (id.idWithTypePrefix.startsWith("c:")) {
                String cardId = id.idWithTypePrefix.substring(2);
                url = Trello.url("https://trello.com/1/cards/%s/closed?value=true&key=%s&token=%s", cardId,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
                description = String.format("Closing card ^b^%s^r^:", cardId);
            } else {
                url = description = null;
            }
        } else {
            url = description = null;
        }
    }

    @Override
    protected void _run() {
        Output.print(description);
        Map<String, Object> result = applicationContext.restTemplate.put(url, applicationContext.typeReferences.mapOfObjectsType);
        if (result == null) {
            Output.print("  ^red^Invalid id or insufficient privileges.^r^");
        } else {
            Output.print("  ^b^Closed!^r^", result);
        }
    }

    @Override protected boolean valid() {
        return (url != null);
    }

    @Override protected String getCommandName() {
        return "close";
    }
}
