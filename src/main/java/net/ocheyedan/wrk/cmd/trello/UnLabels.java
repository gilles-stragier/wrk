package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.Label;
import net.ocheyedan.wrk.trello.Trello;

import java.util.List;

/**
 * User: blangel
 * Date: 7/1/12
 * Time: 2:03 PM
 */
public final class UnLabels extends IdCommand {

    private final String url;

    private final String description;

    public UnLabels(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
        if ((args.args.size() == 3) && "from".equals(args.args.get(1))) {
            LegacyTrelloId cardId = parseWrkId(args.args.get(2), cardsPrefix);
            String label = validate(args.args.get(0));
            url = Trello.url("https://trello.com/1/cards/%s/labels/%s?key=%s&token=%s", cardId.id,
                    label, Trello.APP_DEV_KEY, Trello.USR_TOKEN);
            description = String.format("Un-labeling %s from card ^b^%s^r^:", label, cardId.id);
        } else {
            url = description = null;
        }
    }

    private String validate(String label) {
        if ("green".equals(label) || "yellow".equals(label) || "orange".equals(label) || "red".equals(label) ||
                "purple".equals(label) || "blue".equals(label)) {
            return label;
        }
        Output.print("^red^Label must be one of green|yellow|orange|red|purple|blue.^r^");
        System.exit(1); return null;
    }

    @Override
    protected void _run() {
        Output.print(description);
        List<Label> result = applicationContext.restTemplate.delete(url, applicationContext.typeReferences.labelListType);

        if (result == null) {
            Output.print("  ^red^Invalid id or insufficient privileges.^r^");
        } else {
            Output.print("  ^b^Un-labeled!^r^", result);
        }
    }

    @Override protected boolean valid() {
        return (url != null);
    }

    @Override protected String getCommandName() {
        return "unlabel";
    }
}
