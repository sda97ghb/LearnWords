package com.divanoapps.learnwords.data.local;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by dmitry on 29.04.18.
 */

public class CardRepository {
    private CardDao cardDao;

    CardRepository(CardDao cardDao) {
        this.cardDao = cardDao;
    }

    public Single<List<Card>> getAllCards() {
        return cardDao.getAll();
    }

    public Single<Card> find(String deckName, String word, String comment) {
        return cardDao.find(deckName, word, comment);
    }

    public Completable insert(Card card) {
        card.setTimestamp(TimestampFactory.getTimestamp());
        return Completable.fromAction(() -> cardDao.insert(card));
    }

    public Completable replace(String deckName, String word, String comment, Card card) {
        card.setTimestamp(TimestampFactory.getTimestamp());
        return Completable.fromAction(() -> {
            cardDao.delete(deckName, word, comment);
            cardDao.insert(card);
        });
    }

    public Completable delete(String deckName, String word, String comment) {
        return Completable.fromAction(() -> cardDao.delete(deckName, word, comment));
    }

    public Completable delete(List<Card> cards) {
        return Completable.fromAction(() -> cardDao.delete(cards.toArray(new Card[cards.size()])));
    }
}
