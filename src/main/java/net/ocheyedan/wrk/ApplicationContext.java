package net.ocheyedan.wrk;

import net.ocheyedan.wrk.cmd.TypeReferences;

public class ApplicationContext {

    public final RestTemplate restTemplate;
    public final TypeReferences typeReferences;

    public ApplicationContext(RestTemplate restTemplate, TypeReferences typeReferences) {
        this.restTemplate = restTemplate;
        this.typeReferences = typeReferences;
    }
}
