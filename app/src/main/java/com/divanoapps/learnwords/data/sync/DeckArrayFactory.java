package com.divanoapps.learnwords.data.sync;

import com.divanoapps.learnwords.data.local.Deck;
import com.divanoapps.learnwords.data.wombat.ArrayFactory;

/**
 * Produces array of Decks.
 */
public class DeckArrayFactory implements ArrayFactory<Deck> {
    /**
     * Produces array of size {@code size}.
     * @param size Size of array
     * @return Array of decks.
     */
    @Override
    public Deck[] createArray(int size) {
        return new Deck[size];
    }

    /**
     * Produces array of single deck.
     * @param deck Element of the created array.
     * @return Array of single deck.
     */
    @Override
    public Deck[] singleton(Deck deck) {
        return new Deck[] { deck };
    }
}
