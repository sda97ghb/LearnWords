package com.divanoapps.learnwords.exercise;

import com.divanoapps.learnwords.data.local.Card;

import java.util.List;

/**
 * Created by dmitry on 06.05.18.
 */

public final class AlphabeticalCardDispenser extends OrderedCardDispenser {
    AlphabeticalCardDispenser(List<Card> cards) {
        super(cards);
    }

    @Override
    protected int compareCards(Card card1, Card card2) {
        return card1.getWord().compareTo(card2.getWord());
    }
}
