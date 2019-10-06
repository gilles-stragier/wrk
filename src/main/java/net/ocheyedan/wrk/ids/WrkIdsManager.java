package net.ocheyedan.wrk.ids;

import net.ocheyedan.wrk.trello.TrelloObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableMap;

public class WrkIdsManager {

    private final Map<String, String> addIds;

    public WrkIdsManager() {
        this.addIds = new HashMap<>();
    }

    public <T extends TrelloObject> void registerTrelloIds(List<T> trelloObjects) {
        if (trelloObjects != null && !trelloObjects.isEmpty()) {
            int startIndex = addIds.size() + 1;
            for (T trelloObject : trelloObjects) {
                String wrkId = "wrk" + startIndex++;
                addIds.put(wrkId, String.format(trelloObject.keyPrefix() + "%s", trelloObject.getId()));
            }
        }
    }

    public Map<String, String> idsMap() {
        return unmodifiableMap(addIds);
    }

    public Map<String, String> invertedIdsMap() {
        return idsMap().entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}
