package com.divanoapps.learnwords.sync;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmitry on 12.05.18.
 */

class ClientCardSyncDump {
    String deckName;
    String word;
    String comment;
    long timestamp;
    int sync;

    public ClientCardSyncDump(String deckName, String word, String comment, long timestamp, int sync) {
        this.deckName = deckName;
        this.word = word;
        this.comment = comment;
        this.timestamp = timestamp;
        this.sync = sync;
    }
}

class ClientDeckSyncDump {
    String name;
    long timestamp;
    int sync;

    public ClientDeckSyncDump(String name, long timestamp, int sync) {
        this.name = name;
        this.timestamp = timestamp;
        this.sync = sync;
    }
}

class ServerCardSyncDump {
    String deckName;
    String word;
    String comment;
    long timestamp;

    public ServerCardSyncDump(String deckName, String word, String comment, long timestamp) {
        this.deckName = deckName;
        this.word = word;
        this.comment = comment;
        this.timestamp = timestamp;
    }
}

class ServerDeckSyncDump {
    String name;
    long timestamp;

    public ServerDeckSyncDump(String name, long timestamp) {
        this.name = name;
        this.timestamp = timestamp;
    }
}

public class SyncPlanner {
    List<ClientCardSyncDump> clientCardSyncDumps;
    List<ClientDeckSyncDump> clientDeckSyncDumps;
    List<ServerCardSyncDump> serverCardSyncDumps;
    List<ServerDeckSyncDump> serverDeckSyncDumps;

    void makePlan() {
//        List<> needAddOnServer;
//        List<> needDeleteOnServer;
//        List<> needAddOnClient;
//        List<> needDeleteOnClient;

        List<ClientCardSyncDump> notPresentOnServer = new LinkedList<>();
        for (ClientCardSyncDump clientCardSyncDump : clientCardSyncDumps) {
            ServerCardSyncDump serverCardSyncDump = new ServerCardSyncDump(clientCardSyncDump.deckName, clientCardSyncDump.word, clientCardSyncDump.comment, clientCardSyncDump.timestamp);
            if (!serverCardSyncDumps.contains(serverCardSyncDump))
                notPresentOnServer.add(clientCardSyncDump);
        }
    }
}