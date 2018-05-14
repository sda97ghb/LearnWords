package com.divanoapps.learnwords.data.remote;

import com.divanoapps.learnwords.data.dogfish.ServiceExecutor;
import com.google.gson.Gson;

/**
 * Created by dmitry on 13.05.18.
 */

public class RepositoryModule {
    private Api api;

    public RepositoryModule(String apiToken) {
        RetrofitRequestExecutor retrofitRequestExecutor = new RetrofitRequestExecutor();
        Gson gson = new Gson();
        api = new ServiceExecutor<>(retrofitRequestExecutor, gson, Api.class, apiToken).getService();
    }

    public DeckRepository deckRepository() {
        return new DeckRepository(api);
    }

    public CardRepository cardRepository() {
        return new CardRepository(api);
    }
}
