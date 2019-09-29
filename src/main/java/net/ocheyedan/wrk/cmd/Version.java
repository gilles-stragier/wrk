package net.ocheyedan.wrk.cmd;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.Output;
import net.ocheyedan.wrk.RestTemplate;

/**
 * User: blangel
 * Date: 6/29/12
 * Time: 8:56 PM
 *
 * Prints version information.
 */
public final class Version extends Command {

    public Version(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
    }

    @Override public void run() {
        Output.print("^b^wrk^r^ version ^b^%s^r^", System.getProperty("wrk.version"));
        Output.print("   installed at ^b^%s^r^", System.getProperty("wrk.home"));
        Output.print("  with java version ^b^%s^r^ from vendor ^b^%s^r^", System.getProperty("java.version"),
                System.getProperty("java.vendor"));
        Output.print("   installed at ^b^%s^r^", System.getProperty("java.home"));
        Output.print("  and operating-system ^b^%s^r^ version ^b^%s^r^ arch ^b^%s^r^", System.getProperty("os.name"),
                System.getProperty("os.version"),
                System.getProperty("os.arch"));

    }
}
