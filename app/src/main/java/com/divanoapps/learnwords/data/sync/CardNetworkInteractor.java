package com.divanoapps.learnwords.data.sync;

import com.divanoapps.learnwords.data.local.Card;
import com.divanoapps.learnwords.data.remote.Api;
import com.divanoapps.learnwords.data.remote.SyncCard;
import com.divanoapps.learnwords.data.remote.SyncCardDump;
import com.divanoapps.learnwords.data.remote.SyncCardId;
import com.divanoapps.learnwords.data.wombat.NetworkInteractor;
import com.divanoapps.learnwords.data.wombat.Remote;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmitry on 14.05.18.
 */

public class CardNetworkInteractor implements NetworkInteractor<Card, CardId> {
    Api api;

    public CardNetworkInteractor(Api api) {
        this.api = api;
    }

    @Override
    public List<Remote<CardId>> getRemotes(String entity) {
        List<SyncCardDump> syncCardDumps = api.dumpCards();
        List<Remote<CardId>> remotes = new LinkedList<>();
        for (SyncCardDump syncCardDump : syncCardDumps) {
            CardId cardId = new CardId();
            cardId.setDeckName(syncCardDump.getDeckName());
            cardId.setWord(syncCardDump.getWord());
            cardId.setComment(syncCardDump.getComment());

            Remote<CardId> cardIdRemote = new Remote<>();
            cardIdRemote.setId(cardId);
            cardIdRemote.setTimestamp(syncCardDump.getTimestamp());

            remotes.add(cardIdRemote);
        }
        return remotes;
    }

    @Override
    public Card download(CardId id) {
        SyncCardId syncCardId = new SyncCardId();
        syncCardId.setDeckName(id.getDeckName());
        syncCardId.setWord(id.getWord());
        syncCardId.setComment(id.getComment());

        List<SyncCardId> syncCardIds = new LinkedList<>();
        syncCardIds.add(syncCardId);

        List<SyncCard> syncCards = api.queryCards(syncCardIds);
        SyncCard syncCard = syncCards.get(0);

        if (syncCard == null)
            return null;

        Card card = new Card();
        card.setTimestamp(syncCard.getTimestamp());
        card.setSync(null);
        card.setDeckName(syncCard.getDeckName());
        card.setWord(syncCard.getWord());
        card.setComment(syncCard.getComment());
        card.setTranslation(syncCard.getTranslation());
        card.setDifficulty(syncCard.getDifficulty());
        card.setHidden(syncCard.isHidden());

        return card;
    }

    @Override
    public void upload(Card card) {
        SyncCard syncCard = new SyncCard();
        syncCard.setTimestamp(card.getTimestamp());
        syncCard.setDeckName(card.getDeckName());
        syncCard.setWord(card.getWord());
        syncCard.setComment(card.getComment());
        syncCard.setTranslation(card.getTranslation());
        syncCard.setDifficulty(card.getDifficulty());
        syncCard.setHidden(card.isHidden());

        List<SyncCard> syncCards = new LinkedList<>();
        syncCards.add(syncCard);

        api.saveCards(syncCards);
    }

    @Override
    public void deleteOnServer(CardId id) {
        SyncCardId syncCardId = new SyncCardId();
        syncCardId.setDeckName(id.getDeckName());
        syncCardId.setWord(id.getWord());
        syncCardId.setComment(id.getComment());

        List<SyncCardId> syncCardIds = new LinkedList<>();
        syncCardIds.add(syncCardId);

        api.deleteCards(syncCardIds);
    }
}
