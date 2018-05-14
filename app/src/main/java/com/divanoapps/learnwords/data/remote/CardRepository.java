package com.divanoapps.learnwords.data.remote;

import com.divanoapps.learnwords.data.Repository;
import com.divanoapps.learnwords.data.Specification;
import com.divanoapps.learnwords.data.local.Card;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmitry on 12.05.18.
 */

public class CardRepository implements Repository<Card> {
    private Api api;

    CardRepository(Api api) {
        this.api = api;
    }

    private SyncCard cardToSyncCard(Card card) {
        SyncCard syncCard = new SyncCard();
        syncCard.setTimestamp(card.getTimestamp());
        syncCard.setDeckName(card.getDeckName());
        syncCard.setWord(card.getWord());
        syncCard.setComment(card.getComment());
        syncCard.setTranslation(card.getTranslation());
        syncCard.setDifficulty(card.getDifficulty());
        syncCard.setHidden(card.isHidden());
        return syncCard;
    }

    private SyncCardId cardToSyncCardId(Card card) {
        SyncCardId syncCardId = new SyncCardId();
        syncCardId.setDeckName(card.getDeckName());
        syncCardId.setWord(card.getWord());
        syncCardId.setComment(card.getComment());
        return syncCardId;
    }

    private Card syncCardToCard(SyncCard syncCard) {
        Card card = new Card();
        card.setSync(null);
        card.setTimestamp(syncCard.getTimestamp());
        card.setDeckName(syncCard.getDeckName());
        card.setWord(syncCard.getWord());
        card.setComment(syncCard.getComment());
        card.setTranslation(syncCard.getTranslation());
        card.setDifficulty(syncCard.getDifficulty());
        card.setHidden(syncCard.isHidden());
        return card;
    }

    @Override
    public void insert(Card... cards) {
        List<SyncCard> syncCards = new LinkedList<>();
        for (Card card : cards)
            syncCards.add(cardToSyncCard(card));
        api.saveCards(syncCards);
    }

    @Override
    public void update(Card card) {
        List<SyncCard> syncCards = new LinkedList<>();
        syncCards.add(cardToSyncCard(card));
        api.saveCards(syncCards);
    }

    @Override
    public void delete(Card... cards) {
        List<SyncCardId> syncCardIds = new LinkedList<>();
        for (Card card : cards)
            syncCardIds.add(cardToSyncCardId(card));
        api.deleteCards(syncCardIds);
    }

    @Override
    public List<Card> query(Specification specification) {
        if (specification instanceof SyncCardIdSpecification) {
            SyncCardId syncCardId = ((SyncCardIdSpecification) specification).getSyncCardId();
            List<SyncCardId> syncCardIds = new LinkedList<>();
            syncCardIds.add(syncCardId);
            List<SyncCard> syncCards = api.queryCards(syncCardIds);
            List<Card> cards = new LinkedList<>();
            for (SyncCard syncCard : syncCards)
                cards.add(syncCardToCard(syncCard));
            return cards;
        }
        else {
            return new LinkedList<>();
        }
    }
}
