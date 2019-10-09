package net.ocheyedan.wrk.ids;

import net.ocheyedan.wrk.trello.TrelloObject;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.Normalizer;
import java.util.Collection;
import java.util.Set;

public class NameIdGenerator implements IdGenerator {

    @Override
    public IdMapping generate(Set<IdMapping> existingIds, TrelloObject object) {
        for (int i = 0; i < 3; i++) {
            Alias alias = generateOneId(object.name());

            if (!existingIds.stream().anyMatch(m -> m.contains(alias))) {
                return IdMapping.initiliaze(object.trelloId(), alias);
            }
        }
        throw new IllegalStateException("Unable to generate unique ids after 3 try - Please PANIC !");
    }


    private String randomizedSuffix() {
        return RandomStringUtils.random(3, false, true);
    }

    private Alias generateOneId(String objectName) {
        return new Alias(
                Normalizer.normalize(objectName, Normalizer.Form.NFD)
                        .toLowerCase()
                        .replaceAll("[^A-Za-z0-9 ]", "")
                        .replaceAll(" ", "-")
                        + "-" + randomizedSuffix()
        );
    }
}
