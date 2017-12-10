package com.divanoapps.learnwords.data.requests;

import com.divanoapps.learnwords.data.IDB;
import com.divanoapps.learnwords.data.RequestError;
import com.divanoapps.learnwords.entities.DeckId;

public class DeleteDeckRequest extends Request<Void> {

    private DeckId mId;

    public DeleteDeckRequest(IDB db, DeckId id) {
        super(db);
        mId = id;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            getDb().deleteDeck(mId);
            return null;
        }
        catch (IDB.ForbiddenException e) { setError(new RequestError(e)); return null; }
        catch (IDB.NotFoundException e)  { setError(new RequestError(e)); return null; }
        catch (IDB.ConnectionFailureException e) { setError(new RequestError(e)); return null; }
    }
}
