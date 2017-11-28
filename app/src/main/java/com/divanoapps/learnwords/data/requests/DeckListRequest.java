package com.divanoapps.learnwords.data.requests;

import com.divanoapps.learnwords.data.IDB;
import com.divanoapps.learnwords.entities.DeckShort;

import java.util.List;

public class DeckListRequest extends Request<List<DeckShort>> {

    public DeckListRequest(IDB db) {
        super(db);
    }

    @Override
    protected List<DeckShort> doInBackground(Void... params) {
        return getDb().getDecks();
    }
}
