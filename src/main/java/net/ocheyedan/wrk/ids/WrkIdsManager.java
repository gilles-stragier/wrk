package net.ocheyedan.wrk.ids;

import net.ocheyedan.wrk.trello.Card;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WrkIdsManager {

    private final Map<String, String> wrkIds;
    private final Map<String, String> addIds;

    public WrkIdsManager(Map<String, String> wrkIds) {
        this.wrkIds = wrkIds;
        this.addIds = new HashMap<>();
    }

    public void registerTrelloIds(List<Card> cards) {
        int cardIndex = wrkIds.size() + 1;
        for (Card card : cards) {
            String wrkId = "wrk" + cardIndex++;
            addIds.put(wrkId, String.format("c:%s", card.getId()));
        }
    }

    public Map<String, String> idsMap() {
        return Collections.unmodifiableMap(addIds);
    }

    public Map<String, String> invertedIdsMap() {
        return idsMap().entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}
