package net.ocheyedan.wrk.cmd.trello.boards;

import net.ocheyedan.wrk.RestTemplate;
import net.ocheyedan.wrk.cmd.TypeReferences;
import net.ocheyedan.wrk.cmd.trello.TrelloId;
import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.trello.Board;
import net.ocheyedan.wrk.trello.Trello;

import java.util.List;

public class BoardsService {

    private final IdsAliasingManager idsAliasingManager;
    private final TypeReferences typeReferences;
    private final RestTemplate restTemplate;

    public BoardsService(
            IdsAliasingManager idsAliasingManager,
            TypeReferences typeReferences,
            RestTemplate restTemplate
    ) {
        this.idsAliasingManager = idsAliasingManager;
        this.typeReferences = typeReferences;
        this.restTemplate = restTemplate;
    }

    public List<Board> fetchMyBoards() {
        String url = Trello.url("https://trello.com/1/members/my/boards?filter=open&key=%s&token=%s",
                Trello.APP_DEV_KEY, Trello.USR_TOKEN);
        List<Board> boards = restTemplate.get(url, typeReferences.boardListType);
        idsAliasingManager.registerTrelloIds(boards);
        return boards;
    }

    public List<Board> fetchBoardsOfOrg(TrelloId orgId) {
        String url = Trello.url("https://trello.com/1/organization/%s/boards?filter=open&key=%s&token=%s", orgId.getId(),
                Trello.APP_DEV_KEY, Trello.USR_TOKEN);
        List<Board> boards = restTemplate.get(url, typeReferences.boardListType);
        idsAliasingManager.registerTrelloIds(boards);
        return boards;
    }
}
