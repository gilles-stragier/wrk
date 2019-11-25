package net.ocheyedan.wrk.cmd;

import net.ocheyedan.wrk.Collections;

import java.util.List;

/**
 * User: blangel
 * Date: 6/29/12
 * Time: 4:02 PM
 *
 * Represents the command line arguments passed to wrk.
 */
public final class Args {

    public final List<String> args;

    public Args(List<String> args) {
        this.args = args;
    }

    @Override public String toString() {
        if ((args == null) || args.isEmpty()) {
            return "<none>";
        }
        return Collections.asAString(args);
    }

}
