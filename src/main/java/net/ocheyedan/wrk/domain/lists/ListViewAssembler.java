package net.ocheyedan.wrk.domain.lists;

import net.ocheyedan.wrk.domain.Assembler;
import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.trello.List;

import java.util.Optional;

public class ListViewAssembler implements Assembler<ListView, List> {

    private final IdsAliasingManager idsAliasingManager;

    public ListViewAssembler(IdsAliasingManager idsAliasingManager) {
        this.idsAliasingManager = idsAliasingManager;
    }

    @Override
    public ListView assemble(List list) {
        Optional<String> byTrelloId = idsAliasingManager.findByTrelloId(list);

        return ListView.newBuilder()
                .id(byTrelloId.orElse(list.getId()))
                .name(list.getName())
                .pos(list.getPos())
                .build();
    }

}
