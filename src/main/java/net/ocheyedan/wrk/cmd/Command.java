package net.ocheyedan.wrk.cmd;

import net.ocheyedan.wrk.RestTemplate;

/**
 * User: blangel
 * Date: 6/29/12
 * Time: 4:00 PM
 *
 * Represents a command given to wrk from the user via the command line.
 */
public abstract class Command implements Runnable {

    public final Args args;
    public final RestTemplate restTemplate;

    protected Command(Args args, RestTemplate restTemplate) {
        this.args = args;
        this.restTemplate = restTemplate;
    }
}
