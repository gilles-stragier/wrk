package net.ocheyedan.wrk.cmd.trello;

import com.fasterxml.jackson.core.type.TypeReference;
import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.Trello;

import java.util.Map;

/**
 * User: blangel
 * Date: 7/1/12
 * Time: 11:52 AM
 */
public final class Move extends IdCommand {

    private final String url;

    private final String description;

    public Move(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
        if ((args.args.size() == 3) && "to".equals(args.args.get(1))) {
            TrelloId cardId = parseWrkId(args.args.get(0), cardsPrefix);
            TrelloId listId = parseWrkId(args.args.get(2), listsPrefix);
            url = Trello.url("https://trello.com/1/cards/%s/idList?value=%s&key=%s&token=%s", cardId.id, listId.id,
                             Trello.APP_DEV_KEY, Trello.USR_TOKEN);
            description = String.format("Moving card ^b^%s^r^ to list ^b^%s^r^:", cardId.id, listId.id);
        } else {
            url = description = null;
        }
    }

    @Override
    protected void _run() {
        Output.print(description);
        Map<String, Object> result = applicationContext.restTemplate.put(url, new TypeReference<Map<String, Object>>() {
        });
        if (result == null) {
            Output.print("  ^red^Invalid id or insufficient privileges.^r^");
        } else {
            Output.print("  ^b^Moved!^r^", result);
        }
        ;
    }

    @Override protected boolean valid() {
        return (url != null);
    }

    @Override protected String getCommandName() {
        return "move";
    }
}
