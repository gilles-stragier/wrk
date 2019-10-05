package net.ocheyedan.wrk;

import net.ocheyedan.wrk.trello.Board;
import net.ocheyedan.wrk.trello.Card;
import net.ocheyedan.wrk.trello.Label;
import net.ocheyedan.wrk.trello.List;

import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.emptyList;

public class TestData {

    Board sampleBoard() {
        return new Board(
                "456",
                "boardname",
                "boarddesc",
                false,
                "orgid",
                false,
                false,
                "http://boardurl",
                null,
                null,
                null,
                null
        );
    }

    Card sampleCard() {
        return new Card(
                "123",
                false,
                "somedesc",
                "bid",
                emptyList(),
                "lid",
                Collections.emptyList(),
                0,
                Arrays.asList(new Label("red", "somelabel")),
                "somename",
                2,
                "http://Someurl",
                null,
                false
        );
    }

    List sampleList() {
        return new List(
            "789",
            "listname",
            false,
            "456",
            1
        );
    }
}
