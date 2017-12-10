package com.divanoapps.learnwords.data.requests;

import com.divanoapps.learnwords.data.IDB;
import com.divanoapps.learnwords.data.RequestError;
import com.divanoapps.learnwords.entities.CardId;

public class DeleteCardRequest extends Request<Void> {

    private CardId mId;

    public DeleteCardRequest(IDB db, CardId id) {
        super(db);
        mId = id;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            getDb().deleteCard(mId);
            return null;
        }
        catch (IDB.ForbiddenException e) { setError(new RequestError(e)); return null; }
        catch (IDB.NotFoundException e)  { setError(new RequestError(e)); return null; }
        catch (IDB.ConnectionFailureException e) { setError(new RequestError(e)); return null; }
    }
}
