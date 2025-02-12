package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.Collections;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.SearchResult;
import net.ocheyedan.wrk.trello.Trello;

/**
 * User: blangel
 * Date: 7/3/12
 * Time: 4:05 PM
 */
public final class Search extends IdCommand {

    private final String url;

    private final String description;

    public Search(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
        String query, entered;
        if (args.args.size() > 1) {
            entered = Collections.asAString(args.args.subList(1, args.args.size()));
            query = validate(encode(entered), "Search query", "search queries", true);
            String type = args.args.get(0);
            if ("orgs".equals(type)) {
                url = Trello.url("https://trello.com/1/search?query=%s&modelTypes=organizations&organizations_limit=1000&key=%s&token=%s", query,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
                description = String.format("Searching organizations for ^b^%s^r^", entered);
            } else if ("boards".equals(type)) {
                url = Trello.url("https://trello.com/1/search?query=%s&modelTypes=boards&board_fields=name,url&boards_limit=1000&key=%s&token=%s", query,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
                description = String.format("Searching boards for ^b^%s^r^", entered);
            } else if ("cards".equals(type)) {
                url = Trello.url("https://trello.com/1/search?query=%s&modelTypes=cards&cards_limit=1000&key=%s&token=%s", query,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
                description = String.format("Searching cards for ^b^%s^r^", entered);
            } else if ("members".equals(type)) {
                url = Trello.url("https://trello.com/1/search?query=%s&modelTypes=members&members_limit=1000&key=%s&token=%s", query,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
                description = String.format("Searching members for ^b^%s^r^", entered);
            } else {
                entered = Collections.asAString(args.args);
                query = validate(encode(entered), "Search query", "search queries", true);
                url = Trello.url("https://trello.com/1/search?query=%s&board_fields=name,url&boards_limit=1000&cards_limit=1000&organizations_limit=1000&members_limit=1000&key=%s&token=%s", query,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
                description = String.format("Searching for ^b^%s^r^", entered);
            }
        } else if (args.args.size() == 1) {
            entered = args.args.get(0);
            query = validate(encode(entered), "Search query", "search queries", true);
            url = Trello.url("https://trello.com/1/search?query=%s&board_fields=name,url&boards_limit=1000&cards_limit=1000&organizations_limit=1000&members_limit=1000&key=%s&token=%s", query,
                    Trello.APP_DEV_KEY, Trello.USR_TOKEN);
            description = String.format("Searching for ^b^%s^r^", entered);
        } else {
            url = description = null;
        }
    }

    @Override
    protected void _run() {
        Output.print(description);
        SearchResult searchResults = applicationContext.restTemplate.get(url, applicationContext.typeReferences.searchType);
        if (searchResults == null) {
            Output.print("^red^Invalid query.^r^");
            return;
        }
        if (!searchResults.getOrganizations().isEmpty()) {
            Output.print("Found ^b^%d organization%s%s^r^.", searchResults.getOrganizations().size(),
                         (searchResults.getOrganizations().size() == 1 ? "" : "s"), (searchResults.getOrganizations().size() == 1000 ? " (limited to 1000)" : ""));

            applicationContext.wrkIdsManager.registerTrelloIds(searchResults.getOrganizations());
            applicationContext.outputter.printOrgs(searchResults.getOrganizations(), applicationContext.wrkIdsManager);
        }
        if (!searchResults.getBoards().isEmpty()) {
            Output.print("Found ^b^%d board%s%s^r^.", searchResults.getBoards().size(),
                    (searchResults.getBoards().size() == 1 ? "" : "s"), (searchResults.getBoards().size() == 1000 ? " (limited to 1000)" : ""));

            applicationContext.wrkIdsManager.registerTrelloIds(searchResults.getBoards());
            applicationContext.outputter.printBoards(searchResults.getBoards(), applicationContext.wrkIdsManager);
        }
        if (!searchResults.getCards().isEmpty()) {
            Output.print("Found ^b^%d card%s%s^r^.", searchResults.getCards().size(),
                    (searchResults.getCards().size() == 1 ? "" : "s"), (searchResults.getCards().size() == 1000 ? " (limited to 1000)" : ""));

            applicationContext.wrkIdsManager.registerTrelloIds(searchResults.getCards());
            applicationContext.outputter.printCards(searchResults.getCards(), applicationContext.wrkIdsManager);

        }
        if (!searchResults.getMembers().isEmpty()) {
            Output.print("Found ^b^%d member%s%s^r^.", searchResults.getMembers().size(),
                    (searchResults.getMembers().size() == 1 ? "" : "s"), (searchResults.getMembers().size() == 1000 ? " (limited to 1000)" : ""));

            applicationContext.wrkIdsManager.registerTrelloIds(searchResults.getMembers());
            applicationContext.outputter.printMembers(searchResults.getMembers(), applicationContext.wrkIdsManager);
        }
        // TODO - actions?
        if (!searchResults.hadResults()) {
            Output.print("^black^No results.^r^");
        }
    }


    @Override protected boolean valid() {
        return (url != null);
    }

    @Override protected String getCommandName() {
        return "search";
    }
}
