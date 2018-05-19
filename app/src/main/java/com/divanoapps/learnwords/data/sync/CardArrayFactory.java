package com.divanoapps.learnwords.data.sync;

import com.divanoapps.learnwords.data.local.Card;
import com.divanoapps.learnwords.data.wombat.ArrayFactory;

/**
 * Produces array of Cards.
 */
public class CardArrayFactory implements ArrayFactory<Card> {
    /**
     * Produces array of size {@code size}.
     * @param size Size of array
     * @return Array of cards.
     */
    @Override
    public Card[] createArray(int size) {
        return new Card[size];
    }

    /**
     * Produces array of single card.
     * @param card Element of the created array.
     * @return Array of single card.
     */
    @Override
    public Card[] singleton(Card card) {
        return new Card[] { card };
    }
}
