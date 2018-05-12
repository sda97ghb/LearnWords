package com.divanoapps.learnwords.data.local;

import android.content.Context;

import com.divanoapps.learnwords.data.RxRepository;

/**
 * Created by dmitry on 01.05.18.
 */

public class RepositoryModule {
    private DeckDao deckDao;
    private CardDao cardDao;

    public RepositoryModule(Context context) {
        Database database = new DatabaseAccessor(context).getStorageDatabase();
        deckDao = database.deckDao();
        cardDao = database.cardDao();
    }

    public CardRepository getCardRepository() {
        return new CardRepository(cardDao);
    }

    public DeckRepository getDeckRepository() {
        return new DeckRepository(deckDao, cardDao);
    }

    public RxRepository<Card> getCardRxRepository() {
        return new RxRepository<>(new CardRepository(cardDao));
    }

    public RxRepository<Deck> getDeckRxRepository() {
        return new RxRepository<>(new DeckRepository(deckDao, cardDao));
    }
}
