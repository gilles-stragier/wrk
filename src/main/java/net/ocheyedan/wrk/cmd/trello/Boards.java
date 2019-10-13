package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.cmd.trello.boards.BoardsService;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.Board;

import java.util.List;

import static net.ocheyedan.wrk.trello.TrelloObject.Type.ORG;

/**
 * User: blangel
 * Date: 6/29/12
 * Time: 9:24 PM
 */
public final class Boards extends IdCommand {
    private final BoardsService boardsService;

    public Boards(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
        this.boardsService = applicationContext.boardsService;
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
        List<Board> boards = boardsService.fetchMyBoards();

        Output.print("Open boards you've created:");
        applicationContext.outputter.printBoards(boards, applicationContext.wrkIdsManager);
    }

    private void fetchBoardsOfOrg() {
        TrelloId orgId = TrelloId.parseTrelloId(args.args.get(1), applicationContext.wrkIdsManager, ORG);
        List<Board> boards = boardsService.fetchBoardsOfOrg(orgId);

        Output.print(String.format("Open boards for organization ^b^%s^r^:", orgId.getId()));
        applicationContext.outputter.printBoards(boards, applicationContext.wrkIdsManager);
    }

    @Override protected String getCommandName() {
        return "boards";
    }

}
