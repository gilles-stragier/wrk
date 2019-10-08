package net.ocheyedan.wrk.ids;

import net.ocheyedan.wrk.trello.TrelloObject;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.Normalizer;
import java.util.Collection;

public class NameIdGenerator implements IdGenerator {

    @Override
    public String generate(Collection<String> existingIds, TrelloObject object) {
        for(int i=0; i <3; i++) {
            String id = generateOneId(object.name());
            if (!existingIds.contains(id)) {
                return id;
            }
        }
        throw new IllegalStateException("Unable to generate unique ids after 3 try - Please PANIC !");
    }

    private String randomizedSuffix() {
        return RandomStringUtils.random(3, false, true);
    }

    private String generateOneId(String objectName) {
        return
                Normalizer.normalize(objectName, Normalizer.Form.NFD)
                .toLowerCase()
                .replaceAll("[^A-Za-z0-9 ]","")
                .replaceAll(" ", "-")
                + "-" + randomizedSuffix()
                ;

    }
}
