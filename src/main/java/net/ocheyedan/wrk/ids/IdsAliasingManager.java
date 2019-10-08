package net.ocheyedan.wrk.ids;

import com.fasterxml.jackson.core.type.TypeReference;
import net.ocheyedan.wrk.Json;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.TrelloObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class IdsAliasingManager {

    private final Map<String, String> addIds;

    private File wrkIdsFile;

    private final IdGenerator idGenerator;

    public IdsAliasingManager() {
        this(new SequentialIdGenerator());
    }

    public IdsAliasingManager(IdGenerator idGenerator) {
        this.addIds = new HashMap<>();
        this.idGenerator = idGenerator;
        initFileStuff();
    }

    private void initFileStuff() {
        wrkIdsFile = new File(String.format("%s%s%s%s%s", System.getProperty("user.home"), File.separator, ".wrk", File.separator, "wrk-ids"));
        try {
            if (wrkIdsFile.exists()) {
                addIds.putAll(Json.mapper().readValue(wrkIdsFile, new TypeReference<Map<String, String>>() { }));
            }
        } catch (FileNotFoundException fnfe) {
            // ignore
        } catch (IOException ioe) {
            Output.print(ioe);
        }
    }


    public <T extends TrelloObject> void registerTrelloIds(List<T> trelloObjects) {
        if (trelloObjects != null && !trelloObjects.isEmpty()) {
            for (T trelloObject : trelloObjects) {
                if (!exists(trelloObject)) {
                    String generatedId = idGenerator.generate(addIds.keySet(), trelloObject);
                    addIds.put(generatedId, trelloObject.type().keyPrefix() + trelloObject.getId());
                }
            }
        }
    }

    public Boolean exists(TrelloObject object) {
        return findByTrelloId(object).isPresent();
    }

    public Optional<String> findByTrelloId(TrelloObject object) {
        return findByTrelloId(object.getId(), object.type());
    }

    public Optional<String> findByTrelloId(String tid, TrelloObject.Type type) {
        return Optional.ofNullable(invertedIdsMap().get(type.keyPrefix() + tid));
    }

    private Map<String, String> invertedIdsMap() {
        return addIds.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    public void store() {
        try {
            Json.mapper().writeValue(wrkIdsFile, addIds);
        } catch (IOException ioe) {
            Output.print(ioe);
        }
    }

    public Optional<String> findByWrkId(String wrkId) {
        return Optional.ofNullable(addIds.get(wrkId));
    }
}
