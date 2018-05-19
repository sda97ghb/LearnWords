package com.divanoapps.learnwords.data.sync;

import com.divanoapps.learnwords.data.local.Deck;
import com.divanoapps.learnwords.data.remote.Api;
import com.divanoapps.learnwords.data.remote.SyncDeck;
import com.divanoapps.learnwords.data.remote.SyncDeckDump;
import com.divanoapps.learnwords.data.wombat.NetworkInteractor;
import com.divanoapps.learnwords.data.wombat.Remote;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmitry on 14.05.18.
 */

public class DeckNetworkInteractor implements NetworkInteractor<Deck, DeckId> {
    private Api api;

    public DeckNetworkInteractor(Api api) {
        this.api = api;
    }

    @Override
    public List<Remote<DeckId>> getRemotes(String entity) {
        List<SyncDeckDump> syncDeckDumps = api.dumpDecks();
        List<Remote<DeckId>> remotes = new LinkedList<>();

        for (SyncDeckDump syncDeckDump : syncDeckDumps) {
            DeckId deckId = new DeckId(syncDeckDump.getName());

            Remote<DeckId> remote = new Remote<>();
            remote.setId(deckId);
            remote.setTimestamp(syncDeckDump.getTimestamp());

            remotes.add(remote);
        }

        return remotes;
    }

    @Override
    public Deck download(DeckId id) {
        List<String> deckNames = new LinkedList<>();
        deckNames.add(id.getName());

        List<SyncDeck> syncDecks = api.queryDecks(deckNames);
        SyncDeck syncDeck = syncDecks.get(0);

        if (syncDeck == null)
            return null;

        Deck deck = new Deck();
        deck.setTimestamp(syncDeck.getTimestamp());
        deck.setSync(null);
        deck.setName(syncDeck.getName());
        deck.setFromLanguage(syncDeck.getFromLanguage());
        deck.setToLanguage(syncDeck.getToLanguage());

        return deck;
    }

    @Override
    public void upload(Deck deck) {
        SyncDeck syncDeck = new SyncDeck();
        syncDeck.setTimestamp(deck.getTimestamp());
        syncDeck.setName(deck.getName());
        syncDeck.setFromLanguage(deck.getFromLanguage());
        syncDeck.setToLanguage(deck.getToLanguage());

        List<SyncDeck> syncDecks = new LinkedList<>();
        syncDecks.add(syncDeck);

        api.saveDecks(syncDecks);
    }

    @Override
    public void deleteOnServer(DeckId id) {
        List<String> deckNames = new LinkedList<>();
        deckNames.add(id.getName());
        api.deleteDecks(deckNames);
    }
}
