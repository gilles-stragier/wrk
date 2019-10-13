package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.Board;
import net.ocheyedan.wrk.trello.Trello;

import java.util.List;

/**
 * User: blangel
 * Date: 6/29/12
 * Time: 9:24 PM
 */
public final class Boards extends IdCommand {


    public Boards(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
    }

    private boolean myBoardsMode(Args args) {
        return args.args.isEmpty();
    }

    private boolean orgMode(Args args) {
        return (args.args.size() == 2) && "in".equals(args.args.get(0));
    }

    @Override
    protected boolean valid() {
        return orgMode(args) || myBoardsMode(args);
    }

    @Override
    protected void _run() {

        if (orgMode(args)) {
            fetchBoardsOfOrg();
        } else if (myBoardsMode(args)) {
            fetchMyBoards();
        }

    }

    private void fetchMyBoards() {

        String url = Trello.url("https://trello.com/1/members/my/boards?filter=open&key=%s&token=%s",
                Trello.APP_DEV_KEY, Trello.USR_TOKEN);
        List<Board> boards = applicationContext.restTemplate.get(url, applicationContext.typeReferences.boardListType);
        applicationContext.wrkIdsManager.registerTrelloIds(boards);


        Output.print("Open boards you've created:");
        applicationContext.outputter.printBoards(boards, applicationContext.wrkIdsManager);
    }

    private void fetchBoardsOfOrg() {

        LegacyTrelloId orgId = parseWrkId(args.args.get(1), orgPrefix);
        String url = Trello.url("https://trello.com/1/organization/%s/boards?filter=open&key=%s&token=%s", orgId.id,
                Trello.APP_DEV_KEY, Trello.USR_TOKEN);
        List<Board> boards = applicationContext.restTemplate.get(url, applicationContext.typeReferences.boardListType);
        applicationContext.wrkIdsManager.registerTrelloIds(boards);

        Output.print(String.format("Open boards for organization ^b^%s^r^:", orgId.id));
        applicationContext.outputter.printBoards(boards, applicationContext.wrkIdsManager);
    }

    @Override protected String getCommandName() {
        return "boards";
    }

}
