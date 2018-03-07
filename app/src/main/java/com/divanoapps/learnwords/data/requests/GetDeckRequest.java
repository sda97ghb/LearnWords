package com.divanoapps.learnwords.data.requests;

import com.divanoapps.learnwords.data.IDB;
import com.divanoapps.learnwords.data.RequestError;
import com.divanoapps.learnwords.entities.Deck;
import com.divanoapps.learnwords.entities.DeckId;

public class GetDeckRequest extends Request<Deck> {

    private DeckId mId;

    public GetDeckRequest(IDB db, DeckId id) {
        super(db);
        mId = id;
    }

    @Override
    protected Deck doInBackground(Void... params) {
        try {
            return getDb().getDeck(mId);
        }
        catch (IDB.NotFoundException e)          { setError(new RequestError(e)); return null; }
        catch (IDB.ConnectionFailureException e) { setError(new RequestError(e)); return null; }
    }
}