package com.divanoapps.learnwords.data.local;

import android.arch.persistence.db.SupportSQLiteQuery;

import com.divanoapps.learnwords.data.Repository;
import com.divanoapps.learnwords.data.Specification;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmitry on 29.04.18.
 */

public class DeckRepository implements Repository<Deck> {
    private DeckDao deckDao;
    private CardDao cardDao;

    DeckRepository(DeckDao deckDao, CardDao cardDao) {
        this.deckDao = deckDao;
        this.cardDao = cardDao;
    }

    @Override
    public void insert(Deck... decks) {
        deckDao.insert(decks);
    }

    @Override
    public void update(Deck deck) {
        deckDao.update(deck);
    }

    @Override
    public void delete(Deck... items) {
        deckDao.delete(items);
    }

//    @Override
//    public void delete(Specification specification) {
//        if (specification instanceof SupportSQLiteQuery)
//            deckDao.delete((SupportSQLiteQuery) specification);
//    }

    @Override
    public List<Deck> query(Specification specification) {
        if (specification instanceof SupportSQLiteQuery) {
            List<Deck> decks = deckDao.query((SupportSQLiteQuery) specification);
            for (Deck deck : decks) {
                deck.setCards(cardDao.query(CardSpecificationsFactory.cardsWithDeckName(deck.getName())));
            }
            return decks;
        }
        else
            return new LinkedList<>();

//        return specification instanceof SupportSQLiteQuery
//            ? deckDao.query((SupportSQLiteQuery) specification)
//            : new LinkedList<>();
    }

//    private List<Card> getCardsFromDeckWithName(String name) {
//        return cardDao.blockingFindAllCardsFromDeckWithName(name);
//    }
//
//    public Single<List<String>> getNames() {
//        return deckDao.getNamesOfNotDeleted(Sync.DELETE);
//    }
//
//    public Single<List<Deck>> getAllDecks() {
//        return Single.fromCallable(() -> {
//            List<Deck> decks = deckDao.blockingSelectNotDeleted(Sync.DELETE);
//            List<Deck> allDecks = new LinkedList<>();
//            for (Deck deck : decks) {
//                if (deck != null) {
//                    deck.setCards(getCardsFromDeckWithName(deck.getName()));
//                    allDecks.add(deck);
//                }
//            }
//            return allDecks;
//        });
//    }
//
//    public Single<Deck> getByName(String name) {
//        return Single.fromCallable(() -> {
//            SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM Deck WHERE name = ? LIMIT 1",
//                new Object[]{name});
//            Deck deck = deckDao.blockingFind(query);
////            Deck deck = deckDao.blockingFind(name);
//            if (deck != null)
//                deck.setCards(getCardsFromDeckWithName(name));
//            return deck;
//        });
//    }
//
//    public Completable insert(Deck deck) {
//        deck.setTimestamp(TimestampFactory.getTimestamp());
//        deck.setSync(Sync.ADD);
//        return Completable.fromAction(() -> deckDao.blockingInsert(deck));
//    }
//
//    public Completable replace(String name, Deck deck) {
//        if (name.equals(deck.getName())) {
//            return Completable.fromAction(() -> deckDao.blockingUpdate(deck));
//        }
//        else {
//            return Completable.fromAction(() -> {
//                Deck oldDeck = deckDao.blockingFind(name);
//                oldDeck.setSync(Sync.DELETE);
//                deckDao.blockingUpdate(oldDeck);
//
//                List<Card> oldCards = cardDao.blockingFindAllCardsFromDeckWithName(name);
//
//                deck.setSync(Sync.ADD);
//                deckDao.blockingInsert(deck);
//
//                for (Card card : oldCards) {
//                    card.setSync(Sync.DELETE);
//                    cardDao.blockingUpdate(card);
//
//                    card.setDeckName(deck.getName());
//                    card.setSync(Sync.ADD);
//                    cardDao.blockingInsert(card);
//                }
//            });
//        }
//    }
//
//    public Completable delete(String name) {
//        return Completable.fromAction(() -> {
//            List<Card> cards = getCardsFromDeckWithName(name);
//            for (Card card : cards) {
//                card.setSync(Sync.DELETE);
//                cardDao.blockingUpdate(card);
//            }
//
//            Deck deck = deckDao.blockingFind(name);
//            deck.setSync(Sync.DELETE);
//            deckDao.blockingUpdate(deck);
//        });
//    }
//
//    public Completable delete(Deck... decks) {
//        return Completable.fromAction(() -> {
//            for (Deck deck : decks) {
//                List<Card> cards = getCardsFromDeckWithName(deck.getName());
//                for (Card card : cards) {
//                    card.setSync(Sync.DELETE);
//                    cardDao.blockingUpdate(card);
//                }
//
//                deck.setSync(Sync.DELETE);
//                deckDao.blockingUpdate(deck);
//            }
//        });
//    }
}
