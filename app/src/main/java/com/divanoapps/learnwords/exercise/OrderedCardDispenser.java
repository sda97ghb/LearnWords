package com.divanoapps.learnwords.exercise;

import com.divanoapps.learnwords.data.local.Card;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmitry on 06.05.18.
 */

abstract class OrderedCardDispenser implements CardDispenser {
    private Iterator<Card> iterator;

    OrderedCardDispenser(List<Card> cards) {
        List<Card> cards1 = new LinkedList<>();
        for (Card card : cards)
            if (card != null && !card.isHidden())
                cards1.add(card);
        Collections.sort(cards1, this::compareCards);
        iterator = cards1.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Card getNext() {
        return iterator.next();
    }

    protected abstract int compareCards(Card card1, Card card2);
}
