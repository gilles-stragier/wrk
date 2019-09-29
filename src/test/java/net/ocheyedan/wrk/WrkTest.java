package net.ocheyedan.wrk;

import com.fasterxml.jackson.core.type.TypeReference;
import net.ocheyedan.wrk.cmd.TypeReferences;
import net.ocheyedan.wrk.trello.Badge;
import net.ocheyedan.wrk.trello.Card;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WrkTest {

    private ApplicationContext applicationContext;
    private Wrk wrk;

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
    }

    @Test
    public void testAssignedCards() {
        when(
            applicationContext.restTemplate.get(
                "https://trello.com/1/members/my/cards?filter=open&key=fakeKey&token=fakeToken",
                applicationContext.typeReferences.cardListType
            )).thenReturn(
                asList(
                        sampleCard()
                )
        );

        wrk.execute(new String[]{});

        String output = getStdout();
        Assertions.assertEquals(
                "Open cards assigned to you:\n" +
                        "  trululu | wrk1\n" +
                        "    http://Someurl\n",
                output
        );
    }

    private String getStdout() {
        System.out.flush();
        return baos.toString();
    }

    private Card sampleCard() {
        return new Card(
                "123",
                false,
                "somedesc",
                "bid",
                emptyList(),
                "lid",
                Collections.emptyList(),
                0,
                Collections.emptyList(),
                "trululu",
                2,
                "http://Someurl",
                null,
                false
        );
    }

}