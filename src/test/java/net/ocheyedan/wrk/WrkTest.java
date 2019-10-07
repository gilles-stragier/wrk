package net.ocheyedan.wrk;

import com.fasterxml.jackson.core.type.TypeReference;
import net.ocheyedan.wrk.cmd.TypeReferences;
import net.ocheyedan.wrk.output.DefaultOutputter;
import net.ocheyedan.wrk.trello.SearchResult;
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

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
        applicationContext = new ApplicationContext(
                Mockito.mock(RestTemplate.class),
                new TypeReferences(),
                new DefaultOutputter()
        );

        wrk = new Wrk(
                applicationContext
        );

        testData = new TestData();

        System.setProperty("wrk.trello.usr.token", "fakeToken");
        System.setProperty("wrk.trello.usr.key", "fakeKey");
        System.setProperty("wrk.editor", "/usr/bin/vim");
        System.setProperty("user.home", "./build/");

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


        LinkedList<Map<String, String>> wrkIds = readWrkIds();
        Assertions.assertEquals("c:123", wrkIds.get(0).values().iterator().next());
        Assertions.assertEquals("wrk1", wrkIds.get(0).keySet().iterator().next());
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


        LinkedList<Map<String, String>> wrkIds = readWrkIds();
        Assertions.assertEquals("b:456", wrkIds.get(0).values().iterator().next());
        Assertions.assertEquals("wrk1", wrkIds.get(0).keySet().iterator().next());
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

        LinkedList<Map<String, String>> wrkIds = readWrkIds();
        Assertions.assertEquals("o:abc", wrkIds.get(0).values().iterator().next());
        Assertions.assertEquals("wrk1", wrkIds.get(0).keySet().iterator().next());

    }

    private LinkedList<Map<String, String>> readWrkIds() {
        try {
            return Json.mapper().readValue(new File("./build/.wrk/wrk-ids"), new TypeReference<LinkedList<Map<String, String>>>() {
            });
        } catch (IOException e) {
            return null;
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


        LinkedList<Map<String, String>> wrkIds = readWrkIds();
        Assertions.assertEquals("m:637", wrkIds.get(0).values().iterator().next());
        Assertions.assertEquals("wrk1", wrkIds.get(0).keySet().iterator().next());
    }


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


        LinkedList<Map<String, String>> wrkIds = readWrkIds();
        Map<String, String> idsMap = wrkIds.get(0);
        Iterator<String> trelloIds = idsMap.values().iterator();
        Assertions.assertEquals("m:637", trelloIds.next());
        Assertions.assertEquals("c:123", trelloIds.next());
        Assertions.assertEquals("b:456", trelloIds.next());
        Assertions.assertEquals("o:abc", trelloIds.next());

        Iterator<String> internalIds = idsMap.keySet().iterator();
        Assertions.assertEquals("wrk4", internalIds.next());
        Assertions.assertEquals("wrk3", internalIds.next());
        Assertions.assertEquals("wrk2", internalIds.next());
        Assertions.assertEquals("wrk1", internalIds.next());
    }

    private String getStdout() {
        System.out.flush();
        return baos.toString();
    }

}