package com.divanoapps.learnwords.data.local;

import java.util.ArrayList;
import java.util.LinkedList;
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
        return cardDao.blockingFindAllCardsFromDeckWithName(name);
    }

    public Single<List<String>> getNames() {
        return deckDao.getNamesOfNotDeleted(Sync.DELETE);
    }

    public Single<List<Deck>> getAllDecks() {
        return Single.fromCallable(() -> {
            List<Deck> decks = deckDao.blockingSelectNotDeleted(Sync.DELETE);
            List<Deck> allDecks = new LinkedList<>();
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
        return Single.fromCallable(() -> {
            Deck deck = deckDao.blockingFind(name);
            if (deck != null)
                deck.setCards(getCardsFromDeckWithName(name));
            return deck;
        });
    }

    public Completable insert(Deck deck) {
        deck.setTimestamp(TimestampFactory.getTimestamp());
        deck.setSync(Sync.ADD);
        return Completable.fromAction(() -> deckDao.blockingInsert(deck));
    }

    public Completable replace(String name, Deck deck) {
        if (name.equals(deck.getName())) {
            return Completable.fromAction(() -> deckDao.blockingUpdate(deck));
        }
        else {
            return Completable.fromAction(() -> {
                Deck oldDeck = deckDao.blockingFind(name);
                oldDeck.setSync(Sync.DELETE);
                deckDao.blockingUpdate(oldDeck);

                List<Card> oldCards = cardDao.blockingFindAllCardsFromDeckWithName(name);

                deck.setSync(Sync.ADD);
                deckDao.blockingInsert(deck);

                for (Card card : oldCards) {
                    card.setSync(Sync.DELETE);
                    cardDao.blockingUpdate(card);

                    card.setDeckName(deck.getName());
                    card.setSync(Sync.ADD);
                    cardDao.blockingInsert(card);
                }
            });
        }
    }

    public Completable delete(String name) {
        return Completable.fromAction(() -> {
            List<Card> cards = getCardsFromDeckWithName(name);
            for (Card card : cards) {
                card.setSync(Sync.DELETE);
                cardDao.blockingUpdate(card);
            }

            Deck deck = deckDao.blockingFind(name);
            deck.setSync(Sync.DELETE);
            deckDao.blockingUpdate(deck);
        });
    }

    public Completable delete(Deck... decks) {
        return Completable.fromAction(() -> {
            for (Deck deck : decks) {
                List<Card> cards = getCardsFromDeckWithName(deck.getName());
                for (Card card : cards) {
                    card.setSync(Sync.DELETE);
                    cardDao.blockingUpdate(card);
                }

                deck.setSync(Sync.DELETE);
                deckDao.blockingUpdate(deck);
            }
        });
    }
}
