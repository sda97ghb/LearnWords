package com.divanoapps.learnwords.data.local;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by dmitry on 29.04.18.
 */

public class DeckRepository {
    private DeckDao deckDao;
    private CardDao cardDao;

    DeckRepository(DeckDao deckDao, CardDao cardDao) {
        this.deckDao = deckDao;
        this.cardDao = cardDao;
    }

    private List<Card> getCardsFromDeckWithName(String name) {
        return cardDao.findAllCardsFromDeckWithNameBlocking(name);
    }

    public Single<List<String>> getNames() {
        return deckDao.getNames();
    }

    public Single<List<Deck>> getAllDecks() {
        return deckDao.getAll().map(decks -> {
            List<Deck> allDecks = new ArrayList<>();
            for (Deck deck : decks) {
                if (deck != null) {
                    deck.setCards(getCardsFromDeckWithName(deck.getName()));
                    allDecks.add(deck);
                }
            }
            return allDecks;
        });
    }

    public Single<Deck> getByName(String name) {
        return deckDao.find(name).map(deck -> {
            if (deck != null)
                deck.setCards(getCardsFromDeckWithName(name));
            return deck;
        });
    }

    public Completable insert(Deck deck) {
        deck.setTimestamp(TimestampFactory.getTimestamp());
        return Completable.fromAction(() -> deckDao.insert(deck));
    }

    public Completable replace(String name, Deck deck) {
        deck.setTimestamp(TimestampFactory.getTimestamp());
        return Completable.fromAction(() -> {
            deckDao.delete(name);
            deckDao.insert(deck);
        });
    }

    public Completable delete(String name) {
        return Completable.fromAction(() -> deckDao.delete(name));
    }

    public Completable delete(Deck... decks) {
        return Completable.fromAction(() -> deckDao.delete(decks));
    }
}
