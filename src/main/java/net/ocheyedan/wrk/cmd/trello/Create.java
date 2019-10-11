package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.Board;
import net.ocheyedan.wrk.trello.Card;
import net.ocheyedan.wrk.trello.Trello;

import java.util.Collections;

/**
 * User: blangel
 * Date: 7/1/12
 * Time: 1:18 PM
 */
public final class Create extends IdCommand {

    private enum Type {
        Board, List, Card
    }

    private final String url;

    private final String description;

    private final Type type;

    public Create(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
        if (((args.args.size() == 4) || (args.args.size() == 5))
                && "board".equals(args.args.get(0)) && "in".equals(args.args.get(1))) {
            LegacyTrelloId orgId = parseWrkId(args.args.get(2), orgPrefix);
            String name = validate(encode(args.args.get(3)), "Board name", "board names");
            String desc = (args.args.size() == 5 ? "&desc=" + validate(encode(args.args.get(4)), "Board desc", "board descriptions") : "");
            url = Trello.url("https://trello.com/1/boards?name=%s&idOrganization=%s%s&key=%s&token=%s", name, orgId.id,
                             desc, Trello.APP_DEV_KEY, Trello.USR_TOKEN);
            description = String.format("Creating board in organization ^b^%s^r^:", orgId.id);
            type = Type.Board;
        } else if ((args.args.size() == 4) && "list".equals(args.args.get(0)) && "in".equals(args.args.get(1))) {
            LegacyTrelloId boardId = parseWrkId(args.args.get(2), boardsPrefix);
            String name = validate(encode(args.args.get(3)), "List name", "list names");
            url = Trello.url("https://trello.com/1/lists?name=%s&idBoard=%s&key=%s&token=%s", name, boardId.id,
                             Trello.APP_DEV_KEY, Trello.USR_TOKEN);
            description = String.format("Creating list in board ^b^%s^r^:", boardId.id);
            type = Type.List;
        } else if (((args.args.size() == 4) || (args.args.size() == 5))
                && "card".equals(args.args.get(0)) && "in".equals(args.args.get(1))) {
            LegacyTrelloId listId = parseWrkId(args.args.get(2), listsPrefix);
            String name = validate(encode(args.args.get(3)), "Card name", "card names");
            String desc = (args.args.size() == 5 ? "&desc=" + validate(encode(args.args.get(4)), "Card desc",
                    "card descriptions") : "");
            url = Trello.url("https://trello.com/1/cards?name=%s&idList=%s%s&key=%s&token=%s", name, listId.id,
                    desc, Trello.APP_DEV_KEY, Trello.USR_TOKEN);
            description = String.format("Creating card in list ^b^%s^r^:", listId.id);
            type = Type.Card;
        } else {
            url = description = null;
            type = null;
        }
    }

    @Override
    protected void _run() {
        Output.print(description);
        String wrkId = "wrk1";
        switch (type) {
            case Board:
                Board board = applicationContext.restTemplate.post(url, applicationContext.typeReferences.boardType);
                applicationContext.wrkIdsManager.registerTrelloIds(Collections.singletonList(board));
                applicationContext.outputter.printBoard(applicationContext.wrkIdsManager, board);
                break;
            case List:
                net.ocheyedan.wrk.trello.List list = applicationContext.restTemplate.post(url, applicationContext.typeReferences.listType);
                applicationContext.wrkIdsManager.registerTrelloIds(Collections.singletonList(list));
                applicationContext.outputter.printList(applicationContext.wrkIdsManager, list);
                break;
            case Card:
                Card card = applicationContext.restTemplate.post(url, applicationContext.typeReferences.cardType);
                applicationContext.wrkIdsManager.registerTrelloIds(Collections.singletonList(card));
                applicationContext.outputter.printCard(applicationContext.wrkIdsManager, card);
                break;
        }
    }


    @Override protected boolean valid() {
        return (url != null);
    }

    @Override protected String getCommandName() {
        return "create";
    }
}
