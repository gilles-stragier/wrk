package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.cmd.Args;

/**
 * User: blangel
 * Date: 6/30/12
 * Time: 4:19 PM
 */
public final class Pop extends IdCommand {

    private final Integer times;

    public Pop(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
        if (args.args.size() > 0) {
            if ("all".equals(args.args.get(0))) {
                times = Integer.MAX_VALUE;
            } else {
                times = parse(args.args.get(0));
            }
        } else {
            times = 1;
        }
    }

    private Integer parse(String arg) {
        try {
            int inputted = Integer.parseInt(arg);
            inputted = Math.min(inputted, 50);
            return Math.max(inputted, 1);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void _run() {
        pop(times);
    }

    @Override protected boolean valid() {
        return (times != null);
    }

    @Override protected String getCommandName() {
        return "pop";
    }
}
