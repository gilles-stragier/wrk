package net.ocheyedan.wrk;

import net.ocheyedan.wrk.cmd.TypeReferences;
import net.ocheyedan.wrk.cmd.trello.boards.BoardsService;
import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.output.Outputter;

public class ApplicationContext {

    public final RestTemplate restTemplate;
    public final TypeReferences typeReferences;
    public final Outputter outputter;
    public final IdsAliasingManager wrkIdsManager;
    public final BoardsService boardsService;

    public ApplicationContext(
            RestTemplate restTemplate,
            TypeReferences typeReferences,
            Outputter outputter,
            IdsAliasingManager wrkIdsManager
    ) {
        this.restTemplate = restTemplate;
        this.typeReferences = typeReferences;
        this.outputter = outputter;
        this.wrkIdsManager = wrkIdsManager;
        this.boardsService = new BoardsService(wrkIdsManager, typeReferences, restTemplate);
    }
}
