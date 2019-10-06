package net.ocheyedan.wrk.cmd.trello;

import com.fasterxml.jackson.core.type.TypeReference;
import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.*;

import java.util.Collections;
import java.util.Map;

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
            TrelloId id = parseWrkId(args.args.get(0), allPrefix);
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

    @Override protected Map<String, String> _run() {
        Output.print(description);
        String desc;
        switch (type) {
            case Org:
                Organization org = applicationContext.restTemplate.get(url, new TypeReference<>() {
                });
                if (org == null) {
                    Output.print("^red^Invalid id or not found.^r^");
                    break;
                }
                Output.print("  ^b^%s^r^ ^black^| %s^r^", org.getDisplayName(), org.getId());
                desc = (org.getDesc() == null ? "" : org.getDesc());
                if (!desc.isEmpty()) {
                    Output.print("    %s", desc);
                }
                Output.print("    ^black^%s^r^", org.getUrl());
                break;
            case Board:
                Board board = applicationContext.restTemplate.get(url, new TypeReference<>() {
                });
                if (board == null) {
                    Output.print("^red^Invalid id or not found.^r^");
                    break;
                }
                String boardClosed = ((board.getClosed() != null) && board.getClosed()) ? "^black^[closed] ^r^" : "^b^";
                Output.print("  %s%s^r^ ^black^| %s^r^", boardClosed, board.getName(), board.getId());
                desc = (board.getDesc() == null ? "" : board.getDesc());
                if (!desc.isEmpty()) {
                    Output.print("    %s", desc);
                }
                Output.print("    ^black^%s^r^", board.getUrl());
                break;
            case List:
                net.ocheyedan.wrk.trello.List list = applicationContext.restTemplate.get(url, new TypeReference<>() {
                });
                if (list == null) {
                    Output.print("^red^Invalid id or not found.^r^");
                    break;
                }
                String closed = ((list.getClosed() != null) && list.getClosed()) ? "^black^[closed] ^r^" : "^b^";
                Output.print("  %s%s^r^ ^black^| %s^r^", closed, list.getName(), list.getId());
                break;
            case Card:
                Card card = applicationContext.restTemplate.get(url, applicationContext.typeReferences.cardType);
                applicationContext.defaultOutputter.describeCard(card);
                break;
            case Member:
                Member member = applicationContext.restTemplate.get(url, new TypeReference<>() {
                });
                if (member == null) {
                    Output.print("^red^Invalid id or not found.^r^");
                    break;
                }
                Output.print("  ^b^%s^r^ ^black^| %s^r^", member.getFullName(), member.getId());
                Output.print("    ^black^username^r^ %s", member.getUsername());
                break;
        }
        return Collections.emptyMap();
    }

    @Override protected boolean valid() {
        return (url != null);
    }

    @Override protected String getCommandName() {
        return "desc";
    }
}
