package com.divanoapps.learnwords.data.requests;

import com.divanoapps.learnwords.data.IDB;
import com.divanoapps.learnwords.data.RequestError;
import com.divanoapps.learnwords.entities.Deck;
import com.divanoapps.learnwords.entities.DeckId;

public class UpdateDeckRequest extends Request<Void> {

    private DeckId mId;
    private Deck mNewDeck;

    public UpdateDeckRequest(IDB db, DeckId id, Deck newDeck) {
        super(db);
        mId = id;
        mNewDeck = newDeck;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            getDb().updateDeck(mId, mNewDeck);
            return null;
        }
        catch (IDB.ForbiddenException e)     { setError(new RequestError(e)); return null; }
        catch (IDB.NotFoundException e)      { setError(new RequestError(e)); return null; }
        catch (IDB.AlreadyExistsException e) { setError(new RequestError(e)); return null; }
        catch (IDB.ConnectionFailureException e) { setError(new RequestError(e)); return null; }
    }
}
