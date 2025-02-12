package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.Member;
import net.ocheyedan.wrk.trello.Trello;

import java.util.List;

/**
 * User: blangel
 * Date: 6/30/12
 * Time: 8:48 PM
 */
public final class UnAssign extends IdCommand {

    private final String url;

    private final String description;

    public UnAssign(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
        if (args.args.size() == 1) {
            LegacyTrelloId cardId = parseWrkId(args.args.get(0), cardsPrefix);
            url = Trello.url("https://trello.com/1/cards/%s/members/%s?key=%s&token=%s", cardId.id,
                    Trello.getUsrId(applicationContext), Trello.APP_DEV_KEY, Trello.USR_TOKEN);
            description = String.format("Un-assigning user from card ^b^%s^r^:", cardId.id);
        } else if ((args.args.size() == 3) && "from".equals(args.args.get(1))) {
            LegacyTrelloId cardId = parseWrkId(args.args.get(2), cardsPrefix);
            LegacyTrelloId memberId = parseWrkId(args.args.get(0), membersPrefix);
            url = Trello.url("https://trello.com/1/cards/%s/members/%s?key=%s&token=%s", cardId.id,
                    memberId.id, Trello.APP_DEV_KEY, Trello.USR_TOKEN);
            description = String.format("Un-assigning user ^b^%s^r^ from card ^b^%s^r^:", memberId.id, cardId.id);
        } else {
            url = description = null;
        }
    }

    @Override
    protected void _run() {
        Output.print(description);
        List<Member> members = applicationContext.restTemplate.delete(url, applicationContext.typeReferences.memberListType);
        if (members == null) {
            Output.print("  ^red^Not added or invalid user.^r^");
        } else {
            Output.print("  ^b^Un-assigned!^r^");
        }
    }

    @Override protected boolean valid() {
        return (url != null);
    }

    @Override protected String getCommandName() {
        return "unassign";
    }
}
