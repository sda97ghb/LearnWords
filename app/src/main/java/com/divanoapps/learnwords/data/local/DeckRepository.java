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
}
