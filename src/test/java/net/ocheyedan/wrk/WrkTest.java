package net.ocheyedan.wrk;

import net.ocheyedan.wrk.cmd.TypeReferences;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WrkTest {

    private ApplicationContext applicationContext;
    private Wrk wrk;
    private TestData testData;

    private PrintStream stdout;
    private ByteArrayOutputStream baos;

    @BeforeEach
    void setUp() {
        applicationContext = new ApplicationContext(
                Mockito.mock(RestTemplate.class),
                new TypeReferences()
        );

        wrk = new Wrk(
                applicationContext
        );

        testData = new TestData();

        System.setProperty("wrk.trello.usr.token", "fakeToken");
        System.setProperty("wrk.trello.usr.key", "fakeKey");
        System.setProperty("wrk.editor", "/usr/bin/vim");


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
    public void testAssignedCards() {
        when(
            applicationContext.restTemplate.get(
                "https://trello.com/1/members/my/cards?filter=open&key=fakeKey&token=fakeToken",
                applicationContext.typeReferences.cardListType
            )).thenReturn(
                asList(
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
    public void testBoards() {
        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/members/my/boards?filter=open&key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.boardListType
                )).thenReturn(
                asList(
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
    public void testLists() {
        when(
                applicationContext.restTemplate.get(
                        "https://trello.com/1/boards/456/lists?filter=open&key=fakeKey&token=fakeToken",
                        applicationContext.typeReferences.listsListType
                )).thenReturn(
                asList(
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


    private String getStdout() {
        System.out.flush();
        return baos.toString();
    }

}