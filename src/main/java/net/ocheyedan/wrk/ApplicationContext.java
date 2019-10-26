package net.ocheyedan.wrk;

import net.ocheyedan.wrk.cmd.TypeReferences;
import net.ocheyedan.wrk.domain.cards.SearchCards;
import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.output.Outputter;

public class ApplicationContext {

    public final RestTemplate restTemplate;
    public final TypeReferences typeReferences;
    public final Outputter outputter;
    public final IdsAliasingManager wrkIdsManager;
    public final SearchCards searchCards;

    public ApplicationContext(
            RestTemplate restTemplate,
            TypeReferences typeReferences,
            Outputter outputter,
            IdsAliasingManager wrkIdsManager,
            SearchCards searchCards
    ) {
        this.restTemplate = restTemplate;
        this.typeReferences = typeReferences;
        this.outputter = outputter;
        this.wrkIdsManager = wrkIdsManager;
        this.searchCards = searchCards;
    }
}
