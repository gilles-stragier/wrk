package net.ocheyedan.wrk.cmd.trello;

import net.ocheyedan.wrk.ApplicationContext;
import net.ocheyedan.wrk.cmd.Args;
import net.ocheyedan.wrk.domain.cards.search.*;
import net.ocheyedan.wrk.output.Output;
import net.ocheyedan.wrk.trello.Card;
import net.ocheyedan.wrk.trello.Label;
import net.ocheyedan.wrk.trello.Trello;

import java.util.List;

import static net.ocheyedan.wrk.trello.TrelloObject.Type.BOARD;
import static net.ocheyedan.wrk.trello.TrelloObject.Type.LIST;

/**
 * User: blangel
 * Date: 6/30/12
 * Time: 6:51 AM
 */
public final class Cards extends IdCommand {

    public Cards(Args args, ApplicationContext applicationContext) {
        super(args, applicationContext);
        this.searchCards = applicationContext.searchCards;

    }

    private final SearchCards searchCards;

    public CardsQuery buildFromArgs() {
        if (argsContainsInSomething(args)) {
            String anIdOfSomething = args.args.get(1);
            TrelloId id = parseWrkId(anIdOfSomething, BOARD, LIST);

            if (id.getType() == BOARD) {
                return new CardsOfABoard(id);
            } else {
                return new CardsOfAList(id);
            }
        } else {
            return new AllMyCards();
        }
    }

    public class DescriptionCardsQueryVisitor implements CardsQueryVisitor {
        private String description;

        @Override
        public void visit(AllMyCards allMyCards) {
            this.description = "Open cards assigned to you:";
        }

        @Override
        public void visit(CardsOfABoard cardsOfABoard) {
            this.description = String.format("Open cards for board ^b^%s^r^:", cardsOfABoard.getTrelloId());
        }

        @Override
        public void visit(CardsOfAList cardsOfAList) {
            this.description = String.format("Open cards for list ^b^%s^r^:", cardsOfAList.getTrelloId());
        }

        public String getDescription() {
            return description;
        }
    }

    public String computeUrl(Args args, Cards cards) {
        if (cards.argsContainsInSomething(args)) {
            IdCommand.LegacyTrelloId id = cards.parseWrkId(args.args.get(1), IdCommand.boardsListsPrefix);
            if (id.idWithTypePrefix.startsWith("b:")) {
                String boardId = id.idWithTypePrefix.substring(2);
                return Trello.url("https://trello.com/1/boards/%s/cards?filter=open&key=%s&token=%s", boardId,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
            } else if (id.idWithTypePrefix.startsWith("l:")) {
                String listId = id.idWithTypePrefix.substring(2);
                return Trello.url("https://trello.com/1/lists/%s/cards?filter=open&key=%s&token=%s", listId,
                        Trello.APP_DEV_KEY, Trello.USR_TOKEN);
            } else {
                return null;
            }
        } else if (args.args.isEmpty()) {
            return Trello.url("https://trello.com/1/members/my/cards?filter=open&key=%s&token=%s", Trello.APP_DEV_KEY,
                    Trello.USR_TOKEN);
        } else {
            return null;
        }
    }

    public boolean argsContainsInSomething(Args args) {
        return (args.args.size() == 2) && "in".equals(args.args.get(0));
    }

    @Override
    protected void _run() {
        CardsQuery cardsQuery = buildFromArgs();
        DescriptionCardsQueryVisitor descriptionVisitor = new DescriptionCardsQueryVisitor();
        cardsQuery.accept(descriptionVisitor);
        Output.print(descriptionVisitor.description);
        String url = computeUrl(args, this);

        List<Card> cards = applicationContext.restTemplate.get(url, applicationContext.typeReferences.cardListType);
        applicationContext.wrkIdsManager.registerTrelloIds(cards);
        applicationContext.outputter.printCards(cards, applicationContext.wrkIdsManager);

    }

    @Override protected boolean valid() {
        return argsContainsInSomething(args) || args.args.isEmpty();
    }

    @Override protected String getCommandName() {
        return "cards";
    }


    /**
     * The {@link Card#getUrl()} returns a url based off of board and card's short-id; translating to long-id so that
     * users can copy and paste id printed via url.  Additionally removing the card name from the url to shorten the
     * length of the resulting url.
     * @param card to get long url
     * @return the long url (using {@link Card#getId()} instead of {@link Card#getIdShort()}
     */
    public static String getPrettyUrl(Card card) {
        String originalUrl = card.getUrl();
        int firstIndex = originalUrl.indexOf("card/");
        if (firstIndex == -1) {
            return originalUrl; // balk
        }
        String toRemove = originalUrl.substring(firstIndex + 5);
        return originalUrl.replace(toRemove, card.getIdBoard() + "/" + card.getId());
    }

    public static String buildLabel(List<Label> labels) {
        StringBuilder buffer = new StringBuilder();
        boolean colored = Output.isColoredOutput();
        for (Label label : labels) {
            String name = ((label.getName() == null) || label.getName().isEmpty()
                    ? (colored ? "  " : "[" + label.getColor() + "]")
                    : " " + label.getName() + " ");
            if ("green".equals(label.getColor())) {
                buffer.append(String.format(" ^i^^green^%s^r^", name));
            } else if ("yellow".equals(label.getColor())) {
                buffer.append(String.format(" ^i^^yellow^%s^r^", name));
            } else if ("orange".equals(label.getColor())) {
                buffer.append(String.format(" ^orange^^i^%s^r^", name));
            } else if ("red".equals(label.getColor())) {
                buffer.append(String.format(" ^i^^red^%s^r^", name));
            } else if ("purple".equals(label.getColor())) {
                buffer.append(String.format(" ^i^^magenta^%s^r^", name));
            } else if ("blue".equals(label.getColor())) {
                buffer.append(String.format(" ^i^^blue^%s^r^", name));
            }
        }
        return buffer.toString();
    }
}
