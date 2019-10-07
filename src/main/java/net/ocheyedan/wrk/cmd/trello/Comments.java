package net.ocheyedan.wrk.cmd.trello;

import com.fasterxml.jackson.core.type.TypeReference;
import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.Action;
import net.ocheyedan.wrk.trello.Trello;

import java.util.Collections;
import java.util.List;

/**
 * User: blangel
 * Date: 6/30/12
 * Time: 10:00 AM
 */
public final class Comments extends IdCommand {

    private final String url;

    private final String description;

    public Comments(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
        if ((args.args.size() == 2) && "in".equals(args.args.get(0))) {
            TrelloId cardId = parseWrkId(args.args.get(1), cardsPrefix);
            url = Trello.url("https://trello.com/1/cards/%s/actions?filter=commentCard&key=%s&token=%s", cardId.id,
                    Trello.APP_DEV_KEY, Trello.USR_TOKEN);
            description = String.format("Comments for card ^b^%s^r^:", cardId.id);
        } else {
            url = description = null;
        }
    }

    @Override
    protected void _run() {
        Output.print(description);
        List<Action> comments = applicationContext.restTemplate.get(url, new TypeReference<List<Action>>() { });
        if ((comments == null) || comments.isEmpty()) {
            Output.print("  ^black^None^r^");
            return;
        }
        Collections.reverse(comments); // reverse order of comments so most recent appear on bottom
        String color = "white";
        for (Action comment : comments) {
            Output.print("^%s^|^r^%n^%s^|^r^ %s", color, color, comment.getMemberCreator().getFullName());
            Output.print("^%s^|^r^ ^black^%s^r^%n^%s^|^r^", color, comment.getDate(), color);
            String text = comment.getData().getText();
            String[] splits = text.split("\n");
            for (String split : splits) {
                Output.print("^%s^|^r^ ^b^%s^r^", color, split);
            }
            Output.print("^%s^|^r^%n", color);
            if ("white".equals(color)) {
                color = "black";
            } else {
                color = "white";
            }
        }
    }

    @Override protected boolean valid() {
        return (url != null);
    }

    @Override protected String getCommandName() {
        return "comments";
    }
}
