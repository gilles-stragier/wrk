package net.ocheyedan.wrk.trello;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.output.Output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * User: blangel
 * Date: 6/29/12
 * Time: 8:33 PM
 *
 * Utility methods for when interacting with {@literal Trello}
 */
public final class Trello {

    public static final String APP_DEV_KEY = System.getProperty("wrk.trello.usr.key");

    public static final String USR_TOKEN = System.getProperty("wrk.trello.usr.token");

    private static final String USR_ID = System.getProperty("wrk.trello.usr.id");

    public static String url(String base, Object ... args) {
        String combined = String.format(base, args);
        URI uri = URI.create(combined);
        return uri.toASCIIString();
    }

    public static String getUsrId(ApplicationContext applicationContext) {
        if ((USR_ID == null) || USR_ID.isEmpty()) {
            String url = url("https://trello.com/1/members/my?fields=initials&key=%s&token=%s", APP_DEV_KEY, USR_TOKEN);
            Map<String, String> result = applicationContext.restTemplate.get(url, applicationContext.typeReferences.mapType);
            String id = result.get("id");
            File file = new File(String.format("%s%s%s%s%s", System.getProperty("user.home"), File.separator, ".wrk", File.separator, "usrid"));
            FileWriter fileWriter = null;
            try {
                file.createNewFile();
                fileWriter = new FileWriter(file, false);
                fileWriter.write(id);
                fileWriter.flush();
            } catch (IOException ioe) {
                Output.print(ioe);
            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException ioe) {
                        Output.print(ioe);
                    }
                }
            }
            return id;
        } else {
            return USR_ID;
        }
    }

    private Trello() {

    }

}
