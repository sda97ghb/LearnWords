package com.divanoapps.learnwords.data.remote;

import com.divanoapps.learnwords.data.Repository;
import com.divanoapps.learnwords.data.Specification;
import com.divanoapps.learnwords.data.local.Deck;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmitry on 13.05.18.
 */

public class DeckRepository implements Repository<Deck> {
    private Api api;

    DeckRepository(Api api) {
        this.api = api;
    }

    private SyncDeck deckToSyncDeck(Deck deck) {
        SyncDeck syncDeck = new SyncDeck();
        syncDeck.setTimestamp(deck.getTimestamp());
        syncDeck.setName(deck.getName());
        syncDeck.setFromLanguage(deck.getFromLanguage());
        syncDeck.setToLanguage(deck.getToLanguage());
        return syncDeck;
    }

    private Deck syncDeckToDeck(SyncDeck syncDeck) {
        Deck deck = new Deck();
        deck.setSync(null);
        deck.setTimestamp(syncDeck.getTimestamp());
        deck.setName(syncDeck.getName());
        deck.setFromLanguage(syncDeck.getFromLanguage());
        deck.setToLanguage(syncDeck.getToLanguage());
        deck.setCards(null);
        return deck;
    }

    @Override
    public void insert(Deck... decks) {
        List<SyncDeck> syncDecks = new LinkedList<>();
        for (Deck deck : decks)
            syncDecks.add(deckToSyncDeck(deck));
        api.saveDecks(syncDecks);
    }

    @Override
    public void update(Deck deck) {
        List<SyncDeck> syncDecks = new LinkedList<>();
        syncDecks.add(deckToSyncDeck(deck));
        api.saveDecks(syncDecks);
    }

    @Override
    public void delete(Deck... decks) {
        List<String> deckNames = new LinkedList<>();
        for (Deck deck : decks)
            deckNames.add(deck.getName());
        api.deleteDecks(deckNames);
    }

    @Override
    public List<Deck> query(Specification specification) {
        if (specification instanceof SyncDeckNameSpecification) {
            String deckName = ((SyncDeckNameSpecification) specification).getDeckName();
            List<String> deckNames = new LinkedList<>();
            deckNames.add(deckName);
            List<SyncDeck> syncDecks = api.queryDecks(deckNames);
            List<Deck> decks = new LinkedList<>();
            for (SyncDeck syncDeck : syncDecks)
                decks.add(syncDeckToDeck(syncDeck));
            return decks;
        }
        else if (specification instanceof SyncDeckDumpsSpecification) {
            List<SyncDeckDump> syncDeckDumps = api.dumpDecks();
            List<Deck> decks = new LinkedList<>();
            for (SyncDeckDump syncDeckDump : syncDeckDumps) {
                Deck deck = new Deck();
                deck.setName(syncDeckDump.getName());
                deck.setTimestamp(syncDeckDump.getTimestamp());
                decks.add(deck);
            }
            return decks;
        }
        else
            return new LinkedList<>();
    }
}
