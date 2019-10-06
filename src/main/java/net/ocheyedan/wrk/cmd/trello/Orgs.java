package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.Output;
import net.ocheyedan.wrk.RestTemplate;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.trello.Organization;
import net.ocheyedan.wrk.trello.Trello;
import com.fasterxml.jackson.core.type.TypeReference;

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
        if ((orgs == null) || orgs.isEmpty()) {
            Output.print("  ^black^None^r^");
            return Collections.emptyMap();
        }
        return printOrgs(orgs, 1);
    }

    @Override protected boolean valid() {
        return (url != null);
    }

    @Override protected String getCommandName() {
        return "orgs";
    }

    static Map<String, String> printOrgs(List<Organization> orgs, int indexBase) {
        Map<String, String> wrkIds = new HashMap<String, String>(orgs.size());
        int orgIndex = indexBase;
        for (Organization organization : orgs) {
            String wrkId = "wrk" + orgIndex++;
            wrkIds.put(wrkId, String.format("o:%s", organization.getId()));

            Output.print("  ^b^%s^r^ ^black^| %s^r^ | %s", organization.getDisplayName(), wrkId, organization.getId());
            Output.print("    ^black^%s^r^", organization.getUrl());
        }
        return wrkIds;
    }

}
