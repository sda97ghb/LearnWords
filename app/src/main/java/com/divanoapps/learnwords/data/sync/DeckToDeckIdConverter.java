package com.divanoapps.learnwords.data.sync;

import android.support.annotation.NonNull;

import com.divanoapps.learnwords.data.local.Deck;
import com.divanoapps.learnwords.data.wombat.EntityToIdConverter;

/**
 * Created by dmitry on 14.05.18.
 */

public class DeckToDeckIdConverter implements EntityToIdConverter<Deck, DeckId> {
    @NonNull
    @Override
    public DeckId getId(@NonNull Deck entity) {
        DeckId deckId = new DeckId();
        deckId.setName(entity.getName());
        return deckId;
    }

    @NonNull
    @Override
    public Class<Deck> getEntityClass() {
        return Deck.class;
    }
}
