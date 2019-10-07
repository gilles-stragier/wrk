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
 * Time: 1:33 PM
 */
public final class Members extends IdCommand {

    private final String url;

    private final String description;

    public Members(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
        if ((args.args.size() == 2) && "in".equals(args.args.get(0))) {
            TrelloId id = parseWrkId(args.args.get(1), orgsBoardsCardsPrefix);
            if (id.idWithTypePrefix.startsWith("o:")) {
                String orgId = id.idWithTypePrefix.substring(2);
                url = Trello.url("https://trello.com/1/organizations/%s/members?key=%s&token=%s", orgId,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
                description = String.format("Members of organization ^b^%s^r^:", orgId);
            } else if (id.idWithTypePrefix.startsWith("b:")) {
                String boardId = id.idWithTypePrefix.substring(2);
                url = Trello.url("https://trello.com/1/boards/%s/members?key=%s&token=%s", boardId,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
                description = String.format("Members of board ^b^%s^r^:", boardId);
            } else if (id.idWithTypePrefix.startsWith("c:")) {
                String cardId = id.idWithTypePrefix.substring(2);
                url = Trello.url("https://trello.com/1/cards/%s/members?key=%s&token=%s", cardId,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
                description = String.format("Members of card ^b^%s^r^:", cardId);
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
        List<Member> members = applicationContext.restTemplate.get(url, applicationContext.typeReferences.memberListType);

        applicationContext.wrkIdsManager.registerTrelloIds(members);
        applicationContext.defaultOutputter.printMembers(members, applicationContext.wrkIdsManager);
    }

    @Override
    protected boolean valid() {
        return (url != null);
    }

    @Override
    protected String getCommandName() {
        return "members";
    }


}
