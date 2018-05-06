package com.divanoapps.learnwords.exercise;

import com.divanoapps.learnwords.data.local.Card;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by dmitry on 06.05.18.
 */

public class RandomCardDispenser implements CardDispenser {
    private List<Card> cards;
    private List<Integer> sums;
    private int sum;

    RandomCardDispenser(List<Card> cards) {
        List<Card> cards1 = new LinkedList<>();
        for (Card card : cards)
            if (card != null && !card.isHidden())
                cards1.add(card);
        setCards(cards1);

        calculateSums();
    }

    private void setCards(List<Card> cards) {
        this.cards = cards;
    }

    private void calculateSums() {
        sums = new ArrayList<>(cards.size() + 1);
        sums.add(cards.get(0).getDifficulty());
        for (int i = 1; i < cards.size(); ++ i)
            sums.add(sums.get(i - 1) + cards.get(i).getDifficulty());

        sum = sums.get(sums.size() - 1);
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Card getNext() {
        if (cards.isEmpty())
            throw new NoSuchElementException("Empty card list");

        int randN = ThreadLocalRandom.current().nextInt(0, sum);

        int index = 0;
        while (index < cards.size() && sums.get(index) < randN)
            ++index;

        return cards.get(index);
    }
}
