package net.ocheyedan.wrk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WrkTest {

    @Test
    public void testAssignedCards() {
        System.setProperty("wrk.trello.usr.token", "fakeToken");

        Wrk.main(new String[]{});
    }

}