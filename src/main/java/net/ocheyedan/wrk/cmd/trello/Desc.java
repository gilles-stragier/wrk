package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.*;

/**
 * User: blangel
 * Date: 7/1/12
 * Time: 7:57 AM
 */
public final class Desc extends IdCommand {

    private enum Type {
        Org, Board, List, Card, Member
    }

    private final String url;

    private final String description;

    private final Type type;

    public Desc(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
        if (args.args.size() == 1) {
            LegacyTrelloId id = parseWrkId(args.args.get(0), allPrefix);
            if (id.idWithTypePrefix.startsWith("o:")) {
                String orgId = id.idWithTypePrefix.substring(2);
                url = Trello.url("https://trello.com/1/organizations/%s?key=%s&token=%s", orgId,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
                description = String.format("Description of organization ^b^%s^r^:", orgId);
                type = Type.Org;
            } else if (id.idWithTypePrefix.startsWith("b:")) {
                String boardId = id.idWithTypePrefix.substring(2);
                url = Trello.url("https://trello.com/1/boards/%s?key=%s&token=%s", boardId,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
                description = String.format("Description of board ^b^%s^r^:", boardId);
                type = Type.Board;
            } else if (id.idWithTypePrefix.startsWith("l:")) {
                String listId = id.idWithTypePrefix.substring(2);
                url = Trello.url("https://trello.com/1/lists/%s?key=%s&token=%s", listId,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
                description = String.format("Description of list ^b^%s^r^:", listId);
                type = Type.List;
            } else if (id.idWithTypePrefix.startsWith("c:")) {
                String cardId = id.idWithTypePrefix.substring(2);
                url = Trello.url("https://trello.com/1/cards/%s?key=%s&token=%s", cardId,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
                description = String.format("Description of card ^b^%s^r^:", cardId);
                type = Type.Card;
            } else if (id.idWithTypePrefix.startsWith("m:")) {
                String memberId = id.idWithTypePrefix.substring(2);
                url = Trello.url("https://trello.com/1/members/%s?key=%s&token=%s", memberId,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
                description = String.format("Description of member ^b^%s^r^:", memberId);
                type = Type.Member;
            } else {
                url = description = null;
                type = null;
            }
        } else {
            url = description = null;
            type = null;
        }
    }

    @Override
    protected void _run() {
        Output.print(description);

        switch (type) {
            case Org:
                Organization org = applicationContext.restTemplate.get(url, applicationContext.typeReferences.orgType);
                applicationContext.outputter.describeOrg(org);
                break;
            case Board:
                Board board = applicationContext.restTemplate.get(url, applicationContext.typeReferences.boardType);
                applicationContext.outputter.describeBoard(board);
                break;
            case List:
                net.ocheyedan.wrk.trello.List list = applicationContext.restTemplate.get(url, applicationContext.typeReferences.listType);
                applicationContext.outputter.describeList(list);
                break;
            case Card:
                Card card = applicationContext.restTemplate.get(url, applicationContext.typeReferences.cardType);
                applicationContext.outputter.describeCard(card);
                break;
            case Member:
                Member member = applicationContext.restTemplate.get(url, applicationContext.typeReferences.memberType);
                applicationContext.outputter.describeMember(member);
                break;
        }
    }

    @Override protected boolean valid() {
        return (url != null);
    }

    @Override protected String getCommandName() {
        return "desc";
    }
}
