package com.divanoapps.learnwords.data.requests;

import com.divanoapps.learnwords.data.IDB;
import com.divanoapps.learnwords.data.RequestError;
import com.divanoapps.learnwords.entities.Deck;

public class SaveDeckRequest extends Request<Void> {

    private Deck mDeck;

    public SaveDeckRequest(IDB db, Deck deck) {
        super(db);
        mDeck = deck;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            getDb().saveDeck(mDeck);
            return null;
        }
        catch (IDB.ForbiddenException e) { setError(new RequestError(e)); return null; }
        catch (IDB.ConnectionFailureException e) { setError(new RequestError(e)); return null; }
    }
}
