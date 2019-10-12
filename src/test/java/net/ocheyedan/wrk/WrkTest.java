package net.ocheyedan.wrk;

import com.fasterxml.jackson.core.type.TypeReference;
import net.ocheyedan.wrk.cmd.TypeReferences;
import net.ocheyedan.wrk.cmd.trello.TrelloId;
import net.ocheyedan.wrk.ids.IdMapping;
import net.ocheyedan.wrk.ids.IdsAliasingManager;
import net.ocheyedan.wrk.ids.SequentiaByTypelIdGenerator;
import net.ocheyedan.wrk.output.DefaultOutputter;
import net.ocheyedan.wrk.trello.SearchResult;
import net.ocheyedan.wrk.trello.TrelloObject;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WrkTest {

    private ApplicationContext applicationContext;
    private Wrk wrk;
    private TestData testData;

    private PrintStream stdout;
    private ByteArrayOutputStream baos;

    @BeforeEach
    void setUp() throws IOException {

        System.setProperty("wrk.trello.usr.token", "fakeToken");
        System.setProperty("wrk.trello.usr.key", "fakeKey");
        System.setProperty("wrk.editor", "/usr/bin/vim");
        System.setProperty("user.home", "./build/");

        applicationContext = new ApplicationContext(
                Mockito.mock(RestTemplate.class),
                new TypeReferences(),
                new DefaultOutputter(),
                new IdsAliasingManager()
        );

        wrk = new Wrk(
                applicationContext
        );

        testData = new TestData();


        File wrkDir = new File("build/.wrk");
        if (wrkDir.exists()) {
            FileUtils.deleteDirectory(wrkDir);
        }
        wrkDir.mkdir();

        baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        stdout = System.out;
        System.setOut(ps);
    }

    @AfterEach
    void tearDown() {
        System.out.flush();
        System.setOut(stdout);
        System.out.println(baos.toString());

        Mockito.verifyNoMoreInteractions(applicationContext.restTemplate);

        File wrkDir = new File("build/.wrk/wrk-ids");
        if (wrkDir.exists()) {
            FileUtils.deleteQuietly(wrkDir);
        }
    }

    @Test
    void testAssignCards() {
        when(
                applicationContext.restTemplate.post(
                        "https://trello.com/1/cards/c1/members?value=m1&key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.memberListType
                )).thenReturn(
                singletonList(
                        testData.sampleMember()
                )
        );

        wrk.execute(new String[]{"assign", "m1", "to", "c1"});

        Assertions.assertEquals(
                "Assigning user m1 to card c1:\n" +
                        "  Added!\n",
                getStdout()
        );
    }

    @Test
    void testAssignCardsToMe() {
        when(
                applicationContext.restTemplate.post(
                        "https://trello.com/1/cards/c1/members?value=themember&key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.memberListType
                )).thenReturn(
                singletonList(
                        testData.sampleMember()
                )
        );
        Map<String, String> map = new HashMap<>();
        map.put("id", "themember");

        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/members/my?fields=initials&key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.mapType
                )).thenReturn(map);

        wrk.execute(new String[]{"assign", "c1"});

        Assertions.assertEquals(
                "Assigning user to card c1:\n" +
                        "  Added!\n",
                getStdout()
        );
    }

    @Test
    void testAssignedCards() {
        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/members/my/cards?filter=open&key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.cardListType
                )).thenReturn(
                singletonList(
                        testData.sampleCard()
                )
        );

        wrk.execute(new String[]{});

        Assertions.assertEquals(
                "Open cards assigned to you:\n" +
                        "  somename  somelabel  | wrk1 | 123\n" +
                        "    http://Someurl\n",
                getStdout()
        );
    }

    @Test
    void testBoards() {
        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/members/my/boards?filter=open&key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.boardListType
                )).thenReturn(
                singletonList(
                        testData.sampleBoard()
                )
        );

        wrk.execute(new String[]{"boards"});

        Assertions.assertEquals(
                "Open boards you've created:\n" +
                        "  boardname | wrk1 | 456\n" +
                        "    http://boardurl\n",
                getStdout()
        );

    }

    @Test
    void testLists() {
        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/boards/456/lists?filter=open&key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.listsListType
                )).thenReturn(
                singletonList(
                        testData.sampleList()
                )
        );

        wrk.execute(new String[]{"lists", "in", "456"});

        Assertions.assertEquals(
                "Open lists for board 456:\n" +
                        "  listname | wrk1 | 789\n",
                getStdout()
        );
    }

    @Test
    void testListsAndCustomIds() {
        IdsAliasingManager wrkIdsManager = new IdsAliasingManager(
                new SequentiaByTypelIdGenerator()
        );
        applicationContext = new ApplicationContext(
                Mockito.mock(RestTemplate.class),
                new TypeReferences(),
                new DefaultOutputter(),
                wrkIdsManager
        );

        wrk = new Wrk(
                applicationContext
        );

        wrkIdsManager.registerTrelloIds(Collections.singletonList(
                testData.sampleBoard()
        ));

        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/boards/456/lists?filter=open&key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.listsListType
                )).thenReturn(
                singletonList(
                        testData.sampleList()
                )
        );

        wrk.execute(new String[]{"lists", "in", "b1"});

        Assertions.assertEquals(
                "Open lists for board 456:\n" +
                        "  listname | l1 | 789\n",
                getStdout()
        );
    }

    @Test
    void testMembers() {
        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/organizations/456/members?key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.memberListType
                )).thenReturn(
                singletonList(
                        testData.sampleMember()
                )
        );

        wrk.execute(new String[]{"members", "in", "o:456"});

        Assertions.assertEquals(
                "Members of organization 456:\n" +
                        "  Some Name Full | wrk1\n" +
                        "    username somename\n",
                getStdout()
        );
    }


    @Test
    void testOrgs() {
        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/members/my/organizations?key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.orgsListType
                )).thenReturn(
                singletonList(
                        testData.sampleOrganization()
                )
        );

        wrk.execute(new String[]{"orgs"});

        Assertions.assertEquals(
                "Your organizations:\n" +
                        "  displayOrg | wrk1 | abc\n" +
                        "    http://someorgs\n",
                getStdout()
        );
    }

    @Test
    void testCreateList() {
        when(
                applicationContext.restTemplate.post(
                        "https://trello.com/1/lists?name=Nom+de+la+liste&idBoard=34567&key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.listType
                )).thenReturn(testData.sampleList()
        );

        wrk.execute(new String[]{"create", "list", "in", "34567", "Nom de la liste"});

        Assertions.assertEquals(
                "Creating list in board 34567:\n" +
                        "  listname | wrk1 | 789\n",
                getStdout()
        );
    }

    @Test
    void testCreateCard() {
        when(
                applicationContext.restTemplate.post(
                        "https://trello.com/1/cards?name=Name+of+a+card&idList=34567&key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.cardType
                )).thenReturn(testData.sampleCard()
        );

        wrk.execute(new String[]{"create", "card", "in", "34567", "Name of a card"});

        Assertions.assertEquals(
                "Creating card in list 34567:\n" +
                        "  somename  somelabel  | wrk1 | 123\n" +
                        "    http://Someurl\n",
                getStdout()
        );
    }

    @Test
    void testDescCard() {
        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/cards/123?key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.cardType
                )).thenReturn(testData.sampleCard());

        wrk.execute(new String[]{"desc", "c:123"});

        Assertions.assertEquals(
                "Description of card 123:\n" +
                        "  somename  somelabel  | 123\n" +
                        "    somedesc\n" +
                        "    http://Someurl\n",
                getStdout()
        );
    }

    @Test
    void testDescMember() {
        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/members/123?key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.memberType
                )).thenReturn(testData.sampleMember());

        wrk.execute(new String[]{"desc", "m:123"});

        Assertions.assertEquals(
                "Description of member 123:\n" +
                        "  Some Name Full | 637\n" +
                        "    username somename\n",
                getStdout()
        );
    }

    @Test
    void testDescOrg() {
        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/organizations/123?key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.orgType
                )).thenReturn(testData.sampleOrganization());

        wrk.execute(new String[]{"desc", "o:123"});

        Assertions.assertEquals(
                "Description of organization 123:\n" +
                        "  displayOrg | abc\n" +
                        "    somedesc\n" +
                        "    http://someorgs\n",
                getStdout()
        );
    }

    @Test
    void testDescBoard() {
        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/boards/123?key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.boardType
                )).thenReturn(testData.sampleBoard());

        wrk.execute(new String[]{"desc", "b:123"});

        Assertions.assertEquals(
                "Description of board 123:\n" +
                        "  boardname | 456\n" +
                        "    boarddesc\n" +
                        "    http://boardurl\n",
                getStdout()
        );
    }

    @Test
    void testDescList() {
        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/lists/123?key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.listType
                )).thenReturn(testData.sampleList());

        wrk.execute(new String[]{"desc", "l:123"});

        Assertions.assertEquals(
                "Description of list 123:\n" +
                        "  listname | 789\n",
                getStdout()
        );
    }

    @Test
    void testSearchCard() {
        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/search?query=keyword&modelTypes=cards&cards_limit=1000&key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.searchType
                )).thenReturn(
                new SearchResult(
                        null,
                        null,
                        singletonList(testData.sampleCard()),
                        null,
                        null,
                        null
                )
        );

        wrk.execute(new String[]{"search", "cards", "keyword"});

        Assertions.assertEquals(
                "Searching cards for keyword\n" +
                        "Found 1 card.\n" +
                        "  somename  somelabel  | wrk1 | 123\n" +
                        "    http://Someurl\n",
                getStdout()
        );


        IdMapping onlyOne = readWrkIds().iterator().next();
        Assertions.assertEquals(new TrelloId("123", TrelloObject.Type.CARD), onlyOne.getTrelloId());
        Assertions.assertEquals("wrk1", onlyOne.oneAlias().getId());
    }

    @Test
    void testSearchBoard() {
        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/search?query=keyword&modelTypes=boards&board_fields=name,url&boards_limit=1000&key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.searchType
                )).thenReturn(
                new SearchResult(
                        null,
                        singletonList(testData.sampleBoard()),
                        null,
                        null,
                        null,
                        null
                )
        );

        wrk.execute(new String[]{"search", "boards", "keyword"});

        Assertions.assertEquals(
                "Searching boards for keyword\n" +
                        "Found 1 board.\n" +
                        "  boardname | wrk1 | 456\n" +
                        "    http://boardurl\n",
                getStdout()
        );


        IdMapping onlyOne = readWrkIds().iterator().next();
        Assertions.assertEquals(new TrelloId("456", TrelloObject.Type.BOARD), onlyOne.getTrelloId());
        Assertions.assertEquals("wrk1", onlyOne.oneAlias().getId());
    }

    @Test
    void testSearchOrganizations() {

        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/search?query=keyword&modelTypes=organizations&organizations_limit=1000&key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.searchType
                )).thenReturn(
                new SearchResult(
                        null,
                        null,
                        null,
                        null,
                        singletonList(testData.sampleOrganization()),
                        null
                )
        );

        wrk.execute(new String[]{"search", "orgs", "keyword"});

        Assertions.assertEquals(
                "Searching organizations for keyword\n" +
                        "Found 1 organization.\n" +
                        "  displayOrg | wrk1 | abc\n" +
                        "    http://someorgs\n",
                getStdout()
        );

        IdMapping onlyOne = readWrkIds().iterator().next();
        Assertions.assertEquals(new TrelloId("abc", TrelloObject.Type.ORG), onlyOne.getTrelloId());
        Assertions.assertEquals("wrk1", onlyOne.oneAlias().getId());

    }

    private Set<IdMapping> readWrkIds() {
        try {
            return Json.mapper().readValue(new File("./build/.wrk/wrk-ids"), new TypeReference<Set<IdMapping>>() {
            });
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected exception !", e);
        }
    }

    @Test
    void testSearchMembers() {
        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/search?query=keyword&modelTypes=members&members_limit=1000&key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.searchType
                )).thenReturn(
                new SearchResult(
                        null,
                        null,
                        null,
                        null,
                        null,
                        singletonList(testData.sampleMember())
                )
        );

        wrk.execute(new String[]{"search", "members", "keyword"});

        Assertions.assertEquals(
                "Searching members for keyword\n" +
                        "Found 1 member.\n" +
                        "  Some Name Full | wrk1\n" +
                        "    username somename\n",
                getStdout()
        );


        IdMapping onlyOne = readWrkIds().iterator().next();
        Assertions.assertEquals(new TrelloId("637", TrelloObject.Type.MEMBER), onlyOne.getTrelloId());
        Assertions.assertEquals("wrk1", onlyOne.oneAlias().getId());    }


    @Test
    void testSearchCombined() {
        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/search?query=keyword&board_fields=name,url&boards_limit=1000&cards_limit=1000&organizations_limit=1000&members_limit=1000&key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.searchType
                )).thenReturn(
                new SearchResult(
                        null,
                        singletonList(testData.sampleBoard()),
                        singletonList(testData.sampleCard()),
                        singletonList(testData.sampleAction()),
                        singletonList(testData.sampleOrganization()),
                        singletonList(testData.sampleMember())
                )
        );

        wrk.execute(new String[]{"search", "keyword"});

        Assertions.assertEquals(
                "Searching for keyword\n" +
                        "Found 1 organization.\n" +
                        "  displayOrg | wrk1 | abc\n" +
                        "    http://someorgs\n" +
                        "Found 1 board.\n" +
                        "  boardname | wrk2 | 456\n" +
                        "    http://boardurl\n" +
                        "Found 1 card.\n" +
                        "  somename  somelabel  | wrk3 | 123\n" +
                        "    http://Someurl\n" +
                        "Found 1 member.\n" +
                        "  Some Name Full | wrk4\n" +
                        "    username somename\n",
                getStdout()
        );




        Set<IdMapping> wrkIds = readWrkIds();

        Assertions.assertTrue(wrkIds.stream().anyMatch(
                m -> m.getTrelloId().equals(new TrelloId("637", TrelloObject.Type.MEMBER)) && m.contains("wrk4"))
        );
        Assertions.assertTrue(wrkIds.stream().anyMatch(
                m -> m.getTrelloId().equals(new TrelloId("123", TrelloObject.Type.CARD)) && m.contains("wrk3"))
        );
        Assertions.assertTrue(wrkIds.stream().anyMatch(
                m -> m.getTrelloId().equals(new TrelloId("456", TrelloObject.Type.BOARD)) && m.contains("wrk2"))
        );
        Assertions.assertTrue(wrkIds.stream().anyMatch(
                m -> m.getTrelloId().equals(new TrelloId("abc", TrelloObject.Type.ORG)) && m.contains("wrk1"))
        );
    }

    private String getStdout() {
        System.out.flush();
        return baos.toString();
    }

}