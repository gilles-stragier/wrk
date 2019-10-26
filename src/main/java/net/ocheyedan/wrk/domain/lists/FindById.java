package net.ocheyedan.wrk.domain.lists;

import net.ocheyedan.wrk.RestTemplate;
import net.ocheyedan.wrk.cmd.TypeReferences;
import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.trello.List;
import net.ocheyedan.wrk.trello.Trello;

public class FindById {


    private final RestTemplate restTemplate;
    private final TypeReferences typeReferences;
    private final IdsAliasingManager idsAliasingManager;
    private final ListViewAssembler listViewAssembler;

    public FindById(
            RestTemplate restTemplate,
            TypeReferences typeReferences,
            IdsAliasingManager idsAliasingManager,
            ListViewAssembler listViewAssembler
    ) {
        this.restTemplate = restTemplate;
        this.typeReferences = typeReferences;
        this.idsAliasingManager = idsAliasingManager;
        this.listViewAssembler = listViewAssembler;
    }

    public ListView execute(String id) {

        String url = Trello.url("https://trello.com/1/lists/%s/?fields=all&key=%s&token=%s", id, Trello.APP_DEV_KEY,
                Trello.USR_TOKEN);

        List list = restTemplate.get(url, typeReferences.listType);
        idsAliasingManager.registerTrelloId(list);

        return listViewAssembler.assemble(list);
    }

}
