package net.ocheyedan.wrk.ids;

import net.ocheyedan.wrk.TestData;
import net.ocheyedan.wrk.trello.Board;
import net.ocheyedan.wrk.trello.TrelloObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class NameIdGeneratorTest {

    private NameIdGenerator nameIdGenerator;

    private TestData testData;

    @BeforeEach
    void setUp() {
        this.nameIdGenerator = new NameIdGenerator();
        this.testData = new TestData();
    }

    @Test
    void generate() {
        Board sample = testData.sampleBoard("Payer les 2 factures de l'UCM");

        String result = nameIdGenerator.generate(Collections.emptyList(), sample);

        Assertions.assertTrue(result.startsWith("payer-les-2-factures-de-lucm"));
    }
}