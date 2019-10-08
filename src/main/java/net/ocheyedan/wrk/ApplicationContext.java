package net.ocheyedan.wrk;

import net.ocheyedan.wrk.cmd.TypeReferences;
import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.output.DefaultOutputter;

public class ApplicationContext {

    public final RestTemplate restTemplate;
    public final TypeReferences typeReferences;
    public final DefaultOutputter defaultOutputter;
    public final IdsAliasingManager wrkIdsManager;

    public ApplicationContext(
            RestTemplate restTemplate,
            TypeReferences typeReferences,
            DefaultOutputter defaultOutputter,
            IdsAliasingManager wrkIdsManager
    ) {
        this.restTemplate = restTemplate;
        this.typeReferences = typeReferences;
        this.defaultOutputter = defaultOutputter;
        this.wrkIdsManager = wrkIdsManager;
    }
}
