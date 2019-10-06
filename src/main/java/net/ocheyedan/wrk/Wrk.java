package net.ocheyedan.wrk;

import net.ocheyedan.wrk.cmd.Command;
import net.ocheyedan.wrk.cmd.CommandLineParser;
import net.ocheyedan.wrk.cmd.TypeReferences;
import net.ocheyedan.wrk.output.DefaultOutputter;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.Trello;

/**
 * User: blangel
 * Date: 6/29/12
 * Time: 3:56 PM
 *
 * The main entry point of the application.
 */
public final class Wrk {

    private final ApplicationContext applicationContext;

    Wrk(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext(
                new RestTemplate(),
                new TypeReferences(),
                new DefaultOutputter()
        );
        Wrk wrk = new Wrk(applicationContext);
        wrk.execute(args);

    }

    void execute(String[] args) {
        Config.init();
        ensureTrelloToken();

        Command command = CommandLineParser.parse(args, this.applicationContext);
        command.run();
    }

    private static void ensureTrelloToken() {
        if (Trello.USR_TOKEN.isEmpty()) {
            Output.print("^red^Trello token not set.^r^");
            Output.print("  You must obtain a token from Trello for your user and place it in file [ ^b^~/.wrk/token^r^ ].");
            Output.print("  Wrk's key to use when obtaining your token is ^b^%s^r^", Trello.APP_DEV_KEY);
            Output.print("  For example, to give wrk read/write access; use url:");
            Output.print("    ^b^https://trello.com/1/authorize?key=%s&name=Wrk&expiration=never&response_type=token&scope=read,write^r^", Trello.APP_DEV_KEY);
            Output.print("  See ^b^https://trello.com/docs/gettingstarted/index.html#getting-a-token-from-a-user^r^ for more information.");
            System.exit(1);
        }
    }

}
