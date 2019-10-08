package net.ocheyedan.wrk.cmd.trello;

import com.fasterxml.jackson.core.type.TypeReference;
import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.Json;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.cmd.Command;
import net.ocheyedan.wrk.cmd.Usage;
import net.ocheyedan.wrk.output.Output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * User: blangel
 * Date: 6/30/12
 * Time: 3:03 PM
 *
 * Collects wrk ids and saves them for subsequent use by other commands.
 */
abstract class IdCommand extends Command {

    static final class TrelloId {

        final String id;

        final String idWithTypePrefix;

        TrelloId(String id, String idWithTypePrefix) {
            this.id = id;
            this.idWithTypePrefix = idWithTypePrefix;
        }
    }

    @SuppressWarnings("serial")
    static final Set<String> orgPrefix = new HashSet<>(1) {
        {
            add("o:");
        }
    };
    @SuppressWarnings("serial")
    static final Set<String> boardsPrefix = new HashSet<>(1) {
        {
            add("b:");
        }
    };
    @SuppressWarnings("serial")
    static final Set<String> listsPrefix = new HashSet<>(1) {
        {
            add("l:");
        }
    };
    @SuppressWarnings("serial")
    static final Set<String> cardsPrefix = new HashSet<>(1) {
        {
            add("c:");
        }
    };
    @SuppressWarnings("serial")
    static final Set<String> membersPrefix = new HashSet<>(1) {
        {
            add("m:");
        }
    };
    @SuppressWarnings("serial")
    static final Set<String> boardsListsPrefix = new HashSet<>(1) {
        {
            add("b:");
            add("l:");
        }
    };
    @SuppressWarnings("serial")
    static final Set<String> boardsListsCardsPrefix = new HashSet<>(1) {
        {
            add("b:");
            add("l:");
            add("c:");
        }
    };
    @SuppressWarnings("serial")
    static final Set<String> orgsBoardsCardsPrefix = new HashSet<>(1) {
        {
            add("o:");
            add("b:");
            add("c:");
        }
    };
    @SuppressWarnings("serial")
    static final Set<String> allPrefix = new HashSet<>(1) {
        {
            add("o:");
            add("b:");
            add("l:");
            add("c:");
            add("m:");
        }
    };

    protected IdCommand(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
    }

    protected abstract boolean valid();

    protected abstract String getCommandName();

    @Override public final void run() {
        if (!valid()) {
            Output.print("^red^Invalid arguments to command ^i^%s^r^^red^: %s^r^", getCommandName(), args);
            new Usage(getCommandName(), applicationContext).run();
            return;
        }
        _run();
        applicationContext.wrkIdsManager.store();
    }


    static String validate(String value, String type, String plural) {
        return validate(value, type, plural, false);
    }

    static String validate(String value, String type, String plural, boolean failOnLength) {
        if ((value == null) || value.isEmpty()) {
            Output.print("^red^%s was empty, doing nothing.^r^", type);
            System.exit(0);
        }
        if (value.length() > 16384) {
            if (!failOnLength) {
                Output.print("^red^Trello %s must be less than 16,384 characters, shortening.^r^", plural);
                return value.substring(0, 16384);
            } else {
                Output.print("^red^Trello %s must be less than 16,384 characters, doing nothing.^r^", plural);
                System.exit(0);
            }
        }
        return value;
    }

    static String encode(String comment) {
        return URLEncoder.encode(comment, StandardCharsets.UTF_8);
    }

    protected TrelloId parseWrkId(String wrkId, Set<String> desiredPrefixes) {
        Optional<String> trelloOptional = applicationContext.wrkIdsManager.findByWrkId(wrkId);
        if (!trelloOptional.isPresent()) {
            return new TrelloId(wrkId, wrkId); // user entered a trello-id directly
        }
        String trelloId = trelloOptional.get();
        String existingPrefix = (trelloId.length() > 2 ? trelloId.substring(0, 2) : "");
        if (!desiredPrefixes.contains(existingPrefix)) {
            // given a wrk-id for the wrong prefix; alert and exit.
            String type = prettyPrint(existingPrefix);
            Output.print("The wrk-id [ ^b^%s^r^ ] is for ^red^%s^r^ but the command is for %s.", wrkId, type, prefixesToString(desiredPrefixes));
            System.exit(1);
        }
        return new TrelloId(trelloId.substring(2), trelloId);
    }

    private String prettyPrint(String prefix) {
        if ("b:".equals(prefix)) {
            return "boards";
        } else if ("c:".equals(prefix)) {
            return "cards";
        } else if ("o:".equals(prefix)) {
            return "orgs";
        } else if ("m:".equals(prefix)) {
            return "members";
        } else if ("l:".equals(prefix)) {
            return "lists";
        } else {
            return "<unknown>";
        }
    }

    private String prefixesToString(Set<String> prefixes) {
        StringBuilder buffer = new StringBuilder("[ ");
        boolean first = true;
        for (String prefix : prefixes) {
            if (!first) {
                buffer.append(", ");
            }
            buffer.append("^b^");
            buffer.append(prettyPrint(prefix));
            buffer.append("^r^");
            first = false;
        }
        buffer.append(" ]");
        return buffer.toString();
    }

    protected abstract void _run();
}
