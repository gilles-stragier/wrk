package net.ocheyedan.wrk.ids;

import net.ocheyedan.wrk.TestData;
import net.ocheyedan.wrk.cmd.trello.TrelloId;
import net.ocheyedan.wrk.trello.TrelloObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class SequentiaByTypelIdGeneratorTest {

    private SequentiaByTypelIdGenerator sequentiaByTypelIdGenerator;
    private TestData testData;

    @BeforeEach
    void setUp() {
        this.sequentiaByTypelIdGenerator = new SequentiaByTypelIdGenerator();
        this.testData = new TestData();
    }

    @Test
    void generateAFirstCard() {
        Set<IdMapping> mappings = someMappings();

        IdMapping result = this.sequentiaByTypelIdGenerator.generate(
                mappings,
                testData.sampleCard()
        );

        Assertions.assertEquals("c1", result.oneAlias().getId());
    }

    @Test
    void generateASecondBoard() {
        Set<IdMapping> mappings = someMappings();

        IdMapping result = this.sequentiaByTypelIdGenerator.generate(
                mappings,
                testData.sampleBoard()
        );

        Assertions.assertEquals("b2", result.oneAlias().getId());
    }

    private HashSet<IdMapping> someMappings() {
        return new HashSet<>(Arrays.asList(
                IdMapping.initiliaze(
                        new TrelloId("132", TrelloObject.Type.MEMBER),
                        new Alias("m1")
                ),
                IdMapping.initiliaze(
                        new TrelloId("134322", TrelloObject.Type.BOARD),
                        new Alias ("b1")
                )
        ));
    }
}