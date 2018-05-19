package com.divanoapps.learnwords.data.sync;

import com.divanoapps.learnwords.data.Repository;
import com.divanoapps.learnwords.data.local.Card;
import com.divanoapps.learnwords.data.local.CardSpecificationsFactory;
import com.divanoapps.learnwords.data.local.Deck;
import com.divanoapps.learnwords.data.local.DeckSpecificationsFactory;
import com.divanoapps.learnwords.data.remote.Api;
import com.divanoapps.learnwords.data.wombat.CollectionSync;

/**
 * Created by dmitry on 14.05.18.
 */

public class Sync {
    private Api api;
    private Repository<Deck> deckRepository;
    private Repository<Card> cardRepository;

    public Sync(Api api, Repository<Deck> deckRepository, Repository<Card> cardRepository) {
        this.api = api;
        this.deckRepository = deckRepository;
        this.cardRepository = cardRepository;
    }

    public CollectionSync<Deck, DeckId> getDeckSync() {
        return new CollectionSync<>(
            Deck.class,
            deckRepository,
            DeckSpecificationsFactory.allDecks(),
            new DeckGetByIdSpecificationFactory(),
            new DeckToDeckIdConverter(),
            new DeckNetworkInteractor(api),
            new DeckArrayFactory()
        );
    }

    public CollectionSync<Card, CardId> getCardSync() {
        return new CollectionSync<>(
            Card.class,
            cardRepository,
            CardSpecificationsFactory.allCards(),
            new CardGetByIdSpecificationFactory(),
            new CardToCardIdConverter(),
            new CardNetworkInteractor(api),
            new CardArrayFactory()
        );
    }
}
