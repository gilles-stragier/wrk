package net.ocheyedan.wrk.ids;

import com.fasterxml.jackson.core.type.TypeReference;
import net.ocheyedan.wrk.Json;
import net.ocheyedan.wrk.cmd.trello.TrelloId;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.TrelloObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class IdsAliasingManager {

    private final Set<IdMapping> mappings;

    private File cacheFile;

    private final IdGenerator idGenerator;

    public IdsAliasingManager() {
        this(new SequentialIdGenerator());
    }

    public IdsAliasingManager(IdGenerator idGenerator) {
        this.mappings = new HashSet<>();
        this.idGenerator = idGenerator;
        initCache();
    }

    private void initCache() {
        cacheFile = new File(String.format("%s%s%s%s%s", System.getProperty("user.home"), File.separator, ".wrk", File.separator, "wrk-ids"));
        try {
            if (cacheFile.exists()) {
                mappings.addAll(Json.mapper().readValue(cacheFile, new TypeReference<Set<IdMapping>>() { }));
            }
        } catch (FileNotFoundException fnfe) {
            // ignore
        } catch (IOException ioe) {
            Output.print(ioe);
        }
    }


    public <T extends TrelloObject> void registerTrelloId(T trelloObject) {
        registerTrelloIds(Arrays.asList(trelloObject));
    }

    public <T extends TrelloObject> void registerTrelloIds(List<T> trelloObjects) {
        if (trelloObjects != null && !trelloObjects.isEmpty()) {
            trelloObjects.stream().forEach(t -> mappings.add(idGenerator.generate(mappings, t)));
        }
    }

    public Boolean exists(TrelloObject object) {
        return findByTrelloId(object).isPresent();
    }

    public Optional<String> findByTrelloId(TrelloObject object) {
        return findByTrelloId(object.trelloId());
    }

    public Optional<String> findByTrelloId(TrelloId id) {
        return mappings.stream()
                .filter(m -> m.getTrelloId().equals(id))
                .map(
                        m -> m.getAliases().stream()
                                .findFirst()
                                .get()
                                .getId())
                .findFirst();
    }

    public void store() {
        try {
            Json.mapper().writeValue(cacheFile, mappings);
        } catch (IOException ioe) {
            Output.print(ioe);
        }
    }

    public Optional<TrelloId> findByWrkId(String wrkId) {
        return findByWrkId(new Alias(wrkId));
    }

    public Optional<TrelloId> findByWrkId(Alias alias) {
        return mappings.stream()
                .filter(m -> m.getAliases().contains(alias))
                .map(m -> m.getTrelloId())
                .findFirst();
    }

    public void clear() {
        mappings.clear();
        FileUtils.deleteQuietly(cacheFile);
    }

    public Set<IdMapping> findAll() {
        return Collections.unmodifiableSet(mappings);
    }
}
