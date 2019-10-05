package net.ocheyedan.wrk.cmd;

import com.fasterxml.jackson.core.type.TypeReference;
import net.ocheyedan.wrk.trello.Board;
import net.ocheyedan.wrk.trello.Card;

import java.util.List;

public class TypeReferences {
    public TypeReference<List<Card>> cardListType = new TypeReference<>() {};

    public TypeReference<List<Board>> boardListType = new TypeReference<>() {};

    public TypeReference<List<net.ocheyedan.wrk.trello.List>> listsListType = new TypeReference<>() {};


}
