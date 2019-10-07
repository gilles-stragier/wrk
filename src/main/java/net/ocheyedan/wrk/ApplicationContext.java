package net.ocheyedan.wrk;

import net.ocheyedan.wrk.cmd.TypeReferences;
import net.ocheyedan.wrk.ids.WrkIdsManager;
import net.ocheyedan.wrk.output.DefaultOutputter;

public class ApplicationContext {

    public final RestTemplate restTemplate;
    public final TypeReferences typeReferences;
    public final DefaultOutputter defaultOutputter;
    public final WrkIdsManager wrkIdsManager;

    public ApplicationContext(
            RestTemplate restTemplate,
            TypeReferences typeReferences,
            DefaultOutputter defaultOutputter,
            WrkIdsManager wrkIdsManager
    ) {
        this.restTemplate = restTemplate;
        this.typeReferences = typeReferences;
        this.defaultOutputter = defaultOutputter;
        this.wrkIdsManager = wrkIdsManager;
    }
}
