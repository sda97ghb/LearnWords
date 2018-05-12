package com.divanoapps.learnwords.data.local;

import android.arch.persistence.db.SupportSQLiteQuery;

import com.divanoapps.learnwords.data.Repository;
import com.divanoapps.learnwords.data.Specification;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmitry on 29.04.18.
 */

public class CardRepository implements Repository<Card> {
    private CardDao cardDao;

    CardRepository(CardDao cardDao) {
        this.cardDao = cardDao;
    }

    @Override
    public void insert(Card... cards) {
        cardDao.insert(cards);
    }

    @Override
    public void update(Card card) {
        cardDao.update(card);
    }

    @Override
    public void delete(Card... cards) {
        cardDao.delete(cards);
    }

    @Override
    public List<Card> query(Specification specification) {
        return specification instanceof SupportSQLiteQuery
            ? cardDao.query((SupportSQLiteQuery) specification)
            : new LinkedList<>();
    }
}
