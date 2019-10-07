package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.ids.WrkIdsManager;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.Organization;
import net.ocheyedan.wrk.trello.Trello;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: blangel
 * Date: 6/30/12
 * Time: 8:19 AM
 */
public final class Orgs extends IdCommand {

    private final String url;

    private final String description;

    public Orgs(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
        url = Trello.url("https://trello.com/1/members/my/organizations?key=%s&token=%s", Trello.APP_DEV_KEY,
                Trello.USR_TOKEN);
        description = "Your organizations:";
    }

    @Override protected Map<String, String> _run() {
        Output.print(description);
        List<Organization> orgs = applicationContext.restTemplate.get(url, applicationContext.typeReferences.orgsListType);
        WrkIdsManager idsManager = new WrkIdsManager();
        idsManager.registerTrelloIds(orgs);
        applicationContext.defaultOutputter.printOrgs(orgs, idsManager);
        return idsManager.idsMap();
    }

    @Override protected boolean valid() {
        return (url != null);
    }

    @Override protected String getCommandName() {
        return "orgs";
    }


}
