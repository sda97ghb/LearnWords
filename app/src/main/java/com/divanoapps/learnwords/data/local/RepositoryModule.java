package com.divanoapps.learnwords.data.local;

import android.content.Context;

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
}
