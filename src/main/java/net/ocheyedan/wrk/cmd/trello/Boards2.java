package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.Wrk2;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.Board;
import net.ocheyedan.wrk.trello.Trello;
import picocli.CommandLine;

import java.util.*;

@CommandLine.Command(
        name = "boards"
)
public final class Boards2 implements Runnable {

    @CommandLine.Parameters
    List<String> args = new ArrayList<>();

    @CommandLine.ParentCommand
    private Wrk2 parent;

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true)
    private boolean help;

    private ApplicationContext applicationContext;

    private String prettyPrint(String prefix) {
        if ("b:".equals(prefix)) {
            return "boards";
        } else if ("c:".equals(prefix)) {
            return "cards";
        } else if ("o:".equals(prefix)) {
            return "orgs";
        } else if ("m:".equals(prefix)) {
            return "members";
        } else if ("l:".equals(prefix)) {
            return "lists";
        } else {
            return "<unknown>";
        }
    }

    private String prefixesToString(Set<String> prefixes) {
        StringBuilder buffer = new StringBuilder("[ ");
        boolean first = true;
        for (String prefix : prefixes) {
            if (!first) {
                buffer.append(", ");
            }
            buffer.append("^b^");
            buffer.append(prettyPrint(prefix));
            buffer.append("^r^");
            first = false;
        }
        buffer.append(" ]");
        return buffer.toString();
    }

    protected IdCommand.LegacyTrelloId parseWrkId(String wrkId, Set<String> desiredPrefixes, IdsAliasingManager idsAliasingManager) {
        Optional<TrelloId> trelloOptional = idsAliasingManager.findByWrkId(wrkId);
        if (!trelloOptional.isPresent()) {
            return new IdCommand.LegacyTrelloId(wrkId, wrkId); // user entered a trello-id directly
        }
        String trelloId = trelloOptional.get().formatAsLegacy();
        String existingPrefix = (trelloId.length() > 2 ? trelloId.substring(0, 2) : "");
        if (!desiredPrefixes.contains(existingPrefix)) {
            // given a wrk-id for the wrong prefix; alert and exit.
            String type = prettyPrint(existingPrefix);
            Output.print("The wrk-id [ ^b^%s^r^ ] is for ^red^%s^r^ but the command is for %s.", wrkId, type, prefixesToString(desiredPrefixes));
            System.exit(1);
        }
        return new IdCommand.LegacyTrelloId(trelloId.substring(2), trelloId);
    }

    @SuppressWarnings("serial")
    static final Set<String> orgPrefix = new HashSet<>(1) {
        {
            add("o:");
        }
    };

    @Override
    public void run() {
        applicationContext = parent.createApplicationContext();

        String url;
        String description;

        if ((args.size() == 2) && "in".equals(args.get(0))) {
            IdCommand.LegacyTrelloId orgId = parseWrkId(args.get(1), orgPrefix, applicationContext.wrkIdsManager);
            url = Trello.url("https://trello.com/1/organization/%s/boards?filter=open&key=%s&token=%s", orgId.id,
                    Trello.APP_DEV_KEY, Trello.USR_TOKEN);
            description = String.format("Open boards for organization ^b^%s^r^:", orgId.id);
        } else if (args.isEmpty()) {
            url = Trello.url("https://trello.com/1/members/my/boards?filter=open&key=%s&token=%s",
                    Trello.APP_DEV_KEY, Trello.USR_TOKEN);
            description = "Open boards you've created:";
        } else {
            url = description = null;
        }

        Output.print(description);
        List<Board> boards = applicationContext.restTemplate.get(url, applicationContext.typeReferences.boardListType);

        applicationContext.wrkIdsManager.registerTrelloIds(boards);
        applicationContext.outputter.printBoards(boards, applicationContext.wrkIdsManager);

    }


}
