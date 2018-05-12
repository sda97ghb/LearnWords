package com.divanoapps.learnwords.data.local;

import android.arch.persistence.db.SupportSQLiteQuery;

import com.divanoapps.learnwords.data.Repository;
import com.divanoapps.learnwords.data.Specification;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

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

//    @Override
//    public void delete(Specification specification) {
//        if (specification instanceof SupportSQLiteQuery)
//            cardDao.delete((SupportSQLiteQuery) specification);
//    }

    @Override
    public List<Card> query(Specification specification) {
        return specification instanceof SupportSQLiteQuery
            ? cardDao.query((SupportSQLiteQuery) specification)
            : new LinkedList<>();
    }

//    public Single<List<Card>> getAllCards() {
//        return cardDao.selectNotDeleted(Sync.DELETE);
//    }
//
//    public Single<Card> find(String deckName, String word, String comment) {
//        return cardDao.find(deckName, word, comment);
//    }
//
//    public Completable insert(Card card) {
//        card.setTimestamp(TimestampFactory.getTimestamp());
//        card.setSync(Sync.ADD);
//        return Completable.fromAction(() -> cardDao.blockingInsert(card));
//    }
//
//    public Completable replace(String deckName, String word, String comment, Card card) {
//        card.setTimestamp(TimestampFactory.getTimestamp());
//        if (card.getDeckName().equals(deckName) &&
//            card.getWord().equals(word) &&
//            card.getComment().equals(comment))
//        {
//            return Completable.fromAction(() -> cardDao.blockingUpdate(card));
//        }
//        else
//        {
//            return Completable.fromAction(() -> {
//                Card oldCard = cardDao.blockingFind(deckName, word, comment);
//                oldCard.setSync(Sync.DELETE);
//                cardDao.blockingUpdate(oldCard);
//
//                card.setSync(Sync.ADD);
//                cardDao.blockingInsert(card);
//            });
//        }
//    }
//
//    public Completable delete(String deckName, String word, String comment) {
//        return Completable.fromAction(() -> {
//            Card card = cardDao.blockingFind(deckName, word, comment);
//            card.setSync(Sync.DELETE);
//            cardDao.blockingUpdate(card);
//        });
//    }
//
//    public Completable delete(List<Card> cards) {
//        return Completable.fromAction(() -> {
//            for (Card card : cards) {
//                card.setSync(Sync.DELETE);
//                cardDao.blockingUpdate(card);
//            }
//        });
//    }
}
