package net.ocheyedan.wrk;

import net.ocheyedan.wrk.cmd.Command;
import net.ocheyedan.wrk.cmd.CommandLineParser;
import net.ocheyedan.wrk.cmd.TypeReferences;
import net.ocheyedan.wrk.cmd.trello.Boards2;
import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.ids.SequentiaByTypelIdGenerator;
import net.ocheyedan.wrk.output.DefaultOutputter;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.Trello;
import picocli.CommandLine;

@CommandLine.Command(
        name = "wrk",
        subcommands = {Boards2.class}
)
public class Wrk2 implements Runnable {

    public static void main(String[] args) {
        Config.init();
        Wrk.ensureTrelloToken();

        new CommandLine(new Wrk2()).execute(args);

    }

    public static ApplicationContext createApplicationContext() {
        return new ApplicationContext(
                new RestTemplate(),
                new TypeReferences(),
                new DefaultOutputter(),
                new IdsAliasingManager(
                        new SequentiaByTypelIdGenerator()
                )
        );
    }

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true)
    private boolean help;

    @Override
    public void run() {
        System.out.println("yihaa");
    }


}
