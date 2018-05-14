package com.divanoapps.learnwords.data.sync;

import android.support.annotation.NonNull;

import com.divanoapps.learnwords.data.local.Card;
import com.divanoapps.learnwords.data.wombat.EntityToIdConverter;

/**
 * Created by dmitry on 14.05.18.
 */

public class CardToCardIdConverter implements EntityToIdConverter<Card, CardId> {

    @NonNull
    @Override
    public CardId getId(Card card) {
        CardId id = new CardId();
        id.setDeckName(card.getDeckName());
        id.setWord(card.getWord());
        id.setComment(card.getComment());
        return id;
    }

    @Override
    public Class<Card> getEntityClass() {
        return Card.class;
    }
}
