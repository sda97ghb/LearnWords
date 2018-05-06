package com.divanoapps.learnwords.exercise;

import com.divanoapps.learnwords.data.local.Card;
import com.divanoapps.learnwords.data.local.Deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by dmitry on 09.10.17.
 */

public class CardDispenserFactory {
    public enum Order {
        alphabetical,
        timestamp,
        random
    }

    public static CardDispenser create(List<Card> cards, Order order) {
        switch (order) {
            case alphabetical:
                return new AlphabeticalCardDispenser(cards);
            case timestamp:
                return new TimestampCardDispenser(cards);
            case random:
                return new RandomCardDispenser(cards);

            default:
                return new AlphabeticalCardDispenser(cards);
        }
    }
}
