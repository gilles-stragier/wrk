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
 * Time: 1:58 PM
 */
public final class Labels extends IdCommand {

    private final String url;

    private final String description;

    public Labels(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
        if ((args.args.size() == 3) && "with".equals(args.args.get(1))) {
            LegacyTrelloId cardId = parseWrkId(args.args.get(0), cardsPrefix);
            String label = validate(args.args.get(2));
            url = Trello.url("https://trello.com/1/cards/%s/labels?value=%s&key=%s&token=%s", cardId.id,
                             label, Trello.APP_DEV_KEY, Trello.USR_TOKEN);
            description = String.format("Commenting on card ^b^%s^r^:", cardId.id);
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
        List<Label> result = applicationContext.restTemplate.post(url, applicationContext.typeReferences.labelListType);
        if (result == null) {
            Output.print("  ^red^Invalid id or insufficient privileges.^r^");
        } else {
            Output.print("  ^b^Labeled!^r^", result);
        }
    }

    @Override protected boolean valid() {
        return (url != null);
    }

    @Override protected String getCommandName() {
        return "label";
    }
}
