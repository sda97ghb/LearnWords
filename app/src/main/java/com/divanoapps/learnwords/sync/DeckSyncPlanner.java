package com.divanoapps.learnwords.sync;

import com.divanoapps.learnwords.data.Repository;
import com.divanoapps.learnwords.data.local.Deck;
import com.divanoapps.learnwords.data.local.DeckSpecificationsFactory;
import com.divanoapps.learnwords.data.remote.Api;
import com.divanoapps.learnwords.data.remote.SyncDeckDumpsSpecification;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmitry on 12.05.18.
 */

public class DeckSyncPlanner {
    static class DeckDump {
        int sync;
        long timestamp;
        String name;

        public DeckDump(int sync, long timestamp, String name) {
            this.sync = sync;
            this.timestamp = timestamp;
            this.name = name;
        }

        public int getSync() {
            return sync;
        }

        public void setSync(int sync) {
            this.sync = sync;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    private Repository<Deck> localRepository;
    private Repository<Deck> remoteRepository;

    private List<DeckDump> getLocalDeckDumps() {
        List<DeckDump> localDeckDumps = new LinkedList<>();
        for (Deck deck : localRepository.query(DeckSpecificationsFactory.allDecks())) {
            int sync = deck.getSync() == null ? 0 : deck.getSync();
            localDeckDumps.add(new DeckDump(sync, deck.getTimestamp(), deck.getName()));
        }
        return localDeckDumps;
    }

    private List<DeckDump> getRemoteDeckDumps() {
        List<DeckDump> remoteDeckDumps = new LinkedList<>();
        for (Deck deck : remoteRepository.query(new SyncDeckDumpsSpecification()))
            remoteDeckDumps.add(new DeckDump(0, deck.getTimestamp(), deck.getName()));
        return remoteDeckDumps;
    }

    private List<DeckDump> intersection(List<DeckDump> first, List<DeckDump> second) {
        List<DeckDump> result = new LinkedList<>(first);
        result.retainAll(second);
        return result;
    }

    void makePlan() {
        List<DeckDump> localDeckDumps = getLocalDeckDumps();
        List<DeckDump> remoteDeckDumps = getRemoteDeckDumps();

        List<DeckDump> bothHave = intersection(localDeckDumps, remoteDeckDumps);

        List<DeckDump> serverHas = new LinkedList<>(remoteDeckDumps);
        serverHas.removeAll(bothHave);

        List<DeckDump> clientHas = new LinkedList<>(localDeckDumps);
        clientHas.removeAll(bothHave);

        List<DeckDump> needAddOnServer = new LinkedList<>();
        List<DeckDump> needDeleteOnServer = new LinkedList<>();
        List<DeckDump> needAddOnClient = new LinkedList<>();
        List<DeckDump> needDeleteOnClient = new LinkedList<>();
    }
}