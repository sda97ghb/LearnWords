package com.divanoapps.learnwords;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a deck as a named bunch of cards.
 *
 * @author Dmitry Smirnov
 */
public class Deck {
    private String name;
    private List<Card> cards;
    private List<String> incorrectLines;

    public Deck(@NonNull String name)
    {
        this.name = name;
        this.cards = new ArrayList<>();
        this.incorrectLines = new ArrayList<>();
    }

//    public Deck(Deck other)
//    {
//        this.name = other.getName();
//        this.cards = new ArrayList<>(other.getCards());
//        this.incorrectLines = new ArrayList<>(other.getIncorrectLines());
//    }

    public String getName()
    {
        return name;
    }

    public List<Card> getCards()
    {
        return cards;
    }

    public List<String> getIncorrectLines() {
        return incorrectLines;
    }

    public boolean hasIncorrectLines() {
        return incorrectLines != null && !(incorrectLines.isEmpty());
    }
}
