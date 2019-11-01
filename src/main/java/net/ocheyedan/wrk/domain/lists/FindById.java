package net.ocheyedan.wrk.domain.lists;

import com.fasterxml.jackson.core.type.TypeReference;
import net.ocheyedan.wrk.RestTemplate;
import net.ocheyedan.wrk.domain.Assembler;
import net.ocheyedan.wrk.domain.View;
import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.trello.Trello;
import net.ocheyedan.wrk.trello.TrelloObject;

public class FindById<T extends TrelloObject> {

    private final RestTemplate restTemplate;
    private final TypeReference<T> typeReference;
    private final IdsAliasingManager idsAliasingManager;
    private final String urlTemplate;

    public FindById(
            RestTemplate restTemplate,
            TypeReference<T> typeReference,
            IdsAliasingManager idsAliasingManager,
            String urlTemplate
    ) {
        this.restTemplate = restTemplate;
        this.typeReference = typeReference;
        this.idsAliasingManager = idsAliasingManager;
        this.urlTemplate = urlTemplate;
    }

    public <U extends View> U execute(String id, Assembler<U, T> assembler) {

        String url = Trello.url(urlTemplate, id, Trello.APP_DEV_KEY, Trello.USR_TOKEN);

        T trelloObj = restTemplate.get(url, typeReference);
        idsAliasingManager.registerTrelloId(trelloObj);
        return assembler.assemble(trelloObj);
    }

}
