package net.ocheyedan.wrk.cmd;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.output.Output;

import java.util.stream.Collectors;

/**
 * User: blangel
 * Date: 6/29/12
 * Time: 8:56 PM
 * <p>
 * Prints version information.
 */
public final class Ids extends Command {

    Ids(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
    }

    @Override
    public void run() {
        if (args.args.size() == 1) {
            switch (args.args.get(0)) {
                case "clear":
                    clear();
                    return;
                case "list":
                    list();
                    return;
            }
        }
    }

    private void clear() {
        applicationContext.wrkIdsManager.clear();
    }

    private void list() {
        applicationContext.wrkIdsManager.findAll().forEach(idm -> {
            Output.print("%s (^b^%s^r^) -> %s", idm.getTrelloId().getId(), idm.getTrelloId().getType().name().toLowerCase(), idm.getAliases().stream().map(a -> a.getId()).collect(Collectors.joining()));
        });
    }
}
