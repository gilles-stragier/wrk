package net.ocheyedan.wrk.cmd;

import net.ocheyedan.wrk.ApplicationContext;

/**
 * User: blangel
 * Date: 6/29/12
 * Time: 4:00 PM
 *
 * Represents a command given to wrk from the user via the command line.
 */
public abstract class Command {

    public final Args args;
    public final ApplicationContext applicationContext;

    protected Command(Args args, ApplicationContext applicationContext) {
        this.args = args;
        this.applicationContext = applicationContext;
    }

    public abstract void run();
}
