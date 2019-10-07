package net.ocheyedan.wrk.cmd;

import com.fasterxml.jackson.core.type.TypeReference;
import net.ocheyedan.wrk.trello.*;

import java.util.List;

public class TypeReferences {
    public TypeReference<List<Card>> cardListType = new TypeReference<>() {};

    public TypeReference<Card> cardType = new TypeReference<>() {
    };

    public TypeReference<List<Board>> boardListType = new TypeReference<>() {};

    public TypeReference<List<net.ocheyedan.wrk.trello.List>> listsListType = new TypeReference<>() {};

    public TypeReference<net.ocheyedan.wrk.trello.List> listType = new TypeReference<>() {};

    public TypeReference<List<Organization>> orgsListType = new TypeReference<>() {};

    public TypeReference<SearchResult> searchType = new TypeReference<>() {};

    public TypeReference<Board> boardType = new TypeReference<>() {};
    public TypeReference<Member> memberType = new TypeReference<>() {};
    public TypeReference<Organization> orgType = new TypeReference<>() {};
}
