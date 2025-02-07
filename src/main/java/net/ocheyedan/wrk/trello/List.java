package net.ocheyedan.wrk.trello;

import static net.ocheyedan.wrk.trello.TrelloObject.Type.LIST;

/**
 * User: blangel
 * Date: 7/1/12
 * Time: 10:11 AM
 *
 * Representation of a {@literal Trello} list object.
 *
 * {
 *  id: 4fee0d37ca6ff8287a018e01,
 *  name: "To Do",
 *  closed: false,
 *  idBoard: "4fee0d37ca6ff8287a018e00",
 *  pos: 16384
 * }
 */
public final class List implements TrelloObject {

    private final String id;

    private final String name;

    private final Boolean closed;

    private final String idBoard;

    private final Integer pos;

    private List() {
        this(null, null, null, null, null);
    }

    public List(String id, String name, Boolean closed, String idBoard, Integer pos) {
        this.id = id;
        this.name = name;
        this.closed = closed;
        this.idBoard = idBoard;
        this.pos = pos;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getClosed() {
        return closed;
    }

    public String getIdBoard() {
        return idBoard;
    }

    public Integer getPos() {
        return pos;
    }

    @Override
    public Type type() {
        return LIST;
    }

    @Override
    public String name() {
        return getName();
    }
}
