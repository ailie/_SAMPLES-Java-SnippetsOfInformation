package interviewQuestions.cardDeck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public final class Standard52CardDeck {

    public static enum Card {

        CACE, C2, C3, C4, C5, C6, C7, C8, C9, C10, CJACK, CQUEEN, CKING,

        DACE, D2, D3, D4, D5, D6, D7, D8, D9, D10, DJACK, DQUEEN, DKING,

        HACE, H2, H3, H4, H5, H6, H7, H8, H9, H10, HJACK, HQUEEN, HKING,

        SACE, S2, S3, S4, S5, S6, S7, S8, S9, S10, SJACK, SQUEEN, SKING,
    }

    public static void main(String[] args) {
        Standard52CardDeck deck = new Standard52CardDeck();
        deck.fAvailableCards.forEach(card -> {
            System.out.print(card + " ");
        });
        System.out.println();
        deck.shuffle().fAvailableCards.forEach(card -> {
            System.out.print(card + " ");
        });
    }

    {
        fAvailableCards = new LinkedHashSet<>();
        reset();
    }

    /** an ORDERED SET holding fresh (ie not yet played) cards */
    final private Set<Card> fAvailableCards;

    public void reset() {
        // fAvailableCards.clear();
        fAvailableCards.addAll(Arrays.asList(Card.values()));
    }

    /**
     * It has been shown that because of the large number of possibilities from
     * shuffling a 52 card deck, it is probable that no two fair card shuffles have ever
     * yielded exactly the same order of cards. As a comparison to how huge the number
     * of possibilities is, to finish sorting all combinations starting from the Big
     * Bang at planck speed would make the universe roughly ten million times older than
     * its current age.
     */
    public Standard52CardDeck shuffle() {

        List<Card> tmpList = new ArrayList<>(fAvailableCards);

        fAvailableCards.clear();

        final Random rndGenerator = new Random();

        for (int remainingElementsCount = tmpList.size(); remainingElementsCount > 0; remainingElementsCount--) {
            int rndIndex = rndGenerator.nextInt(remainingElementsCount);
            fAvailableCards.add(tmpList.remove(rndIndex));
        }

        return this;
    }
}
