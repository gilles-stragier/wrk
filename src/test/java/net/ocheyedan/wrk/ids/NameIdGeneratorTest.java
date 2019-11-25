package net.ocheyedan.wrk.ids;

import net.ocheyedan.wrk.TestData;
import net.ocheyedan.wrk.trello.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

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

        IdMapping result = nameIdGenerator.generate(Collections.emptySet(), sample);

        Assertions.assertTrue(result.getAliases().iterator().next().getId().startsWith("payer-les-2-factures-de-lucm"));
    }
}