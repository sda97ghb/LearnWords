package com.divanoapps.learnwords;

import com.divanoapps.learnwords.entities.Card;
import com.divanoapps.learnwords.entities.Deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by dmitry on 09.10.17.
 */

public class CardRetriever {
    public enum Order {
        alphabetical,
        file,
        random,
        higher30,
        lower30
    }

    private List<Card> mCards;

    private RetrieveOrder mRetrieveOrder;

    public CardRetriever(Order order, Deck deck) {
        mCards = deck.getCardsAsList();

        switch (order) {
            case alphabetical: mRetrieveOrder = new AlphabeticalRetrieveOrder(); break;
            case file:         mRetrieveOrder = new FileRetrieveOrder();         break;
            case random:       mRetrieveOrder = new RandomRetrieveOrder();       break;
            
            default:           mRetrieveOrder = new FileRetrieveOrder();         break;
        }
    }

    public Card getNext() {
        return mRetrieveOrder.getNext();
    }

    private interface RetrieveOrder {
        Card getNext();
    }

    private class AlphabeticalRetrieveOrder implements RetrieveOrder {
        List<Card> mSortedCards;
        private int mCurrentCardIndex = -1;

        @Override
        public Card getNext() {
            if (mSortedCards == null) {
                mSortedCards = new LinkedList<>(mCards);
                Collections.sort(mSortedCards, (o1, o2) -> o1.getWord().compareTo(o2.getWord()));
            }

            ++ mCurrentCardIndex;
            if (mCurrentCardIndex >= mSortedCards.size())
                return null;
            else
                return mSortedCards.get(mCurrentCardIndex);
        }
    }

    private class FileRetrieveOrder implements RetrieveOrder {
        private int mCurrentCardIndex = -1;

        @Override
        public Card getNext() {
            ++ mCurrentCardIndex;
            if (mCurrentCardIndex >= mCards.size())
                return null;
            else
                return mCards.get(mCurrentCardIndex);
        }
    }

    private class RandomRetrieveOrder implements RetrieveOrder {
        @Override
        public Card getNext() {
            if (mCards.isEmpty())
                return null;

            List<Integer> sums = new ArrayList<>(mCards.size() + 1);
            sums.set(0, 0);
            for (int i = 1; i < mCards.size(); ++ i)
                sums.set(i, sums.get(i - 1) + mCards.get(i).getDifficulty());

            int sum = sums.get(sums.size() - 1);
            int randN = ThreadLocalRandom.current().nextInt(0, sum);

            int index = 0;
            while (sums.get(index) < randN)
                ++ index;

            return mCards.get(index);
        }
    }
}
