package com.divanoapps.learnwords.data.sync;

import com.divanoapps.learnwords.data.Specification;
import com.divanoapps.learnwords.data.local.CardSpecificationsFactory;
import com.divanoapps.learnwords.data.wombat.GetByIdSpecificationFactory;

/**
 * Created by dmitry on 14.05.18.
 */

public class CardGetByIdSpecificationFactory implements GetByIdSpecificationFactory<CardId> {
    @Override
    public Specification create(CardId id) {
        return CardSpecificationsFactory.byDeckNameAndWordAndComment(id.getDeckName(), id.getWord(), id.getComment());
    }
}
